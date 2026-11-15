# Data Structure Analyzer

An interactive learning tool that visualizes sorting and searching algorithms, core data structures, and advanced labs (linked lists, trees, graphs, benchmarks and complexity). Plain HTML, CSS and JavaScript. No backend, no build step, no dependencies.

## Run it
Open `index.html` in a modern browser. To serve it locally instead: `python3 -m http.server` and visit `http://localhost:8000`.
Deploys as static files (GitHub Pages, Netlify, Vercel).

## What is inside
| Area | Contents |
|---|---|
| Sorting | Bubble, Selection, Insertion, Merge, Quick, Heap, plus a side-by-side comparison. Step controls, pseudocode highlighting, plain-language explanations, optional quiz mode |
| Searching | Linear and Binary search (binary sorts the values first) |
| Data structures | Stack, Queue, Linked list |
| Quiz | 14 questions across three topics, with explanations and per-topic best scores |
| Advanced Labs | Linked List Lab (singly, doubly, circular), Tree Lab (BST, AVL, four traversals), Graph Lab (BFS, DFS, Dijkstra, Prim, Kruskal), Performance Analyzer (real timings, CSV export), Complexity Explorer, Algorithm Race |

## Files
- `index.html` : application shell (top bar, global search, sidebar, content areas)
- `styles.css`, `advanced.css` : component styles
- `theme.css` : the design system (tokens for the dark and light themes, layout, surfaces). Loaded last, so it is the one file to edit to restyle the app
- `script.js` : router, algorithms, visualizers, quiz
- `advanced.js` : Advanced Labs and Heap sort
- `shell.js` : sidebar icons, global search (press `/` or Ctrl+K), recent activity and the Home dashboard
- `screenshots/` : 1440 x 900 captures of every screen in both themes, plus contact sheets

## Design system
Dark graphite surfaces (`#10141B` background, `#1B2330` cards) with a single cobalt accent (`#4F83F1`); matching light theme (`#F5F7FA`, `#365FD6`). Inter for text, JetBrains Mono for numbers and code. Thin borders instead of shadows. Colors in the visualizers carry meaning: amber = comparing, red = swapping, green = sorted or found.

## Notes
- Benchmark times depend on your browser and hardware. The Complexity Explorer plots theoretical operation counts, not execution time.
- Home shows only real data: recent modules and quiz best scores are read from your browser's local storage.
- Keyboard: Space play/pause, arrow keys step, `/` focus search.

## Java Code tabs
Sorting (Bubble, Selection, Insertion, Merge, Quick, Heap), searching (Linear, Binary), Stack, Queue and Linked list each have **Visualize / Java Code / Explanation / Complexity Analysis** tabs, and the Linked List Lab, Tree Lab and Graph Lab have a Java panel under the visualizer.

| Module | Java implementations |
|---|---|
| Sorting / Searching | `BubbleSort`, `SelectionSort`, `InsertionSort`, `MergeSort`, `QuickSort`, `HeapSort`, `LinearSearch`, `BinarySearch` |
| Stack | `ArrayStack`, `LinkedStack` (choose with the selector in the code header) |
| Queue | `ArrayQueue` (shifting), `CircularQueue`, `LinkedQueue` |
| Tree Lab | `BinarySearchTree`, `AVLTree` (insert, delete, search, inorder, preorder, postorder, level order, and the four AVL rotations LL, RR, LR, RL) |
| Data structures page (Java reference) | `ArrayOperations` (declare, traverse, insert, delete, search, update), `CircularDeque`, `MinPriorityQueue` (binary min-heap), `BinaryTree` (general binary tree: insert, delete, search and the four traversals). These have no visualizer, so the panel is a reference: pick an operation chip to highlight its lines |
| Graph Lab | `AdjacencyListGraph` (addVertex, addEdge, BFS, DFS, Dijkstra, Prim, Kruskal) and `AdjacencyMatrixGraph` (addVertex, addEdge, removeEdge, hasEdge, neighbours) |
| Linked list / Linked List Lab | `SinglyLinkedList`, `DoublyLinkedList`, `CircularLinkedList` (insert at head, tail or index; delete at head, tail or index; search; reverse; traverse) |

- **Java Code**: syntax-highlighted, line-numbered source with *Copy code* and *Download .java*. The Java lines for what you just did are highlighted and the relevant variables (`i`, `j`, `temp`, `pivot`, `low`, `high`, `mid`, `top`, `front`, `head`, `tail`, `size`, ...) are shown beside the code. In stack, queue and list modules, *Show operation* chips highlight any method on demand.
- **How the sync works**: sorting and searching steps carry an event (`compare`, `swap`, `pivot`, `goRight`, ...); stack, queue and list button presses are classified as operations (`push`, `dequeue`, `insertHead`, `underflow`, ...). Every event maps to `// @marker` lines in `java/*.java`; `tools/build-java.py` turns those markers into `javacode.js` and removes them from the displayed code, so the displayed line numbers always match the real file. The Java is never executed in the browser; the Explanation tab lists the event-to-line table.
- **Variables**: sorting and searching variables come from the step data. Stack, queue and list variables (`top`, `size`, `head.value`, ...) come from the visualizer state; the circular queue `front` index is a model that advances on each successful dequeue. The demo holds at most 8 items, so array versions use capacity 8 and linked versions note that they have no limit.
- **Source of truth**: `java/*.java`. After editing one, run `python3 tools/build-java.py` to regenerate `javacode.js`.

### Verify the Java yourself
```bash
cd java
javac -d out *.java tests/*.java   # compiles all 24 classes plus the test harnesses
java -cp out Check                  # 309 sort cases x 6 algorithms and 500 searches against java.util.Arrays
java -cp out Check2                 # random operation sequences for the lists, stacks and queues against java.util models
java -cp out Check3                 # BST and AVL against TreeSet, rotation cases, traversal rebuild checks
java -cp out TreeTrace i3 i2 i1 d3  # prints preorder and height of a BST and an AVL tree for the given operations
java -cp out Check5                 # arrays, deque, min-heap and binary tree against java.util models
java -cp out Check4                 # 400 random graphs against Floyd-Warshall, brute-force MST and reachability
java -cp out GraphTrace 0 6 0 0-1-4 0-2-2 3-4-2   # runs every algorithm on a graph (directed 0/1, vertex count, start, edges)
java -cp out CircularQueue          # every class has a runnable main
```
Requires JDK 11 or newer (developed and tested on JDK 21).

### Not yet covered
Array, Deque, Priority Queue and the plain Binary Tree are Java reference only (no visualizer exists for them). Compare sorts, Performance Analyzer, Complexity Explorer and Algorithm Race have no single algorithm to show.

### How the Tree Lab sync works
Insert, delete, search and the four traversals are classified from the button pressed. For AVL trees the lab records which rotation case ran (`LL`, `RR`, `LR` or `RL`, through `window.dsaTreeRot`) and the matching `rebalance` and rotation lines are highlighted together; one delete can trigger more than one rotation. The lab and the Java classes were checked against each other on random operation sequences: they produce the same preorder and height.

### How the Graph Lab sync works
Each animation step of BFS, DFS, Dijkstra, Prim and Kruskal reports an event and the variables at that moment (`current`, `queue` or `stack`, `u`, `distance`, `edge`, `weight`, `total`, `edges skipped (cycle)`); the matching Java lines are highlighted. Adding a vertex or an edge highlights the same operation in whichever representation (adjacency list or matrix) is selected. Prim and Kruskal now reveal their tree one edge at a time, and each Kruskal step is an accepted edge (the number of rejected edges that would have closed a cycle is shown). The Java classes use the same tie-breaking rules as the lab (the first minimal vertex or edge wins; stable sort for Kruskal), and the two were checked against each other on random directed and undirected, weighted and unweighted graphs: visit orders, distances, paths, and MST edges and totals are identical.
