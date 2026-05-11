# SelfCareApp – Wellbeing Companion

An Android self-care application designed with feminist HCI principles, offering an AI chat companion, a to-do list, and a journaling feature in one privacy-first package.

> BSc thesis project – University of Pécs, Faculty of Informatics  
> Specialization: Software Development

---

## Features

- **Onboarding** – Personalized greeting using the user's name, stored locally
- **To-Do List** – Add, edit, complete, delete, and drag-and-drop reorder tasks
- **Journal** – Create entries with mood tracking and text content; browse and edit in list view
- **AI Chat** – Empathetic chatbot powered by a Python/FastAPI backend with FSM-based dialogue arcs and a four-layer dynamic system prompt
- **Settings** – Choose tone (neutral / friendly / calm / energetic), style (supportive / direct), and visual theme; preferences persist across sessions

---

## Design System

Four themes with full Material 3 integration:

| Theme | Mode |
|---|---|
| Soothing Light | Light |
| Soothing Dark | Dark |
| Dopamine Bright | Light |
| Dopamine Dark | Dark |

Each theme has its own color palette, gradient background, typography, and button style, managed via custom `attrs.xml` attributes and `BaseActivity`.

---

## Architecture

### Android (Frontend)
- **Language:** Java
- **Pattern:** Activity-based with Repository pattern
- **Database:** Room (SQLite) with DAO interfaces
- **UI:** ConstraintLayout, RecyclerView, Material 3 components
- **Navigation:** Explicit Intents

Data layer hierarchy:
```
UI → Activity → Repository → DAO → Room Database → SQLite
```

### Backend
- **Language:** Python
- **Framework:** FastAPI
- **LLM:** Google Gemini API (`gemini-1.5-flash`)
- **Logic:** Keyword-based category detection with negation handling, FSM dialogue arcs, four-layer system prompt assembly

Endpoints:
```
POST /chat                   – Send message, receive response
DELETE /chat/{session_id}    – Reset conversation (right to be forgotten)
```

---

## Project Structure

```
SelfCareApp/
├── frontend/          # Android app (Java)
│   └── app/src/main/
│       ├── java/com/example/selfcareapp/
│       │   ├── data/          # Entities, DAOs, Repository, AppDatabase
│       │   ├── ui/            # Activities, Adapters
│       │   └── utils/         # SentimentEngine, UserPreferences
│       └── res/
│           ├── values/        # themes.xml, attrs.xml, colors_*.xml
│           ├── font/          # Font resources
│           └── drawable/      # Gradient backgrounds, chat bubbles
└── chat_backend/      # Python FastAPI backend
    ├── main.py        # API endpoints, session management
    └── chatbot.py     # FSM logic, keyword detection, prompt assembly
```

---

## Getting Started

### Android App

1. Clone the repository
2. Open the `frontend/` folder in Android Studio
3. Build and run on an emulator or physical device (API 26+)

> The app connects to the backend at `10.0.2.2` (Android emulator localhost). Update `BASE_URL` in `ChatConversationActivity.java` for a physical device or deployment.

### Python Backend

```bash
cd chat_backend
pip install -r requirements.txt
uvicorn main:app --reload
```

Requires a valid Google Gemini API key set as an environment variable:
```bash
export GEMINI_API_KEY=your_key_here
```

---

## Privacy

- Journal and chat data are stored locally on-device only
- Server-side chat sessions are held in memory and can be deleted at any time via the reset function
- No user data is sold or shared with third parties

---

## Theoretical Grounding

The app is informed by feminist HCI and Care Ethics frameworks:

- **Bardzell** – Agency, equity, and empowerment in design
- **Costanza-Chock** – Design Justice
- **Noddings / Gilligan** – Care Ethics
- **Wajcman** – Feminist technology theory

---

## Requirements

| Component | Requirement |
|---|---|
| Android | API 26+ (Android 8.0) |
| Android Studio | Hedgehog or newer |
| Python | 3.10+ |
| Gemini API | Valid API key |

---

## License

This project was developed as a BSc thesis and is intended for academic purposes.
