# Chatbot logika - keyword matching alapú válaszgenerátor
# Feminist HCI elvek: felhasználó hangvételi preferenciája érvényesül (agency, empowerment)
import random

KEYWORDS = {
    "stress":  ["stressz", "fáradt", "túlterhelt", "nehéz", "rossz", "sír", "ideges", "szorongás"],
    "happy":   ["boldog", "jó", "szuper", "klassz", "örülök", "remek", "sikerült", "büszke"],
    "todo":    ["feladat", "tennivaló", "lista", "elvégzett", "tervek", "meg kell"],
    "journal": ["napló", "leírnám", "érzés", "gondolat", "ma történt", "azon gondolkozom"],
    "negation": ["nem", "se", "sem", "soha", "sehogy"],
}

# Multiple responses per category+tone so it doesn't repeat
RESPONSES = {
    "stress": {
        "neutral": [
            "Megértem, hogy most nehéz. Miben tudok segíteni?",
            "Hallak. Mi terhelne most a legjobban?",
            "Ez valóban fárasztóan hangzik. Mesélsz kicsit többet?",
        ],
        "friendly": [
            "Látom, hogy most nehéz. Én itt vagyok veled.",
            "Ne aggódj, együtt nézzük meg. Mi a legsúlyosabb most?",
            "Ez sok, hallom. Mire van szükséged most leginkább?",
        ],
        "calm": [
            "Vegyél egy mély levegőt. Lassan haladunk.",
            "Ez is elmúlik. Mi az, ami most a legjobban nyom?",
            "Rendben van, ha most nehéz. Meséld el.",
        ],
        "energetic": [
            "Meg tudod csinálni! Egy lépés egyszerre!",
            "Nehéz, de te erősebb vagy ennél. Mit tegyünk most?",
            "Tudom, hogy megy. Mit oldunk meg először?",
        ],
    },
    "happy": {
        "neutral": [
            "Örülök, hogy jól érzed magad.",
            "Ez jó hallani. Mi hozta ezt?",
        ],
        "friendly": [
            "De jó hallani ezt!",
            "Ez nagyon örömteli! Mi történt?",
        ],
        "calm": [
            "Élvezd ezt a pillanatot.",
            "Ez szép. Tartsd meg ezt az érzést.",
        ],
        "energetic": [
            "Fantasztikus! Tartsd meg ezt az energiát!",
            "Ez remek! Mit csináltál, hogy így érezd magad?",
        ],
    },
    "todo": {
        "neutral": [
            "Menjünk végig a teendőkön.",
            "Mi a legfontosabb most a listádon?",
        ],
        "friendly": [
            "Nézzük meg együtt a teendőket!",
            "Melyik feladattal kezdjük?",
        ],
        "calm": [
            "Rendezzük sorba lassan.",
            "Egy feladat egyszerre. Mi jön először?",
        ],
        "energetic": [
            "Pipáljuk ki őket egyenként!",
            "Induljunk! Melyik a legsürgősebb?",
        ],
    },
    "journal": {
        "neutral": [
            "A napló jó hely az érzéseknek.",
            "Írj le mindent, ami eszedbe jut.",
        ],
        "friendly": [
            "Írd le, amit érzel, én figyelek.",
            "A naplódban nincsenek rossz gondolatok.",
        ],
        "calm": [
            "Kezdj el írni, ami eszedbe jut.",
            "Lassan, szabadon! Nincs helyes formája.",
        ],
        "energetic": [
            "A gondolataid megérdemlik, hogy leírjuk!",
            "Gyere, tegyük rendbe fejben! Kezdd el írni!",
        ],
    },
    "follow_up_stress": [
        "Köszönöm, hogy megosztottad. Hogyan érzed most magad?",
        "Hallak. Van valami, amit most tehetnél magadért?",
        "Ez sok volt. Szükséged van valamire?",
    ],
    "default": {
        "neutral": [
            "Köszönöm az üzeneted. Miben segíthetek?",
            "Értem. Mondj többet, ha szeretnél.",
        ],
        "friendly": [
            "Köszönöm, hogy megosztottad velem.",
            "Hallak! Miben segíthetek?",
        ],
        "calm": [
            "Értem. Kérdezz bátran, itt vagyok.",
            "Semmi sietség. Miben segíthetek?",
        ],
        "energetic": [
            "Miben segíthetek még?",
            "Köszönöm! Menjünk tovább! Mi a következő?",
        ],
    },
}

def detect_category(message: str) -> str:
    msg = message.lower()
    has_negation = any(neg in msg for neg in KEYWORDS["negation"])

    for category, words in KEYWORDS.items():
        if category == "negation":
            continue
        if any(w in msg for w in words):
            # "nem jó" should NOT trigger happy
            if category == "happy" and has_negation:
                return "stress"
            return category
    return "default"

def was_stressed_recently(history: list) -> bool:
    """Check if the user expressed stress in the last 2 turns."""
    recent = [h["text"] for h in history[-4:] if h["role"] == "user"]
    for msg in recent:
        if detect_category(msg) == "stress":
            return True
    return False

def get_last_bot_response(history: list) -> str:
    for h in reversed(history):
        if h["role"] == "bot":
            return h["text"]
    return ""

def get_response(message: str, tone: str = "friendly", style: str = "supportive", history: list = []) -> str:
    """
    Bemeneti üzenet alapján választ generál.
    :param message: A felhasználó üzenete.
    :param tone: A válasz hangvétele (neutral / friendly / calm / energetic).
    :param style: A válasz stílusa (jelenleg nem használt, jövőbeli bővítéshez).
    :return: Szöveges válasz.
    """
    category = detect_category(message)
    tone_key = tone if tone in ("neutral", "friendly", "calm", "energetic") else "friendly"

    # If this is a short follow-up after stress, give a check-in response
    is_short = len(message.strip().split()) <= 4
    if is_short and was_stressed_recently(history) and category == "default":
        return random.choice(RESPONSES["follow_up_stress"])

    options = RESPONSES.get(category, RESPONSES["default"])
    if isinstance(options, dict):
        options = options.get(tone_key, options.get("friendly", []))

    # Avoid repeating the last bot response
    last = get_last_bot_response(history)
    available = [r for r in options if r != last]
    if not available:
        available = options

    return random.choice(available)