import random
from datetime import datetime

KEYWORDS = {
    "stress":  ["stressz", "fáradt", "túlterhelt", "nehéz", "rossz", "sír", "ideges", "szorongás"],
    "happy":   ["boldog", "jó", "szuper", "klassz", "örülök", "remek", "sikerült", "büszke"],
    "todo":    ["teendő", "feladat", "tennivaló", "lista", "elvégzett", "tervek", "meg kell"],
    "journal": ["napló", "leírnám", "érzés", "gondolat", "ma történt", "azon gondolkozom"],
    "negation": ["nem", "se", "sem", "soha", "sehogy"],
}

REDIRECTS = ["hagyjuk", "mindegy", "inkább", "valami mást", "nem akarok erről"]

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
    "follow_up_stressz": [
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

# Híd-mondatok, amik a user szavaira reagálnak
BRIDGES = {
    "stress": "Hallom, hogy most nehéz neked és {word} érzed magad. Ez érthető.",
    "happy": "De jó hallani, hogy {word} dolgok történnek veled!",
    "todo": "A feladatok és a {word} sokszor nyomasztóak tudnak lenni.",
    "journal": "Az, hogy leírod, hogy {word}, sokat segíthet a tisztánlátásban.",
    "default": "Értem, köszönöm, hogy megosztottad velem, hogy {word}."
}

# --- Conversational Arcs (Feminist HCI: Agency & Support) ---
ARCS = {
    "stress": [
        # step 0 - opening
        {
            "neutral":   "Hallak. Mi okozza most a legtöbb stresszt?",
            "friendly":  "Látom, hogy nehéz. Mi terhel most a legjobban?",
            "calm":      "Vegyél egy mély levegőt. Meséled, mi nyom?",
            "energetic": "Rendben, nézzük meg! Mi a legfőbb gond most?",
        },
        # step 1 - follow-up
        {
            "neutral":   "Mióta érzed így magad?",
            "friendly":  "Ez valóban sok. Mióta tart ez?",
            "calm":      "Értem. Mióta van ez így?",
            "energetic": "Oké. Mióta küzdesz ezzel?",
        },
        # step 2 - offer agency
        {
            "neutral":   "Mit tennél most magadért, ha tehetnél valamit?",
            "friendly":  "Mi az egy dolog, ami most jót tenne neked?",
            "calm":      "Ha most egy kis szünetet tarthatnál, mit csinálnál?",
            "energetic": "Mi az egy lépés, amit most meg tudsz tenni?",
        },
        # step 3 - close the arc
        {
            "neutral":   "Köszönöm, hogy elmondtad. Ha bármikor beszélni szeretnél, itt vagyok.",
            "friendly":  "Köszönöm, hogy megosztottad velem. Bármikor visszatérhetsz.",
            "calm":      "Köszönöm. Tartsd meg ezt a gondolatot magadnak.",
            "energetic": "Jól tetted, hogy elmondtad! Bármikor itt vagyok.",
        }
    ],
    "happy": [
        {
            "neutral":   "Örülök. Mi hozta ezt?",
            "friendly":  "De jó hallani! Mi történt?",
            "calm":      "Ez szép. Mesélj.",
            "energetic": "Ez remek! Mi váltotta ki?",
        },
        {
            "neutral":   "Hogyan tartod meg ezt az érzést?",
            "friendly":  "Szeretnéd megőrizni ezt? Mit tennél?",
            "calm":      "Élvezd. Mi segít ebben megmaradni?",
            "energetic": "Mit csinálsz, hogy így érezd magad? Csináld többet!",
        },
    ],
    "todo": [
        {
            "neutral":   "Mi a legfontosabb most a listádon?",
            "friendly":  "Melyik feladattal kezdjük?",
            "calm":      "Egy feladat egyszerre. Mi jön először?",
            "energetic": "Induljunk! Mi a legsürgősebb?",
        },
        {
            "neutral":   "Mennyi időd van rá?",
            "friendly":  "Oké! És mennyi idő áll rendelkezésre?",
            "calm":      "Lassan haladunk. Mennyi időd van?",
            "energetic": "Jó! Mennyi idő alatt kell megcsinálni?",
        },
        {
            "neutral":   "Szükséged van emlékeztetőre, vagy elég a napló?",
            "friendly":  "Felírod a naplóba, vagy marad itt?",
            "calm":      "Elég, ha most rögzíted. Naplóba kerüljön?",
            "energetic": "Tegyük bele a teendőlistádba! Naplóba is?",
        },
    ],
    "journal": [
        {
            "neutral":   "Mi az, amit ma le szeretnél írni?",
            "friendly":  "Miben gondolkozol ma?",
            "calm":      "Mi jut eszedbe most?",
            "energetic": "Gyerünk! Mi volt a legfontosabb ma?",
        },
        {
            "neutral":   "Hogyan érezted magad közben?",
            "friendly":  "Hogy érzed magad most, hogy kimondtad?",
            "calm":      "Megkönnyebbültél, hogy leírtad?",
            "energetic": "Jó! Hogy érzed magad most?",
        },
    ],
}

def detect_category(message: str) -> str:
    msg = message.lower()
    has_negation = any(neg in msg for neg in KEYWORDS["negation"])
    for category, words in KEYWORDS.items():
        if category == "negation": continue
        if any(w in msg for w in words):
            if category == "happy" and has_negation: return "stress"
            return category
    return "default"

# Keresünk egy szót a user üzenetéből, amit visszaismételhetünk
def get_echo_word(message: str, category: str) -> str:
    words = message.lower().split()
    # Megpróbálunk olyat keresni, ami a kulcsszavaink között van
    if category in KEYWORDS:
        for w in words:
            if w in KEYWORDS[category]:
                return w
    # Ha nincs találat, csak az utolsó értelmes szót adjuk vissza
    return words[-1] if words else "ez"

def get_greeting() -> str:
    hour = datetime.now().hour
    if hour < 12: prefix = "Jó reggelt"
    elif hour < 18: prefix = "Jó napot"
    else: prefix = "Jó estét"
    return f"{prefix}! Miben segíthetek ma?"

def get_response(message: str, tone: str, history: list, state: str):
    tone_key = tone if tone in ["neutral", "friendly", "calm", "energetic"] else "friendly"

    # 1. Ha ez a legelső üzenet (history még üres a main.py-ban)
    if not history:
        return get_greeting(), "idle"

    # 2. Escape Hatch
    if any(r in message.lower() for r in REDIRECTS):
        return "Természetesen, hagyjuk ezt a témát. Miről beszélnél szívesebben?", "idle"

    category = detect_category(message)
    echo = get_echo_word(message, category)

    # 3. Folyamatban lévő beszélgetés (Arc) folytatása
    if state.startswith("arc_"):
        parts = state.split("_")
        arc_cat, arc_step = parts[1], int(parts[2])
        next_step = arc_step + 1

        if arc_cat in ARCS and next_step < len(ARCS[arc_cat]):
            # A híd + a következő kérdés
            bridge = BRIDGES.get(category, BRIDGES["default"]).format(word=echo)
            question = ARCS[arc_cat][next_step][tone_key]
            return f"{bridge} {question}", f"arc_{arc_cat}_{next_step}"
        else:
            return "Köszönöm, hogy elmondtad. Ha bármi más van benned, itt vagyok.", "idle"

    # 4. Új téma indítása
    if category in ARCS:
        return ARCS[category][0][tone_key], f"arc_{category}_0"

    # 5. Alapértelmezett válasz (ha csak beszélgetne, de nincs téma)
    responses = ["Értem. Mesélj még erről!", "Hallgatlak, miben segíthetek még?", "Köszönöm, hogy megosztottad velem."]
    return random.choice(responses), "idle"