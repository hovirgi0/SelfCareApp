from fastapi import FastAPI
from pydantic import BaseModel
import uuid
from chatbot import get_response

app = FastAPI()
sessions = {}

class ChatRequest(BaseModel):
    message: str
    tone: str = "friendly"
    session_id: str = ""

@app.post("/chat")
def chat(request: ChatRequest):
    # 1. Session betöltése vagy létrehozása
    sid = request.session_id if request.session_id else str(uuid.uuid4())
    if sid not in sessions:
        sessions[sid] = {"history": [], "state": "idle"}

    session = sessions[sid]

    # 2. Válasz generálása (MÉG a history frissítése előtt, hogy lássa, üres-e!)
    # Így az első üzenetre az üdvözlést adja, a másodikra már a reakciót
    response_text, new_state = get_response(
        request.message,
        request.tone,
        session["history"],
        session["state"]
    )

    # 3. History és állapot frissítése
    session["history"].append({"role": "user", "content": request.message})
    session["history"].append({"role": "bot", "content": response_text})
    session["state"] = new_state

    return {"response": response_text, "session_id": sid}

@app.delete("/chat/{session_id}")
def reset_chat(session_id: str):
    sessions.pop(session_id, None)
    return {"status": "reset"}