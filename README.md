# 🗺️ Routing Engine — Dijkstra’s Algorithm (Maastricht Map)

> A high-performance routing engine implemented in **Java**, using **Dijkstra’s algorithm** to find the shortest path between locations in **Maastricht**.  
> The engine reads a JSON map of the city’s roads and dynamically computes optimal routes.

---

## 🌍 Overview

This project demonstrates how **graph-based routing algorithms** can be applied to real-world map data.  
It loads a JSON file representing the **road network of Maastricht**, where each node corresponds to an intersection and each edge corresponds to a road segment with a given distance.

Using **Dijkstra’s Algorithm**, the engine computes the most efficient path between two points.

---

## ⚙️ Core Features

✅ **JSON-based map input** — parses a graph of nodes and edges from `maastricht_graph.json`  
✅ **Dijkstra’s shortest path algorithm** — efficient, reliable, and optimized for performance  
✅ **Object-oriented design** — modular classes for easy extension (Graph, Node, Edge, Algorithm, etc.)  
✅ **JavaFX front-end** — optional GUI to visualize routes and interact with the graph  
✅ **Extensible architecture** — ready for A*, bidirectional search, or heuristic-based routing

---

## 🧠 Architecture

```
mapper1-2-routing-eugen/
│
├── build.gradle                    # Gradle build configuration
├── gradle.properties
├── settings.gradle
│
├── src/main/java/com/example/routingengine/
│   ├── DijkstraAlgorithm.java       # Core algorithm logic (priority queue, distance updates)
│   ├── Graph.java                   # Graph structure: manages nodes and edges
│   ├── Node.java                    # Represents an intersection with coordinates and neighbors
│   ├── Edge.java                    # Represents a road segment with distance and direction
│   ├── MapApplication.java          # JavaFX launcher (main entry point)
│   ├── HelloController.java         # JavaFX controller (UI bindings)
│   └── module-info.java             # Java module configuration
│
└── src/main/resources/com/example/routingengine/
    ├── hello-view.fxml              # UI layout for visualization
    └── maastricht_graph.json        # Road network data (Maastricht city)
```

---

## 🔍 Algorithm Overview

**Dijkstra’s Algorithm** finds the shortest path between nodes in a weighted graph by progressively visiting nodes with the smallest tentative distance.  

### Pseudocode (simplified)

```
1. Initialize all nodes with distance = ∞
2. Set the start node distance = 0
3. While unvisited nodes exist:
   - Select node u with smallest distance
   - For each neighbor v of u:
       alt = distance[u] + weight(u, v)
       if alt < distance[v]:
           distance[v] = alt
           previous[v] = u
4. Return path by backtracking from destination
```

### Complexity
- **Time:** O((V + E) log V) using a priority queue  
- **Space:** O(V)

---

## 🚀 Usage

### 1. Compile and Run (CLI)

```bash
./gradlew build
./gradlew run
```

or, manually:

```bash
javac -d out $(find src -name "*.java")
java -cp out com.example.routingengine.MapApplication
```

### 2. JSON Input Format

Example from `maastricht_graph.json`:
```json
{
  "nodes": [
    {"id": "A", "lat": 50.85, "lon": 5.68},
    {"id": "B", "lat": 50.86, "lon": 5.67}
  ],
  "edges": [
    {"from": "A", "to": "B", "distance": 120}
  ]
}
```

---

## 🧭 Example Output

```
Start: Markt
Destination: Vrijthof

Shortest Path:
Markt -> Boschstraat -> Vrijthof

Total distance: 1.25 km
Computation time: 0.003s
```

> If running in GUI mode, the route is highlighted on the map.

---

## 🧩 Extending the Engine

You can easily extend this project to include:

| Feature | Description |
|----------|--------------|
| **A\*** Algorithm | Add heuristics (e.g., Euclidean distance) for faster routing |
| **Bidirectional Search** | Run Dijkstra from both source and target simultaneously |
| **Turn penalties** | Model traffic rules or turns |
| **Real-time traffic** | Adjust edge weights dynamically |
| **Visualization Layer** | Display roads and paths using JavaFX or OpenStreetMap tiles |

---

## 🧱 Dependencies

- **Java 17+**
- **Gradle 8+**
- (Optional) **JavaFX SDK** for UI visualization

To install JavaFX SDK:
```bash
sudo apt install openjfx
```

---

## 🧑‍💻 Author

**Eugeniu Gheorghiță**  
Bachelor Student — Computer Science, Maastricht University  
📍 Maastricht, Netherlands  
📧 [mreugen123@gmail.com](mailto:mreugen123@gmail.com)  
🔗 [LinkedIn](https://www.linkedin.com/in/eugen-gheorghita-378314253/)

---

## 📚 References

- *Edsger W. Dijkstra (1959)* — “A note on two problems in connexion with graphs.”  
- JavaFX documentation — for UI development  
- Maastricht open geodata sources  

---

## 🗂️ License

This project is open for educational and research use.  
Feel free to fork, modify, and improve it.

---

*Last updated: October 2025*
