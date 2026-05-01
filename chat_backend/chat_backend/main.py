from fastapi import FastAPI
from pydantic import BaseModel
import uuid
from groq import Groq
from chatbot import  get_system_prompt, detect_category, get_last_bot_category, ARCS

app = FastAPI()
# Ez egy egyszerű memóriában tárolt szótár: session_id -> {history, state}
# Minden felhasználónak saját "emlékezete" van, így nem keverednek össze a beszélgetések.
# Fontos korlát: ha a szerver újraindul, minden session elvész (RAM-ban van, nem adatbázisban).
sessions = {}

#Groc client: API key from enviroment variable GROQ_API_KEY
client = Groq()

class ChatRequest(BaseModel):
    message: str
    tone: str = "friendly"
    style: str = "supportive"
    session_id: str = ""

@app.post("/chat")
def chat(request: ChatRequest):
    # 1. Session betöltése vagy létrehozása
    sid = request.session_id if request.session_id else str(uuid.uuid4())
    if sid not in sessions:
        sessions[sid] = {"history": [], "state": "idle"}

    session = sessions[sid]

    # Detect category + arc state
    category = detect_category(request.message)
    if category == "default" and len(request.message.split()) <= 3:
        category = get_last_bot_category(session["history"])

    # 2. Válasz generálása (MÉG a history frissítése előtt, hogy lássa, üres-e!)
    # Így az első üzenetre az üdvözlést adja, a másodikra már a reakciót
    system_prompt = get_system_prompt(
        tone=request.tone,
        style=request.style,
        state=session["state"],
        category=category,
        history=session["history"]
    )

    # Build message history for Groq Client (convert format to Groq's format)
    client_messages = []
    for h in session["history"]:
        role = "user" if h["role"] == "user" else "assistant"
        client_messages.append({"role": role, "content": h["content"]})
    client_messages.append({"role": "user", "content": request.message})

    # Call Groq Client
    response = client.chat.completions.create(
        model="llama-3.3-70b-versatile",
        max_tokens=300,
        messages=[
            {"role": "system", "content": system_prompt},  # system prompt itt van
            *client_messages                                # majd a beszélgetés history
        ]
    )

    # A válasz kiolvasása
    response_text = response.choices[0].message.content

    # Update arc state
    new_state = session["state"]
    # HA már arc-ban vagyunk (pl. "arc_stress_1"):
    if session["state"].startswith("arc_"):
        parts = session["state"].split("_")          # ["arc", "stress", "1"]
        arc_cat, arc_step = parts[1], int(parts[2])  # "stress", 1
        next_step = arc_step + 1                     # 2

        if arc_cat in ARCS and next_step < len(ARCS[arc_cat]):
            new_state = f"arc_{arc_cat}_{next_step}" # "arc_stress_2" → folytatjuk
        else:
            new_state = "idle"                       # elfogytak a lépések → vége az arc-nak

    # HA új témát detektáltunk és nincs aktív arc:
    elif category in ARCS:
        new_state = f"arc_{category}_0"             # elindítjuk az arc-ot a 0. lépéstől

    # 3. History és állapot frissítése
    session["history"].append({"role": "user", "content": request.message})
    session["history"].append({"role": "bot", "content": response_text})
    session["state"] = new_state

    return {"response": response_text, "session_id": sid}

@app.delete("/chat/{session_id}")
def reset_chat(session_id: str):
    sessions.pop(session_id, None)
    return {"status": "reset"}