# Chatbot logika - keyword matching alapú válaszgenerátor
# Feminist HCI elvek: felhasználó hangvételi preferenciája érvényesül (agency, empowerment)

KEYWORDS = {
    "stress":  ["stressz", "fáradt", "túlterhelt", "nehéz", "rossz", "sír", "ideges"],
    "happy":   ["boldog", "jó", "szuper", "klassz", "örülök", "remek"],
    "todo":    ["feladat", "tennivaló", "lista", "elvégzett", "tervek"],
    "journal": ["napló", "leírnám", "érzés", "gondolat", "ma"],
}

RESPONSES = {
    "stress": {
        "neutral":   "Megértem, hogy most nehéz. Hogyan tudok segíteni?.",
        "friendly":  "Látom, hogy most nehéz. Én mindig itt vagyok.",
        "calm":      "Vegyél egy mély levegőt. Ez is elmúlik.",
        "energetic": "Meg tudod csinálni - egy lépés egyszerre!",
    },
    "happy": {
        "neutral":   "Örülök, hogy jól érzed magad.",
        "friendly":  "De jó hallani ezt! Örülök neked ",
        "calm":      "Élvezd ezt a pillanatot.",
        "energetic": "Fantasztikus! Tartsd meg ezt az energiát!",
    },
    "todo": {
        "neutral":   "Nézzük végig a teendőket.",
        "friendly":  "Menjünk végig a teendőkön együtt",
        "calm":      "Rendezzük sorba a feladatokat, haladunk lassan.",
        "energetic": "Pipáld ki őket egyenként - menni fog!",
    },
    "journal": {
        "neutral":   "A napló jó hely az érzéseknek.",
        "friendly":  "Írd le, amit érzel - én figyelek.",
        "calm":      "Kezdj el írni, ami eszedbe jut.",
        "energetic": "A gondolataid megérdemlik, hogy leírjuk őket!",
    },
    "default": {
        "neutral":   "Köszönöm az üzeneted. Miben segíthetek?",
        "friendly":  "Köszönöm, hogy megosztottad velem.",
        "calm":      "Értem. Kérdezz bátran, itt vagyok.",
        "energetic": "Köszönöm! Miben segíthetek még?",
    },
}

def detect_category(message: str) -> str:
    msg = message.lower()
    for category, words in KEYWORDS.items():
        if any(w in msg for w in words):
            return category
    return "default"

def get_response(message: str, tone: str = "friendly", style: str = "supportive") -> str:
    """
    Bemeneti üzenet alapján választ generál.
    :param message: A felhasználó üzenete.
    :param tone: A válasz hangvétele (neutral / friendly / calm / energetic).
    :param style: A válasz stílusa (jelenleg nem használt, jövőbeli bővítéshez).
    :return: Szöveges válasz.
    """
    category = detect_category(message)
    tone_key = tone if tone in ("neutral", "friendly", "calm", "energetic") else "friendly"
    return RESPONSES[category][tone_key]