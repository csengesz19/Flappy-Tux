# Flappy-Tux
A simple Java Swing Flappy game: dodge pipes, score points, save highscores.

---

## Features

- Classic flappy gameplay: gravity + flap impulse + pipe gaps
- Multiple screens:
  - Main menu
  - Game screen
  - Game over screen
  - High scores screen
  - Settings screen
- **Top 10 high score** persistence (saved in the user’s home directory)
- **Settings persistence** (music on/off)
- Audio:
  - Background music loop
  - Sound effects for flap and death

---

## Controls

- **SPACE**: flap (jump)
- **Mouse click**: flap
- **ESC**: return to menu (while playing)

---

## Requirements

- **Java 8+** (recommended: Java 17+)
- No external dependencies (only standard Java libraries)

---

## How to Run

### 1) Compile
From the project folder (where the `.java` files are):

```bash
javac *.java
```

Assets (Required Files)

Put these files in the same folder where you run java TuxFlappy:

tux.png — Tux sprite used by the player

bg.wav — background music

flap.wav — flap sound effect

die.wav — death sound effect

Notes:

tux.png is loaded from the working directory (filesystem), so it must be present next to the compiled classes (or wherever you run the command).

Audio is loaded via the project’s audio helper classes; placing WAV files next to the app is the simplest setup.

Save Files (Persistence)

The game stores data in the user’s home directory:

High scores (Top 10): ~/tuxflappy_scores.txt

Settings: ~/tuxflappy_settings.properties


.
├── README.md
├── Audio.java
├── GameOverPanel.java
├── GamePanel.java
├── HighScorePanel.java
├── HighScoreStore.java
├── MainFrame.java
├── MenuPanel.java
├── PipePair.java
├── SettingsPanel.java
├── SettingsStore.java
├── Sound.java
├── Tux.java
├── TuxFlappy.java
├── tux.png
├── bg.wav
├── flap.wav
└── die.wav


Code Overview (File Map)

TuxFlappy.java — Application entry point (main). Starts the UI (creates the main window).

MainFrame.java — Main JFrame + screen navigation (CardLayout). Switches between Menu/Game/GameOver/HighScores/Settings and coordinates transitions.

MenuPanel.java — Main menu UI (start game, high scores, settings, exit).

GamePanel.java — Core gameplay: update loop/timer, input handling, scoring, collision checks, pipe management, and triggering game over.

Tux.java — Player logic + rendering: physics (gravity/flap), position/velocity, hitbox/bounds, draws tux.png.

PipePair.java — Obstacle pair: pipe geometry, movement/reset when off-screen, collision checks, “passed” logic for scoring.

GameOverPanel.java — Game over UI: shows final score and provides actions like restart/back to menu.

HighScorePanel.java — High score UI: displays the stored Top 10 list.

HighScoreStore.java — High score persistence (~/tuxflappy_scores.txt): load/save, keep sorted, trim to Top 10.

SettingsPanel.java — Settings UI: toggles options (music on/off) and saves changes.

SettingsStore.java — Settings persistence (~/tuxflappy_settings.properties) using a Properties-style file.

Audio.java — Audio controller: background music loop + playing sound effects; handles pausing/stopping on game over.

Sound.java — Sound definitions / mapping to WAV file names (e.g., background music, flap, die).
