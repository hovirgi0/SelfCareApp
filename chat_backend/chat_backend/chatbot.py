# --- Chatbot Conversation Logic ---
# Rule-based conversational support system for the wellbeing application.
#
# Main responsibilities:
# - Detect emotional and functional conversation categories
# - Generate structured conversational flows ("arcs")
# - Maintain lightweight contextual continuity
# - Support customizable interaction tone and style
#
# Design principles:
# - Transparency and explainability
# - Minimal data processing
# - User autonomy (agency)
# - Non-judgmental interaction
# - Short, supportive responses

import random
from datetime import datetime

# --- Keyword Dictionaries ---
# Categories are detected through lightweight keyword matching.
# Each category contains related Hungarian expressions and variations.

KEYWORDS = {

    # Stress, emotional difficulty and negative emotional states
    "stress": [
        "stressz", "stresszes", "stresszelt", "stresszben", "stresszel", "stresszez",
        "stresszet", "stressznek", "stresszem",

        "fáradt", "fáradtság", "fáradtan", "elfáradt", "kifáradt",
        "fáradtnak", "fáradtabb", "fáradtsággal",

        "túlterhelt", "túlterhelve", "túlterhelés", "túlterhelten",

        "nehéz", "nehezen", "nehézség", "nehézségekkel", "nehezemre",

        "rossz", "rosszul", "rosszabb", "rosszabbul", "rosszat",

        "szorongás", "szorongok", "szorongásom", "szorongással",
        "szorong", "szorongott", "szorongva",

        "ideges", "idegesség", "idegeskedek", "idegesít",
        "idegesített", "idegeskedés", "idegesen",

        "szomorú", "szomorúság", "szomorúan", "szomorúbb", "szomorkodok",

        "sírok", "sír", "sírtam", "sírt", "sírva", "sírás",

        "depresszió", "depressziós", "depressziót",
        "depresszióban", "depresszióval", "depressziótól",

        "elveszett", "elveszve", "elvesztem", "eltévedtem",
        "tehetetlen", "tehetetlenség", "tehetetlennek",

        "nem bírom", "nem bírok", "nem tudom tovább"
    ],

    # Positive emotional states
    "happy": [
        "boldog", "boldogan", "boldogság", "boldogtalan",

        "örülök", "örül", "örültem", "örömmel", "öröm", "örömöm",
        "örömteli", "örvendek",

        "jól érzem", "jól vagyok", "jó napom", "nagyon jó",

        "remek", "szuper", "klassz", "fantasztikus",
        "csodás", "nagyszerű", "kiváló", "tökéletes",

        "motivált", "motiváltan", "motivációm",

        "energikus", "energiával",
        "lendületes", "lendülettel",

        "sikerült", "sikeresen", "siker", "sikeres", "sikerrel",

        "büszke", "büszkén", "büszkeség",

        "elégedett", "elégedetten", "elégedettség"
    ],

    # Task and productivity related expressions
    "todo": [
        "feladat", "feladatom", "feladatok", "feladatokat", "feladatokkal",

        "tennivaló", "tennivalóm", "tennivalók",

        "lista", "listám", "listán",

        "elvégzett", "elvégzem", "elvégezni",

        "tervek", "terveim", "terveimmel", "tervezem",

        "határidő", "határidőm", "határidőre",

        "megcsinálni", "meg kell", "meg kellene",

        "befejezni", "befejeztem", "befejezetlen",

        "prioritás", "prioritásom"
    ],

    # Reflection and journaling related expressions
    "journal": [
        "napló", "naplóm", "naplóba", "naplóban",

        "leírnám", "leírom", "leírni",

        "érzés", "érzésem", "érzéseimet", "érzéseimről",

        "gondolat", "gondolataim", "gondolataimat",

        "ma történt", "ma volt", "ma éreztem",

        "azon gondolkozom", "azon töprengek",

        "reflexió", "önreflexió"
    ],

    # Unclear or uncertain input patterns
    "vague": [
        "hmm", "nem tudom", "semmit", "minden",

        "fogalmam sincs", "csak úgy",

        "nem igazán", "talán",

        "nem is tudom", "valahogy",

        "nehéz mondani",

        "nem tudok válaszolni",

        "kicsit minden",

        "passz"
    ],

    # Negation expressions used for lightweight polarity detection
    "negation": [
        "nem", "se", "sem", "soha", "sehogy",

        "semmi", "semmit", "semmilyen",

        "nem igazán", "nem nagyon", "nem annyira",

        "nem érzem", "nem érzed",

        "nem vagyok"
    ]
}

# Expressions used to intentionally redirect the conversation.
REDIRECTS = [
    "hagyjuk",
    "mindegy",
    "inkább",
    "valami mást",
    "nem akarok erről"
]

# --- Response Templates ---
# Predefined response variations grouped by:
# - detected conversation category
# - selected communication tone
#
# Purpose:
# - reduce repetitive chatbot outputs
# - provide lightweight personalization
# - maintain conversational consistency

RESPONSES = {
    # Vague or uncertain user input
    "vague": {
        "neutral": [
            "Semmi baj, nem kell mindent rögtön tudni. Mesélj csak arról, ami épp eszedbe jut.",
            "Itt nincsenek rossz válaszok, tényleg."
        ],
        "friendly": [
            "Ez teljesen rendben van, nem kell ráfeszülni. Nem kell most mindent tudnod.",
            "Csak mondd, ami jön, bármi legyen is az. Figyelek."
        ],
        "calm": [
            "Nem muszáj szavakba önteni mindent. Üljünk csak így egy kicsit, ne siessünk.",
            "Rendben van, ha most nem jönnek a szavak. Itt vagyok."
        ],
        "energetic": [
            "Semmi gond! Kezdjük azzal, ami legelőször beugrik, ne is gondolkodj rajta!",
            "Nem baj, vágjunk bele a közepébe! Mondd, ami jön!"
        ],
    },

    # Stress or emotional difficulty
    "stress": {
        "neutral": [
            "Értem, hogy most nehéz időszakod van. Miben tudnék leginkább segíteni?",
            "Figyelek. Mi az, ami most a leginkább nyomja a vállad?",
            "Ez tényleg kimerítőnek hangzik. Szeretnél mesélni róla kicsit bővebben?",
        ],
        "friendly": [
            "Látom, hogy most nagyon összecsaptak a hullámok. Ne feledd, itt vagyok veled.",
            "Ne aggódj, majd együtt kibogozzuk. Mi a legnehezebb most?",
            "Ez rengeteg egyszerre, teljesen átérzem. Mire lenne most leginkább szükséged?",
        ],
        "calm": [
            "Vegyél egy mély levegőt. Szépen lassan haladunk, nem kerget a tatár.",
            "Ez is el fog múlni. Mi az a konkrét dolog, ami most a legjobban bánt?",
            "Rendben van, ha most nehéz. Csak meséld el, ami kijön belőled.",
        ],
        "energetic": [
            "Meg tudod csinálni, ne kételkedj! Csak egy lépés egyszerre, semmi több.",
            "Kemény helyzet, de te még ennél is keményebb vagy. Mit oldjunk meg legelőször?",
            "Tudom, hogy megugrod! Vágjunk bele: mi az első pont a listán?",
        ],
    },

    # Positive emotional state
    "happy": {
        "neutral": [
            "Örülök, hogy jól érzed magad, jó ilyet hallani.",
            "Ez remek hír! Mi váltotta ki ezt a jókedvet?",
        ],
        "friendly": [
            "De jó ilyet hallani, komolyan feldobtad a napom!",
            "Ez szuperül hangzik! Mesélj, mi történt?",
        ],
        "calm": [
            "Próbáld meg jól kiélvezni ezt a pillanatot.",
            "Ez nagyon szép. Tartsd meg magadban ezt a békét.",
        ],
        "energetic": [
            "Fantasztikus! Tartsd meg ezt a lendületet, ne hagyd elveszni!",
            "Ez az! Ez a beszéd! Mit csináltál, hogy ilyen jól érzed magad?",
        ],
    },

    # Productivity and planning related interaction
    "todo": {
        "neutral": [
            "Nézzük át, mik a teendők.",
            "Mi a legfontosabb dolog most a listádon?",
        ],
        "friendly": [
            "Gyerünk, nézzük meg együtt, mit kell ma elintézni!",
            "Na, melyik feladatnak essünk neki először?",
        ],
        "calm": [
            "Rendezzük sorba a dolgokat, szépen nyugodtan.",
            "Egyszerre csak egy dologgal foglalkozzunk. Mi legyen az első?",
        ],
        "energetic": [
            "Daráljuk le őket szépen sorban! Pipáljuk ki mindet!",
            "Indulás! Melyik a legsürgősebb, amit le kell tudni?",
        ],
    },

    # Reflection and journaling interaction
    "journal": {
        "neutral": [
            "A napló a legjobb hely arra, hogy kiadd magadból az érzéseidet.",
            "Csak írj le mindent, ami épp átsuhan az agyadon.",
        ],
        "friendly": [
            "Írd csak le bátran, én itt vagyok és figyelek rád.",
            "A naplódban nincsenek tabuk vagy rossz gondolatok, engedd el magad.",
        ],
        "calm": [
            "Csak kezdj el írni, ne is figyelj a formára, csak a gondolataidra.",
            "Lassan, szabadon... hagyd, hogy a toll (vagy a billentyű) vezessen.",
        ],
        "energetic": [
            "A gondolataid fontosak, érdemes őket rögzíteni! Vágj bele!",
            "Gyerünk, tegyünk rendet a fejedben! Kezdj el írni, bármit!",
        ],
    },

    # Follow-up responses for stress related conversations
    "follow_up_stressz": [
        "Köszönöm, hogy megosztottad. Hogy érzed magad most, hogy kimondtad?",
        "Értem. Van bármi apróság, amit most megtehetnél magadért?",
        "Ez nem volt kevés. Van valami, amire most szükséged lenne?",
    ],

    # Generic fallback responses
    "default": {
        "neutral": [
            "Köszönöm az üzeneted. Miben lehetek a segítségedre?",
            "Értem. Mesélj még róla, ha van kedved.",
        ],
        "friendly": [
            "Köszi, hogy elmesélted! Miben tudok most segíteni?",
            "Figyelek! Van még valami, amiről beszélnél?",
        ],
        "calm": [
            "Értem. Ne siess sehova, itt vagyok, ha kérdeznél.",
            "Semmi rohanás. Miben segíthetek most?",
        ],
        "energetic": [
            "Miben tudok még segíteni? Ne fogd vissza magad!",
            "Köszi! Menjünk tovább! Mi legyen a következő téma?",
        ],
    },
}

# --- Bridge Sentences ---
# Transitional templates that acknowledge detected emotions or keywords.
#
# Purpose:
# - create conversational continuity
# - reflect user wording
# - support lightweight conversational mirroring

BRIDGES = {
    # Stress-related reflection
    "stress": "Hallom, hogy most nem könnyű neked, és hogy {word} érzed magad. Teljesen érthető a helyzet.",
    # Positive emotional reinforcement
    "happy": "De jó hallani, hogy ilyen {word} dolgok történnek veled!",
    # Productivity related acknowledgment
    "todo": "A tennivalók és a {word} körüli pörgés néha tényleg nyomasztó tud lenni.",
    # Reflection and journaling encouragement
    "journal": "Az, hogy leírod, miért érzed magad {word}, rengeteget segíthet, hogy tisztábban láss.",
    # Generic fallback bridge
    "default": "Értem, és köszi, hogy megosztottad velem ezt a {word} dolgot."
}

# --- Conversational Arcs ---
# Structured multi-step conversational flows.
#
# Each arc:
# - maintains topic continuity
# - avoids repetitive responses
# - gradually deepens interaction
# - supports user agency

ARCS = {
    "stress": [
        # Step 0 — opening
        {
            "neutral":   "Figyelek. Mi az, ami most a legtöbb stresszt okozza?",
            "friendly":  "Látom, hogy nehéz. Mi az, ami most a leginkább nyomja a lelked?",
            "calm":      "Vegyél egy mély levegőt. Meséld el kérlek, mi bánt most leginkább.",
            "energetic": "Rendben, nézzük meg közelebbről! Mi a legfőbb gond most?",
        },
        # Step 1 — follow-up
        {
            "neutral":   "Mióta érzed ezt így?",
            "friendly":  "Ez tényleg sok lehet. Mióta tart ez az állapot?",
            "calm":      "Értem. Mióta küzdesz ezzel az érzéssel?",
            "energetic": "Oké, vágom. Mióta megy ez így nálad?",
        },
        # Step 2 — support agency
        {
            "neutral":   "Mit tennél most legszívesebben magadért, ha bármit lehetne?",
            "friendly":  "Mi az az egyetlen apróság, ami most egy kicsit jobbá tenné a kedved?",
            "calm":      "Ha most tarthatnál egy kis szünetet a világtól, mit csinálnál legszívesebben?",
            "energetic": "Mi az az egyetlen lépés, amit most rögtön meg tudsz tenni?",
        },
        # Step 3 — closing
        {
            "neutral":   "Köszönöm, hogy elmondtad. Ha bármikor beszélni szeretnél, tudod, hol találsz.",
            "friendly":  "Köszi, hogy megosztottad ezt velem. Bármikor visszajöhetsz, ha beszélni akarsz.",
            "calm":      "Köszönöm a bizalmadat. Most csak pihentesd ezt a gondolatot egy kicsit.",
            "energetic": "Nagyon jól tetted, hogy kiadtad magadból! Bármikor itt vagyok, ha folytatnád.",
        }
    ],

    "happy": [
        {
            "neutral":   "Ennek örülök. Mi hozta ezt a változást?",
            "friendly":  "De szuper! Mesélj csak, pontosan mi történt?",
            "calm":      "Ez nagyon szép. Mesélj még róla egy kicsit.",
            "energetic": "Ez az! Ez remek hír! Mi váltotta ki belőled ezt a jókedvet?",
        },
        {
            "neutral":   "Hogyan tudnád ezt a jó érzést minél tovább megőrizni?",
            "friendly":  "Szeretnéd, ha ez az érzés kitartana? Szerinted mi segítene ebben?",
            "calm":      "Élvezd ki. Mi az, ami segít neked ebben a békés állapotban maradni?",
            "energetic": "Mit tudnál tenni, hogy holnap is így érezd magad? Csináld azt többet!",
        },
    ],

    "todo": [
        {
            "neutral":   "Mi a legfontosabb tétel most a listádon?",
            "friendly":  "Na, melyik feladattal indítsunk?",
            "calm":      "Egyszerre csak egy dolog. Mi legyen az első?",
            "energetic": "Induljunk! Mi az, ami abszolút nem várhat?",
        },
        {
            "neutral":   "Körülbelül mennyi időd van rá?",
            "friendly":  "Okés! És nagyjából mennyi időt tudsz ráfordítani?",
            "calm":      "Lassan haladunk, nem kell kapkodni. Mennyi időd van erre ma?",
            "energetic": "Vágom! Mennyi idő alatt akarod letudni?",
        },
    ],

    "journal": [
        {
            "neutral":   "Mi az, amit ma mindenképpen le szeretnél jegyezni?",
            "friendly":  "Milyen gondolatok járnak ma a fejedben?",
            "calm":      "Mi jut eszedbe így legelőször?",
            "energetic": "Csapjunk bele! Mi volt számodra a nap legfontosabb pillanata?",
        },
        {
            "neutral":   "Hogyan érezted magad, miközben ezen gondolkodtál?",
            "friendly":  "Hogy érzed magad most, hogy így leírtad/kimondtad?",
            "calm":      "Érzed a megkönnyebbülést, hogy papírra (vagy képernyőre) vetetted?",
            "energetic": "Szuper! Milyen érzés most, hogy túl vagy rajta?",
        },
    ],
}

# --- Category Detection ---

def detect_category(message: str) -> str:
    """
    Detects the dominant conversation category from user input.

    The function:
    - performs lightweight keyword matching
    - supports basic negation handling
    - returns a single high-level category

    Example:
        "nem vagyok boldog" -> stress
    """

    msg = message.lower()
    words = msg.split()

    negation_positions = set()

    # Detect negation positions in the sentence
    for i, w in enumerate(words):
        for neg in KEYWORDS["negation"]:
            if neg in w:
                negation_positions.add(i)

    # Search categories and keywords
    for category, kw_list in KEYWORDS.items():

        if category == "negation":
            continue

        for kw in kw_list:

            if kw in msg:

                kw_words = kw.split()
                kw_pos = -1

                # Locate keyword position
                for i in range(len(words) - len(kw_words) + 1):
                    if words[i:i + len(kw_words)] == kw_words:
                        kw_pos = i
                        break

                # Fallback if keyword cannot be positioned
                if kw_pos == -1:
                    return category

                # Lightweight negation handling
                negated = any(
                    abs(neg_pos - kw_pos) <= 3
                    for neg_pos in negation_positions
                )

                if negated:

                    if category == "happy":
                        return "stress"

                    elif category == "stress":
                        return "happy"

                    else:
                        return "default"

                return category

    return "default"

# --- Context Utilities ---

def get_last_bot_category(history: list) -> str:
    """
    Estimates the previous conversation topic based on
    the last assistant response.

    Used for short or ambiguous user replies.
    """

    if not history:
        return "default"

    last_bot_msg = ""

    for h in reversed(history):
        if h["role"] == "bot":
            last_bot_msg = h["content"].lower()
            break

    if not last_bot_msg:
        return "default"

    signals = {
        "stress": ["stressz", "terhel", "fáradt", "nehéz", "szorongás"],
        "happy": ["örülök", "jó hallani", "remek", "fantasztikus", "szép"],
        "todo": ["teendő", "feladat", "listá", "prioritás"],
        "journal": ["napló", "ír", "gondolat", "érzés"]
    }

    for cat, sigs in signals.items():
        if any(s in last_bot_msg for s in sigs):
            return cat

    return "default"


def get_echo_word(message: str, category: str) -> str:
    """
    Extracts a meaningful keyword from the user message.

    The returned word may later be reused in responses
    to create lightweight conversational mirroring.
    """

    words = message.lower().split()

    if category in KEYWORDS:
        for w in words:
            if w in KEYWORDS[category]:
                return w

    return words[-1] if words else "ez"


def get_greeting() -> str:
    """
    Returns the default opening greeting.
    """
    return "Szia! Miben segíthetek ma? Örülök, hogy itt vagy."


# --- Response Generator ---

def get_response(message: str, tone: str, history: list, state: str):

    tone_key = (
        tone
        if tone in ["neutral", "friendly", "calm", "energetic"]
        else "friendly"
    )

    # First message — return greeting and reset state
    if not history:
        return get_greeting(), "idle"

    # Escape hatch — allow intentional topic switching
    if any(r in message.lower() for r in REDIRECTS):
        return (
            "Természetesen, hagyjuk ezt a témát. "
            "Miről beszélnél szívesebben?",
            "idle"
        )

    category = detect_category(message)

    # Short replies inherit previous context
    if category == "default" and len(message.split()) <= 3:
        category = get_last_bot_category(history)

    echo = get_echo_word(message, category)

    # Continue active conversational arc
    if state.startswith("arc_"):

        parts = state.split("_")
        arc_cat = parts[1]
        arc_step = int(parts[2])

        next_step = arc_step + 1

        if arc_cat in ARCS and next_step < len(ARCS[arc_cat]):

            bridge = BRIDGES.get(
                category,
                BRIDGES["default"]
            ).format(word=echo)

            question = ARCS[arc_cat][next_step][tone_key]

            return (
                f"{bridge} {question}",
                f"arc_{arc_cat}_{next_step}"
            )

        else:
            return (
                "Köszönöm, hogy elmondtad. "
                "Ha bármi más van benned, itt vagyok.",
                "idle"
            )

    # Start a new conversational arc
    if category in ARCS:
        return (
            ARCS[category][0][tone_key],
            f"arc_{category}_0"
        )

    # Generic fallback response
    responses = [
        "Értem. Mesélj még erről!",
        "Hallgatlak, miben segíthetek még?",
        "Köszönöm, hogy megosztottad velem."
    ]

    return random.choice(responses), "idle"

# --- Dynamic System Prompt Generator ---

def get_system_prompt(tone: str, style: str, state: str, category: str, history: list) -> str:
    """
    Build a dynamic system prompt for the LLM based on current conversation state.

    Args:
        tone     -- user-selected tone (neutral / friendly / calm / energetic)
        style    -- response style (supportive / direct / reflective)
        state    -- current arc state (e.g. "idle", "arc_stress_1")
        category -- detected message category (stress / happy / todo / journal / default)
        history  -- conversation history (passed for consistency, unused here)

    Returns:
        A system prompt string consumed by the LLM.
    """
    current_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    # --- Layer 1: Base character ---
    # Defines who this companion is and what values drive it.
    # Not a doctor, not a therapist, not an assistant — an equal partner.
    # Grounded in Care Ethics, feminist agency,
    # and data minimisation principles.
    base = """Te egy empatikus öngondoskodási társ vagy egy mobilalkalmazásban, amelynek
három fő funkciója van: AI csevegő társ, teendőlista és napló.
Magyarul kommunikálsz. Természetes, hétköznapi stílusban beszélsz.
 
ALAPELVEK — ezeket MINDIG tartsd be, minden körülmények között:
- Soha nem adsz orvosi vagy terápiás tanácsot. Ha valaki súlyos krízist jelez
  (pl. önkárosítás, reménytelenség), halkan és empatikusan javasold szakember keresését.
- Nem ítélkezel és nem értékelsz. A felhasználó döntései az övéi. (Feminist HCI: agency elve)
- KONKRÉT segítséget adsz, nem csak kérdéseket. Ha valaki segítséget kér, segíts —
  adj egy rövid, gyakorlati választ vagy javaslatot, MAJD tegyél fel egy nyitott kérdést.
  Például: "Ha a dolgozatod túl hosszú, kezdd azzal, hogy minden bekezdésből megtartod
  csak a leglényegesebb mondatot. Mit próbáltál eddig?"
- RÖVID válaszokat adsz: maximum 3-4 mondat. Soha nem áradozol.
- Nem vagy mindig pozitív — ha valaki szomorú, elfogadod az érzést, nem bagatellizálod.
  (Care Ethics: gondoskodás, nem gyors megoldás)
- Kerülöd a sztereotip "gondoskodó, kiszolgáló" szerepet. Egyenrangú partner vagy,
  nem asszisztens. (Feminist HCI: equity elve)
- Ha a felhasználó el akar térni a témától, hagyod. Nem ragaszkodsz az archoz.
- Ha a felhasználó a teendőlistájáról vagy naplójáról kérdez, emlékeztesd, hogy
  ezek elérhetők az alkalmazásban.
"""

    # --- Layer 2: Tone and style ---
    # User-chosen in Settings — preserving autonomy over how support is delivered.
    tone_map = {
        "neutral":   "Stílusod visszafogott és tárgyilagos, de meleg. Sem túl formális, sem túl laza. A felhasználó önmenendzsmentre főként fog használni, például a teendőihez kér segítséget.",
        "friendly":  "Stílusod barátságos és közvetlen, mintha egy jó ismerőssel beszélnél. Természetes, nem erőltetett.",
        "calm":      "Stílusod lassú és nyugodt. Rövid mondatok. Nem sietős. Teret adsz a csendnek is. Főként érzelmi támogatáshoz hasznos.",
        "energetic": "Stílusod lendületes és bátorító, de nem tolakodó. Aktív igék, pozitív jövőkép.",
    }
    tone_text = tone_map.get(tone, tone_map["friendly"])

    style_map = {
        "supportive": (
            "Stílusod támogató és empatikus."
            "majd óvatosan kínálsz egy-egy javaslatot. Soha nem nyomsz semmit rá."
        ),
        "direct": (
            "Stílusod közvetlen és praktikus. Gyorsan a lényegre térsz, "
            "nem kerülgeted a dolgot. Empatikus vagy, de nem nyújtod el a válaszokat."
        ),
    }
    style_text = style_map.get(style, style_map["supportive"])

    state_instruction = f"Jelenlegi időpont: {current_time}. Feladat: ..."

    # --- Layer 3: Arc state instruction ---
    # Determines the LLM's specific task for the current conversation step.
    # Each arc step has a distinct goal; the final step always closes without a question
    # so the conversation can end naturally rather than looping indefinitely.

    if state == "idle":
        if not history:
            # Opening message: warm greeting only.
            state_instruction = (
                "Ez az első üzenet a beszélgetésben. "
                "Köszöntsd a felhasználót melegen és barátságosan, "
                "és kérdezd meg egyszerűen, miben segíthetsz ma."
            )
        else:
            # Mid-conversation idle: follow the user's lead, don't force a topic.
            # Short closing messages (e.g. "köszi") get a brief warm reply — no new topic.
            state_instruction = (
                "Nincs aktív téma. Reagálj természetesen arra, amit a felhasználó mondott. "
                "Ha új témát vet fel, kövesd és ne erőltesd a régi témát. "
                "Ha a felhasználó csak köszön vagy lezáró üzenetet küld (pl. 'köszi', 'ok', "
                "'rendben', 'szia'), reagálj melegen és RÖVIDEN — NE kezdj új témát és NE kérdezz."
            )

    elif state.startswith("arc_stress"):
        # Stress arc: most care-intensive path.
        # Goal: be present with the person, not solve their problem for them (Care Ethics).
        parts = state.split("_")
        step = int(parts[2]) if len(parts) > 2 else 0

        step_instructions = [
            # Step 0: acknowledge + offer one concrete suggestion, then ask an open question.
            "Ismerd el az érzést empátiával. Adj egy rövid, praktikus első javaslatot, "
            "majd kérdezz rá nyitott kérdéssel, mi okozza konkrétan a nehézséget.",

            # Step 1: show you listened, ask about duration/context.
            "A felhasználó már megosztott valamit. Mutasd, hogy figyeltél — utalj vissza rá. "
            "Kérdezd meg, mióta érzi így magát.",

            # Step 2: surface the user's own solution — agency, not prescription (Feminist HCI).
            "Kínálj egy konkrét kis lépést amit most megtehet, majd kérdezd meg "
            "mit tenne magáért ha megtehetné — hadd ő válaszolja meg. (Feminist HCI: agency elve)",

            # Step 3 (closing): warm close, no question — arc ends here.
            "Zárd le a beszélgetést melegen és természetesen. Fejezd ki, hogy örülsz, "
            "hogy megosztotta veled. Mondd el, hogy bármikor visszatérhet. "
            "FONTOS: NE tegyél fel kérdést — ez a lezáró üzenet, itt véget ér az arc.",
        ]
        step_text = step_instructions[step] if step < len(step_instructions) else step_instructions[-1]
        state_instruction = f"Stressz/nehézség téma, {step + 1}. lépés. Feladatod: {step_text}"

    elif state.startswith("arc_happy"):
        # Happy arc: shorter by design — don't over-process positive feelings.
        parts = state.split("_")
        step = int(parts[2]) if len(parts) > 2 else 0

        step_instructions = [
            # Step 0: share in the joy genuinely, ask what brought it on.
            "Örülj a felhasználóval együtt — őszintén, de mértékkel. "
            "Kérdezz rá, mi hozta ezt az érzést.",

            # Step 1 (closing): affirm and close, no question.
            "Zárd le pozitívan és természetesen. Fejezd ki, hogy jó volt ezt hallani. "
            "FONTOS: NE tegyél fel újabb kérdést — ez a lezáró üzenet.",
        ]
        step_text = step_instructions[step] if step < len(step_instructions) else step_instructions[-1]
        state_instruction = f"Pozitív érzés téma, {step + 1}. lépés. Feladatod: {step_text}"

    elif state.startswith("arc_todo"):
        # Todo arc: user sets priorities, the system facilitates — never decides for them.
        parts = state.split("_")
        step = int(parts[2]) if len(parts) > 2 else 0

        step_instructions = [
            # Step 0: ask what matters most — user's choice, not a pre-built list.
            "Kérdezd meg, mi a legfontosabb teendő most. Ne sorolj fel lehetőségeket — "
            "kérdezz, és hagyd, hogy ő válaszoljon.",

            # Step 1: ask about available time, help prioritise without deciding.
            "Kérdezd, mennyi ideje van a feladatra. Segíts prioritást felállítani, "
            "de NE te döntsd el helyette.",

            # Step 2 (closing): brief summary, point to in-app features, no question.
            "Összefoglald röviden amit megbeszéltetek, és emlékeztesd, hogy a teendőlista "
            "és napló funkció elérhető az alkalmazásban. "
            "FONTOS: NE tegyél fel újabb kérdést — ez a lezáró üzenet.",
        ]
        step_text = step_instructions[step] if step < len(step_instructions) else step_instructions[-1]
        state_instruction = f"Teendők téma, {step + 1}. lépés. Feladatod: {step_text}"

    elif state.startswith("arc_journal"):
        # Journal arc: create space for self-reflection without judgement.
        parts = state.split("_")
        step = int(parts[2]) if len(parts) > 2 else 0

        step_instructions = [
            # Step 0: open, non-judgemental invitation — no wrong answer.
            "Kérdezd, mit szeretne ma leírni vagy kimondani. "
            "Légy ítéletmentes és nyitott — emlékeztesd rá, hogy nincs rossz válasz.",

            # Step 1 (closing): affirm self-reflection, point to journal feature, no question.
            "Erősítsd meg, hogy az önreflexió értékes volt, és örülsz, hogy megosztotta. "
            "Emlékeztesd, hogy a napló funkció is elérhető az appban, ha le szeretné írni. "
            "FONTOS: NE tegyél fel újabb kérdést — ez a lezáró üzenet.",
        ]
        step_text = step_instructions[step] if step < len(step_instructions) else step_instructions[-1]
        state_instruction = f"Napló/önreflexió téma, {step + 1}. lépés. Feladatod: {step_text}"

    else:
        # Unknown state — safe empathetic fallback.
        state_instruction = "Reagálj természetesen és empatikusan arra, amit a felhasználó mondott."

    # --- Assemble final prompt ---
    # Order matters: base principles come first so the LLM cannot override them
    # with task-specific instructions below.
    full_prompt = f"""{base}
HANGNEM: {tone_text}

STÍLUS: {style_text}
 
JELENLEGI FELADAT: {state_instruction}
 
EMLÉKEZTETŐ: Az ALAPELVEKET mindig tartsd be, a feladattól és a hangemtől függetlenül.
"""
    return full_prompt