# CloneHero

**CloneHero** is a 2D physics-based puzzle game written entirely in **Java**. It leverages a customized version of **LiquidFun** (a 2D physics engine originally from Google) adapted by Professor Faella, based on his course *Game Design and Development*. The goal of the game is to complete challenging golf-like levels by shooting the ball into the hole while avoiding hazards like spikes, using physics interactions and player-controlled shots.

---

## 🕹️ Game Features

- **Plain Java Implementation** — No engines, no JavaFX, no LibGDX: just Java and Android framework (obv lol).
- **Tilemap-Based Rendering** — The game uses a custom tilemap renderer to handle graphics efficiently.
- **Physics-Driven Interactions** — Powered by LiquidFun for realistic 2D physics simulations.
- **Data-Driven Level Design** — Each level is loaded from an external `.txt` file, making it easy to create new maps.
- **Optimized Sprite Atlas** — All game sprites are packed in a single 256x256 image (`assets/fgame.png`) to improve performance by aligning with 2ⁿ texture-size best practices.
- **Puzzle Mechanics** — Levels feature interactive objects like spikes, gates, keys, pressure plates, and gold collectibles.

---

## 📚 Technical Foundation

### 🧱 Framework

CloneHero builds on **Badlogic’s base framework**, `com.badlogic.androidgames.framework`, originally introduced in the book *Beginning Android Games (2nd Edition)* by Mario Zechner and Robert Green. This lightweight framework handles the basic game loop, asset loading, input handling, and screen transitions.

### 🎨 Graphics

Rendering is handled by the custom `MyGraphics.java` module. It interprets tilemaps and draws elements using sprites from the compact texture atlas (`assets/fgame.png`).

---

## 🧩 Level Loading

Each level is defined in a simple `.txt` file (e.g., `level1.txt`, `level2.txt`, ...), and contains:

- A **grid of tile codes** (e.g., 0 = empty, 1 = solid, 2 = water, f = flag, g = gates used just as placeholders).
- A `#START_DATA#` section containing metadata such as the level name and shot limit.
- A `#START_ENTITIES#` section listing game entities and their positions (e.g., `spike`, `gold`, `key`, `mtile`, `pressure`).

Here’s an excerpt from a level file:
```
#START_DATA#
name=Hole 5
shots=3
#END_DATA#

#START_ENTITIES#
spike,35,15
gold,28,15
key,24,17,36,7,36,6,36,5,36,4,
#END_ENTITIES#
```

The level loader reads this format to dynamically instantiate tiles and entities during runtime.

---

## 🧪 Entity System

CloneHero uses an **Entity Factory** pattern for game object instantiation. Each entity type (such as spikes, gold, keys, pressure plates, and gates) is registered and created through the factory based on type and coordinates from the level files. For instance:

- `spike,x,y` creates a physics-enabled spike.
- `pressure,x,y,...gateCoords` creates a pressure plate linked to gate tiles (gateCoords).
- `key,x,y,...gateCoords` functions similarly, but with a one-time unlock mechanic.

This design makes it easy to add new mechanics without changing the core game loop.

---

## 🗺️ Current Levels

The game includes **5 handcrafted levels**, ranging from beginner to more advanced challenges:

| Level | Name   | Shots Allowed | Features                       |
|-------|--------|----------------|--------------------------------|
| 1     | Hole 1 | 5              | Introductory terrain, basic spike |
| 2     | Hole 2 | 3              | Gold collection and vertical platforming |
| 3     | Hole 3 | 5              | Spikes and pressure-triggered gates |
| 4     | Hole 4 | 3              | High hazard density with spike corridors |
| 5     | Hole 5 | 3              | Key-and-gate puzzle and gold pickup |

All levels can be expanded or replaced simply by editing the `.txt` files under the project.

---

## 🧑‍🎓 Acknowledgements

- **Professor Faella** — For modifying and adapting LiquidFun for Java students.
- **Mario Zechner & Robert Green** — For the foundational framework from *Beginning Android Games*.
- **Google** — For originally developing LiquidFun.
