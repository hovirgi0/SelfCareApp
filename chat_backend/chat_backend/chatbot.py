import random
from datetime import datetime

KEYWORDS = {
    "stress": [
        "stressz", "stresszes", "stresszelt", "stresszben", "stresszel", "stresszez", "stresszet", "stressznek", "stresszem",
        "fáradt", "fáradtság", "fáradtan", "elfáradt", "kifáradt", "fáradtnak", "fáradtabb", "fáradtsággal",
        "túlterhelt", "túlterhelve", "túlterhelés", "túlterhelten",
        "nehéz", "nehezen", "nehézség", "nehézségekkel", "nehezemre",
        "rossz", "rosszul", "rosszabb", "rosszabbul", "rosszat",
        "szorongás", "szorongok", "szorongásom", "szorongással", "szorong", "szorongott", "szorongva",
        "ideges", "idegesség", "idegeskedek", "idegesít", "idegesített", "idegeskedés", "idegesen",
        "szomorú", "szomorúság", "szomorúan", "szomorúbb", "szomorkodok",
        "sírok", "sír", "sírtam", "sírt", "sírva", "sírás",
        "depresszió", "depressziós", "depressziót", "depresszióban", "depresszióval", "depressziótól",
        "elveszett", "elveszve", "elvesztem", "eltévedtem", "tehetetlen", "tehetetlenség", "tehetetlennek",
        "nem bírom", "nem bírok", "nem tudom tovább"
    ],
    "happy": [
        "boldog", "boldogan", "boldogság", "boldogtalan", "örülök", "örül", "örültem", "örömmel", "öröm", "örömöm",
        "örömteli", "örvendek", "jól érzem", "jól vagyok", "jó napom", "nagyon jó", "remek", "szuper", "klassz",
        "fantasztikus", "csodás", "nagyszerű", "kiváló", "tökéletes", "motivált", "motiváltan", "motivációm",
        "energikus", "energiával", "lendületes", "lendülettel", "sikerült", "sikeresen", "siker", "sikeres",
        "sikerrel", "büszke", "büszkén", "büszkeség", "elégedett", "elégedetten", "elégedettség"
    ],
    "todo": [
        "feladat", "feladatom", "feladatok", "feladatokat", "feladatokkal", "tennivaló", "tennivalóm", "tennivalók",
        "lista", "listám", "listán", "elvégzett", "elvégzem", "elvégezni", "tervek", "terveim", "terveimmel",
        "tervezem", "határidő", "határidőm", "határidőre", "megcsinálni", "meg kell", "meg kellene", "befejezni",
        "befejeztem", "befejezetlen", "prioritás", "prioritásom"
    ],
    "journal": [
        "napló", "naplóm", "naplóba", "naplóban", "leírnám", "leírom", "leírni", "érzés", "érzésem", "érzéseimet",
        "érzéseimről", "gondolat", "gondolataim", "gondolataimat", "ma történt", "ma volt", "ma éreztem",
        "azon gondolkozom", "azon töprengek", "reflexió", "önreflexió"
    ],
    "vague": [
        "hmm", "nem tudom", "semmit", "minden", "fogalmam sincs", "csak úgy", "nem igazán", "talán",
        "nem is tudom", "valahogy", "nehéz mondani", "nem tudok válaszolni", "kicsit minden", "passz"
    ],
    "negation": [
        "nem", "se", "sem", "soha", "sehogy", "semmi", "semmit", "semmilyen", "nem igazán",
        "nem nagyon", "nem annyira", "nem érzem", "nem érzed", "nem vagyok"
    ]
}

REDIRECTS = ["hagyjuk", "mindegy", "inkább", "valami mást", "nem akarok erről"]

# Multiple responses per category+tone so it doesn't repeat
RESPONSES = {
    "vague": {
        "neutral": ["Rendben, nem kell tudni. Csak mesélj, ami eszedbe jut.", "Nincs rossz válasz."],
        "friendly": ["Ez is teljesen rendben van. Nem kell tudni.", "Mondd csak el, ami jön, bármi legyen is."],
        "calm": ["Nem kell szavak. Üljünk csak így egy kicsit.", "Rendben van, ha most nincs szó rá."],
        "energetic": ["Nem baj! Kezd azzal, ami először eszedbe jut.", "Semmi gond, mondd csak!"],
    },
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
    words = msg.split()

    # Negációs pozíciók keresése
    negation_positions = set()
    for i, w in enumerate(words):
        for neg in KEYWORDS["negation"]:
            if neg in w:
                negation_positions.add(i)

    for category, kw_list in KEYWORDS.items():
        if category == "negation": continue
        for kw in kw_list:
            if kw in msg:
                # Kulcsszó pozíciójának megkeresése
                kw_words = kw.split()
                kw_pos = -1
                for i in range(len(words) - len(kw_words) + 1):
                    if words[i:i+len(kw_words)] == kw_words:
                        kw_pos = i
                        break

                if kw_pos == -1: return category # Alrészlet találat

                # Negációs ablak (3 szó)
                negated = any(abs(neg_pos - kw_pos) <= 3 for neg_pos in negation_positions)

                if negated:
                    if category == "happy": return "stress"
                    elif category == "stress": return "happy"
                    else: return "default"
                return category
    return "default"

def get_last_bot_category(history: list) -> str:
    """Kitalálja az utolsó bot üzenet alapján, miről volt szó."""
    if not history: return "default"

    # Megkeressük az utolsó bot választ a history-ban
    last_bot_msg = ""
    for h in reversed(history):
        if h["role"] == "bot":
            last_bot_msg = h["content"].lower()
            break

    if not last_bot_msg: return "default"

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

# Keresünk egy szót a user üzenetéből, amit visszaismételhetünk
def get_echo_word(message: str, category: str) -> str:
    words = message.lower().split()
    # Megpróbálunk olyat keresni, ami a kulcsszavaink között van
    if category in KEYWORDS:
        for w in words:
            if w in KEYWORDS[category]:
                return w
    #  Ha nincs találat, csak az utolsó értelmes szót adjuk vissza
    return words[-1] if words else "ez"

def get_greeting() -> str:
    return "Szia! Miben segíthetek ma? Örülök, hogy itt vagy."

# --- Fő válaszgeneráló függvény ---
def get_response(message: str, tone: str, history: list, state: str):
    tone_key = tone if tone in ["neutral", "friendly", "calm", "energetic"] else "friendly"

    # 1. Ha ez a legelső üzenet (history még üres a main.py-ban)
    if not history:
        return get_greeting(), "idle"

    # 2. Escape Hatch
    if any(r in message.lower() for r in REDIRECTS):
        return "Természetesen, hagyjuk ezt a témát. Miről beszélnél szívesebben?", "idle"

    category = detect_category(message)

    # Rövid válaszok kezelése kontextus alapján
    if category == "default" and len(message.split()) <= 3:
        category = get_last_bot_category(history)

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

    # 4. Új téma indítása (ideértve a 'vague' kategóriát is)
    if category in ARCS:
        return ARCS[category][0][tone_key], f"arc_{category}_0"

    # 5. Fallback Alapértelmezett válasz (ha csak beszélgetne, de nincs téma)
    responses = ["Értem. Mesélj még erről!", "Hallgatlak, miben segíthetek még?", "Köszönöm, hogy megosztottad velem."]
    return random.choice(responses), "idle"

# =============================================================================
# SYSTEM PROMPT GENERÁTOR — Feminist HCI elvek alapján
# =============================================================================
# Ez a függvény adja meg Groq-nak a "szerepkártyát" minden üzenet előtt.
# A Routing Workflow magja: a kategória és az arc állapot alapján
# specializált instrukciót kap a modell.
#
# Feminist HCI referencia:
#   - Bardzell: agency, equity, empowerment elvei
#   - Costanza-Chock: Design Justice — a rendszer nem irányít, hanem lehetőséget teremt
#   - Noddings: Care Ethics — gondoskodás, nem megoldás
# =============================================================================

def get_system_prompt(tone: str, style: str, state: str, category: str, history: list) -> str:
    """
    Dinamikus system prompt generálás a Groq számára.

    Paraméterek:
      tone     — a felhasználó által Settings-ben választott hangnem
                 (neutral / friendly / calm / energetic)
      style    — válaszstílus (supportive / direct / reflective): MÉG NINCS BEÉPÍTVE
      state    — jelenlegi arc állapot (pl. "idle", "arc_stress_1")
      category — az üzenet detektált kategóriája (stress / happy / todo / journal / default)
      history  — az eddigi beszélgetés listája (nem használjuk itt, de átadjuk konzisztencia miatt)

    Visszaad: egy string system promptot, amit a Groq kap.
    """
    # Lekérjük a pontos időt
    current_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    # -------------------------------------------------------------------------
    # RÉTEG 1: ALAPKARAKTER
    # -------------------------------------------------------------------------
    # Meghatározza, ki ez a társ és milyen értékek vezérlik.
    # Fontos: nem orvos, nem terapeuta, nem asszisztens — ez a Care Ethics (Noddings) és az
    # adatminimalizáció (thesis II.2.2) elvének gyakorlati megvalósítása.
    # A "nem ítélkezel" és "autonómia" elvek Bardzell agency + equity elveiből jönnek.
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

    # -------------------------------------------------------------------------
    # RÉTEG 2: HANGNEM
    # -------------------------------------------------------------------------
    # A felhasználó Settings-ben választja — ez az autonómia megőrzésének
    # gyakorlati megvalósítása (thesis II.2.2: "minden személyre szabási lépés opcionális")
    tone_map = {
        "neutral":   "Stílusod visszafogott és tárgyilagos, de meleg. Sem túl formális, sem túl laza. A felhasználó önmenendzsmentre főként fog használni, például a teendőihez kér segítséget.",
        "friendly":  "Stílusod barátságos és közvetlen, mintha egy jó ismerőssel beszélnél. Természetes, nem erőltetett.",
        "calm":      "Stílusod lassú és nyugodt. Rövid mondatok. Nem sietős. Teret adsz a csendnek is. Főként érzelmi támogatáshoz hasznos.",
        "energetic": "Stílusod lendületes és bátorító, de nem tolakodó. Aktív igék, pozitív jövőkép.",
    }
    # Ha ismeretlen tone érkezik, friendly az alapértelmezett
    tone_text = tone_map.get(tone, tone_map["friendly"])

    # Itt adjuk át az időt, hogy az MI tájékozódhasson:
    state_instruction = f"Jelenlegi időpont: {current_time}. Feladat: ..."

    # -------------------------------------------------------------------------
    # RÉTEG 3: ÁLLAPOT (ARC) INSTRUKCIÓ
    # -------------------------------------------------------------------------
    # Az arc állapot mondja meg, éppen hol tart a beszélgetés.
    # Ez a Prompt Chaining workflow lényege: az előző lépés kimenete
    # befolyásolja a következő instrukciót.
    #
    # Minden arc_lépéshez más feladat tartozik — ez biztosítja, hogy
    # a modell ne ugorjon előre, hanem végigkövesse a strukturált párbeszédet.
    # Az UTOLSÓ lépés minden arc-ban lezáró üzenet — NEM végződik kérdéssel,
    # hogy a beszélgetés természetesen zárulhasson.

    if state == "idle":
        # Nincs aktív téma — nyitott, meghívó hangnem
        if not history:
            # Legelső üzenet: köszöntés
            state_instruction = (
                "Ez az első üzenet a beszélgetésben. "
                "Köszöntsd a felhasználót melegen és barátságosan, "
                "és kérdezd meg egyszerűen, miben segíthetsz ma."
            )
        else:
            # Folytatódik a csevegés, de nincs aktív arc
            # Ha a felhasználó rövid lezáró üzenetet küld (pl. "köszi", "ok", "rendben"),
            # NE kezdj új témát — reagálj melegen és röviden, majd várj.
            state_instruction = (
                "Nincs aktív téma. Reagálj természetesen arra, amit a felhasználó mondott. "
                "Ha új témát vet fel, kövesd és ne erőltesd a régi témát. "
                "Ha a felhasználó csak köszön vagy lezáró üzenetet küld (pl. 'köszi', 'ok', "
                "'rendben', 'szia'), reagálj melegen és RÖVIDEN — NE kezdj új témát és NE kérdezz."
            )

    elif state.startswith("arc_stress"):
        # Stressz arc — a legtöbb figyelmet igénylő terület
        # Care Ethics itt a leglényegesebb: nem megoldjuk, hanem együtt vagyunk a személlyel
        parts = state.split("_")
        step = int(parts[2]) if len(parts) > 2 else 0

        step_instructions = [
            # 0. lépés: elismerés + konkrét segítség, nem rögtön megoldás
            "Ismerd el az érzést empátiával. Adj egy rövid, praktikus első javaslatot, "
            "majd kérdezz rá nyitott kérdéssel, mi okozza konkrétan a nehézséget.",

            # 1. lépés: mélyítés, időbeli kontextus
            "A felhasználó már megosztott valamit. Mutasd, hogy figyeltél — utalj vissza rá. "
            "Kérdezd meg, mióta érzi így magát.",

            # 2. lépés: agency — a megoldást a felhasználó találja meg, nem te
            "Kínálj egy konkrét kis lépést amit most megtehet, majd kérdezd meg "
            "mit tenne magáért ha megtehetné — hadd ő válaszolja meg. (Feminist HCI: agency elve)",

            # 3. lépés: LEZÁRÁS — NEM végződik kérdéssel!
            # Ez az arc utolsó lépése: természetes, meleg befejezés.
            # A cél, hogy a felhasználó érezze: meghallgatták, és bármikor visszatérhet.
            "Zárd le a beszélgetést melegen és természetesen. Fejezd ki, hogy örülsz, "
            "hogy megosztotta veled. Mondd el, hogy bármikor visszatérhet. "
            "FONTOS: NE tegyél fel kérdést — ez a lezáró üzenet, itt véget ér az arc.",
        ]
        step_text = step_instructions[step] if step < len(step_instructions) else step_instructions[-1]
        state_instruction = f"Stressz/nehézség téma, {step + 1}. lépés. Feladatod: {step_text}"

    elif state.startswith("arc_happy"):
        parts = state.split("_")
        step = int(parts[2]) if len(parts) > 2 else 0

        step_instructions = [
            # 0. lépés: örömmel együtt lenni, kíváncsiság
            "Örülj a felhasználóval együtt — őszintén, de mértékkel. "
            "Kérdezz rá, mi hozta ezt az érzést.",

            # 1. lépés: LEZÁRÁS — NEM végződik kérdéssel!
            # Pozitív arc rövidebb: 2 lépés elég, nem kell túlnyújtani a jó érzést.
            "Zárd le pozitívan és természetesen. Fejezd ki, hogy jó volt ezt hallani. "
            "FONTOS: NE tegyél fel újabb kérdést — ez a lezáró üzenet.",
        ]
        step_text = step_instructions[step] if step < len(step_instructions) else step_instructions[-1]
        state_instruction = f"Pozitív érzés téma, {step + 1}. lépés. Feladatod: {step_text}"

    elif state.startswith("arc_todo"):
        parts = state.split("_")
        step = int(parts[2]) if len(parts) > 2 else 0

        step_instructions = [
            # 0. lépés: prioritás — felhasználó dönti el, nem a rendszer
            "Kérdezd meg, mi a legfontosabb teendő most. Ne sorolj fel lehetőségeket — "
            "kérdezz, és hagyd, hogy ő válaszoljon.",

            # 1. lépés: idő és kapacitás
            "Kérdezd, mennyi ideje van a feladatra. Segíts prioritást felállítani, "
            "de NE te döntsd el helyette.",

            # 2. lépés: LEZÁRÁS — NEM végződik kérdéssel!
            # Az app funkcióira mutatunk rá, majd természetesen lezárjuk.
            "Összefoglald röviden amit megbeszéltetek, és emlékeztesd, hogy a teendőlista "
            "és napló funkció elérhető az alkalmazásban. "
            "FONTOS: NE tegyél fel újabb kérdést — ez a lezáró üzenet.",
        ]
        step_text = step_instructions[step] if step < len(step_instructions) else step_instructions[-1]
        state_instruction = f"Teendők téma, {step + 1}. lépés. Feladatod: {step_text}"

    elif state.startswith("arc_journal"):
        parts = state.split("_")
        step = int(parts[2]) if len(parts) > 2 else 0

        step_instructions = [
            # 0. lépés: tér és biztonság teremtése
            "Kérdezd, mit szeretne ma leírni vagy kimondani. "
            "Légy ítéletmentes és nyitott — emlékeztesd rá, hogy nincs rossz válasz.",

            # 1. lépés: LEZÁRÁS — NEM végződik kérdéssel!
            # Az önreflexiót megerősítjük, majd természetesen lezárjuk.
            "Erősítsd meg, hogy az önreflexió értékes volt, és örülsz, hogy megosztotta. "
            "Emlékeztesd, hogy a napló funkció is elérhető az appban, ha le szeretné írni. "
            "FONTOS: NE tegyél fel újabb kérdést — ez a lezáró üzenet.",
        ]
        step_text = step_instructions[step] if step < len(step_instructions) else step_instructions[-1]
        state_instruction = f"Napló/önreflexió téma, {step + 1}. lépés. Feladatod: {step_text}"

    else:
        # Ismeretlen state — általános, empatikus fallback
        state_instruction = "Reagálj természetesen és empatikusan arra, amit a felhasználó mondott."

    # -------------------------------------------------------------------------
    # ÖSSZEFŰZÉS
    # -------------------------------------------------------------------------
    # A három réteg (karakter + hangnem + állapot) együtt alkotja a teljes promptot.
    # A sorrend fontos: az alapelvek mindig előre kerülnek, hogy a modell
    # ne felülírhassa őket a feladatspecifikus instrukció miatt.
    full_prompt = f"""{base}
HANGNEM: {tone_text}
 
JELENLEGI FELADAT: {state_instruction}
 
EMLÉKEZTETŐ: Az ALAPELVEKET mindig tartsd be, a feladattól és a hangemtől függetlenül.
"""
    return full_prompt