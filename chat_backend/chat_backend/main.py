# --- FastAPI Backend ---
# REST API backend for the wellbeing chatbot system.
#
# Responsibilities:
# - Manage user chat sessions
# - Generate LLM responses through Gemini API
# - Maintain lightweight conversational state
# - Provide session reset functionality

from fastapi import FastAPI
from pydantic import BaseModel
import uuid

from google import genai
from google.genai import types
import os
from chatbot import (
    get_system_prompt,
    detect_category,
    get_last_bot_category,
    ARCS
)

client = genai.Client(api_key=os.environ["GEMINI_API_KEY"])

app = FastAPI()

# --- In-Memory Session Storage ---
# Stores lightweight conversation state per user session.
#
# Structure:
# session_id -> {
#     "history": [...],
#     "state": "idle"
# }
#
# Limitation:
# Data is stored only in memory and is lost
# when the server restarts.

sessions = {}

# --- Request Schema ---

class ChatRequest(BaseModel):
    """
    Request model for incoming chat messages.
    """

    message: str
    tone: str = "friendly"
    style: str = "supportive"
    session_id: str = ""


# --- Chat Endpoint ---

@app.post("/chat")
def chat(request: ChatRequest):

    # Load existing session or create a new one
    sid = (
        request.session_id
        if request.session_id
        else str(uuid.uuid4())
    )

    if sid not in sessions:
        sessions[sid] = {
            "history": [],
            "state": "idle"
        }

    session = sessions[sid]

    # Detect conversation category
    category = detect_category(request.message)

    # Short replies inherit previous context
    if category == "default" and len(request.message.split()) <= 3:
        category = get_last_bot_category(session["history"])

    # Generate dynamic system prompt
    system_prompt = get_system_prompt(
        tone=request.tone,
        style=request.style,
        state=session["state"],
        category=category,
        history=session["history"]
    )

    # --- Build LLM Conversation History ---
    # Convert local message format into Groq-compatible format.

    client_messages = []

    for h in session["history"]:

        role = (
            "user"
            if h["role"] == "user"
            else "assistant"
        )

        client_messages.append({
            "role": role,
            "content": h["content"]
        })

    client_messages.append({
        "role": "user",
        "content": request.message
    })

    # --- Generate LLM Response ---

    # Convert history to Gemini format (all messages except the last user one)
    gemini_history = [
        types.Content(
            role="user" if msg["role"] == "user" else "model",
            parts=[types.Part(text=msg["content"])]
        )
        for msg in client_messages[:-1]
    ]

    response = client.models.generate_content(
        model="gemini-3.1-flash-lite",
        contents=gemini_history + [
            types.Content(role="user", parts=[types.Part(text=request.message)])
        ],
        config=types.GenerateContentConfig(
            system_instruction=system_prompt,
            max_output_tokens=500
        )
    )

    response_text = response.text

    # --- Update Conversational Arc State ---

    new_state = session["state"]

    # Continue active conversational arc
    if session["state"].startswith("arc_"):

        parts = session["state"].split("_")

        arc_cat = parts[1]
        arc_step = int(parts[2])

        next_step = arc_step + 1

        if arc_cat in ARCS and next_step < len(ARCS[arc_cat]):

            new_state = f"arc_{arc_cat}_{next_step}"

        else:
            new_state = "idle"

    # Start new conversational arc
    elif category in ARCS:

        new_state = f"arc_{category}_0"

    # --- Update Session Memory ---

    session["history"].append({
        "role": "user",
        "content": request.message
    })

    session["history"].append({
        "role": "bot",
        "content": response_text
    })

    session["state"] = new_state

    return {
        "response": response_text,
        "session_id": sid
    }


# --- Session Reset Endpoint ---

@app.delete("/chat/{session_id}")
def reset_chat(session_id: str):
    """
    Deletes a conversation session and resets its state.
    """

    sessions.pop(session_id, None)

    return {
        "status": "reset"
    }