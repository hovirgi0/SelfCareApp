# FastAPI szerver belépési pontja
# Docs: https//fastapi.tiangolo.com
from anyio import streams
from fastapi import FastAPI
from pydantic import BaseModel
from chatbot import get_response

app = FastAPI()

# Request body séma
class ChatRequest(BaseModel):
    message: str
    tone: str = "friendly"
    style: str = "neutral"

# POST /chat végpont
@app.post("/chat")
def chat(request: ChatRequest):
    reply = get_response(request.message, request.tone)
    return {"reply": reply}