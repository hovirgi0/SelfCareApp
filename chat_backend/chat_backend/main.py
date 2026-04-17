# FastAPI szerver belépési pontja
# Docs: https//fastapi.tiangolo.com

from fastapi import FastAPI
from pydantic import BaseModel
from chatbot import get_response
import uuid

app = FastAPI()

# In-memory session store: session_id → list of {"role": "user"/"bot", "text": str}
sessions: dict = {}

# Request body séma
class ChatRequest(BaseModel):
    message: str
    tone: str = "friendly"
    style: str = "neutral"
    session_id: str = "" # conversation session

class ChatResponse(BaseModel):
    response: str
    session_id: str  # send it back so Android can store it

# POST /chat végpont
@app.post("/chat", response_model=ChatResponse)
def chat(request: ChatRequest):
    # Create or retrieve session
    sid = request.session_id if request.session_id else str(uuid.uuid4())
    if sid not in sessions:
        sessions[sid] = []

    history = sessions[sid]
    history.append({"role": "user", "text": request.message})

    response = get_response(request.message, request.tone, request.style, history)

    history.append({"role": "bot", "text": response})
    sessions[sid] = history  # save back

    return {"response": response, "session_id": sid}

@app.delete("/chat/{session_id}")  # user can reset conversation
def reset_chat(session_id: str):
    sessions.pop(session_id, None)
    return {"status": "reset"}