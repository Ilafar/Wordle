# Wordle Console Edition

A clean, zero-dependency Java console Wordle game built with custom data structures and SOLID architecture.

## Requirements

- Java Development Kit (JDK) 17 or newer (verified with JDK 21)

## How to Run

### Windows

```bat
run.bat
```

### Linux / macOS

```bash
chmod +x run.sh
./run.sh
```

## Gameplay & Commands

From the main menu:

- `1` — Play Wordle (guess the 5-letter word within 6 attempts)
- `2` — View Stats, Solved Words (BST), and Top-5 Leaderboard (Min-Heap)
- `0` — Exit

Tile feedback legend:

- `(+)` — Letter is in the correct position
- `(?)` — Letter is in the word, wrong position
- `(.)` — Letter is not in the word

In-round commands:

- `:hint` — Show the next progressive hint from the hint queue
- `:undo` — Undo your last guess
- `:quit` — Abandon the current round and reveal the word

## Project Structure

```text
├── run.bat                         # Windows compile & run script
├── run.sh                          # Linux/macOS compile & run script
├── DSA_Istifade_Hesabati.docx      # Azerbaijani report mapping syllabus topics to code
└── src/com/wordgame/
    ├── Main.java                   # Application entry point & dependency wiring
    ├── ds/
    │   ├── DynamicArray.java       # Resizable array with amortized O(1) append
    │   ├── LinkedStack.java        # Singly linked-list LIFO stack (:undo)
    │   ├── ArrayQueue.java         # Circular array FIFO queue (word rotation & :hint)
    │   ├── ChainingHashMap.java    # Separate-chaining hash map with rehashing (load 0.75)
    │   ├── BinarySearchTree.java   # Unbalanced BST (sorted solved words & height)
    │   └── MinHeap.java            # Array-backed min-heap (heapify & Top-K scores)
    ├── game/
    │   ├── WordGame.java           # Game abstraction interface
    │   └── WordleGame.java         # 5-letter Wordle rules & evaluation logic
    ├── model/
    │   ├── GameMode.java
    │   ├── MoveOutcome.java
    │   ├── ScoreEntry.java
    │   └── TileState.java
    ├── service/
    │   ├── WordBank.java           # Shuffled non-repeating word queue & O(1) dictionary
    │   └── ProgressTracker.java    # Stats, BST solved catalog, and Min-Heap leaderboard
    └── ui/
        └── ConsoleRunner.java      # Console I/O and game loop
```

## Data Structures Used

| Structure | Where Used | Purpose |
| --- | --- | --- |
| `DynamicArray<T>` | `WordleGame`, `WordBank`, `MinHeap` | Guess history, word pool shuffling, heap backing storage |
| `LinkedStack<T>` | `WordleGame` | LIFO undo stack for `:undo` |
| `ArrayQueue<T>` | `WordleGame`, `WordBank` | FIFO `:hint` queue and non-repeating word rotation |
| `ChainingHashMap<K, V>` | `WordleGame`, `WordBank`, `ProgressTracker`, `Main` | Letter frequency counting, $O(1)$ dictionary, stats, mode factories |
| `BinarySearchTree<T>` | `ProgressTracker` | Alphabetical solved-words storage (`inOrder`) and tree height |
| `MinHeap<T>` | `ProgressTracker` | Top-5 leaderboard ranked by fewest attempts and fastest time |

