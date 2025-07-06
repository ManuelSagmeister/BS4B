# 🕹️ SS_25_EA_Project — Multiplayer Game

A full-stack real-time multiplayer game project — built with:

- ⚛️ **React + Next.js** frontend
- 🕸️ **Spring Boot WebSocket** backend
- 🗄️ **Database microservice** 

Supports **real-time player input streaming** and **3D multiplayer gameplay** rendered in the browser.

---

## 🗂️ Project Structure

```txt
repo-root/
├── BS4BSpringBackend/      # Spring Boot backend (game logic + WebSocket server)
├── DataBaseService/        # Database microservice (Java/Spring)
├── react-frontend/         # Next.js React frontend (3D client)
└── README.md               # Project overview (this file)
```

---

## 🚀 Components

### 🎮 `react-frontend/` — Game Client

- **Stack:** Next.js, React, TypeScript, Three.js, @react-three/fiber, @react-three/rapier, WebSockets
- **Responsibilities:**
    - Renders the 3D world and player avatars.
    - Handles player input (keyboard + mouse).
    - Sends **player input** to backend via WebSocket (not raw position sync!).
    - Receives game state updates (player states, zombies, bullets, gas cloud, etc.).
- **Main Files:**
    - `src/app/page.tsx` → main app entry.
    - `src/app/components/` → 3D models & world components.
    - `src/hooks/` → custom game logic hooks (input, camera follow, mouse tracking).
- **Run Dev Mode:**

```bash
cd react-frontend
npm install
npm run dev
```

Access at: [http://localhost:3000](http://localhost:3000)

---

### 🧠 `BS4BSpringBackend/` — Game Server

- **Stack:** Java, Spring Boot, WebSocket, Jackson
- **Responsibilities:**
    - Maintains authoritative **game state**.
    - Receives **player inputs** (movement, shooting) and applies game logic.
    - Runs game loop (scheduled at fixed intervals).
    - Broadcasts full **GameRoomDTO** to clients (player states, bullets, zombies, gas cloud).
    - Manages player sessions (join/leave/respawn).
- **Main Files:**
    - `GameEngineService.java` → game loop logic.
    - `GameWebSocketHandler.java` → WebSocket entry point.
    - `model/` → player, bullet, zombie, gas cloud definitions.
- **Run:**

```bash
cd BS4BSpringBackend
./gradlew bootRun
```

---

### 🗄️ `DataBaseService/` — Database Microservice (Optional)

- **Stack:** Java, Spring Boot
- **Responsibilities:**
    - Provides persistent storage for player accounts, scores, etc.
    - Communicates with main backend via REST or events.
- **Run:**

```bash
cd DataBaseService
./gradlew bootRun
```

---

## 🕸️ How It Works

- Players connect via **WebSocket**.
- Frontend sends **input events** (move direction, shoot direction, shoot flag).
- Backend processes input, applies game rules, simulates world.
- Backend broadcasts full **GameRoomDTO** every frame:
    - Player states (health, ammo, position, rotation, isShooting)
    - Active bullets
    - Zombies with AI states
    - Gas cloud current size
- Frontend renders world based on received state.

---

## 🛠️ Getting Started

### 1️⃣ Start Backend

```bash
cd BS4BSpringBackend
./gradlew bootRun
```

### 2️⃣ (Optional) Start Database Service

```bash
cd DataBaseService
./gradlew bootRun
```

### 3️⃣ Start Frontend

```bash
cd react-frontend
npm install
npm run dev
```

Then open: [http://localhost:3000](http://localhost:3000)

---

## 🛠️ Development Notes

- **Frontend:** `react-frontend/src/app/` → React, Three.js, hooks
- **Backend:** `BS4BSpringBackend/src/` → Game loop, WebSocket, player model
- **Database:** `DataBaseService/src/` → Persistent player data

---

## 🧰 Technologies Used

- **Frontend:** Next.js, React, TypeScript, Three.js, @react-three/fiber
- **Backend:** Java, Spring Boot, WebSocket, Jackson
- **Database:** Java, Spring Boot, Postgresql

---

## ⚖️ License

This project is for educational purposes.

---

## ✍️ Authors

- Clemens Pohl
- Manuel Sagmeister
- Lucas Hartmann
- Emanuel Stachnieß