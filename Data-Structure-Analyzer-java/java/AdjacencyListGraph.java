import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

// Graph stored as adjacency lists. Vertices are numbered 0 .. vertexCount - 1; weights are non-negative integers.
public class AdjacencyListGraph {
    public static final int INF = Integer.MAX_VALUE;

    public static class Edge {
        public final int from;
        public final int to;
        public final int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() { return from + "-" + to + " (" + weight + ")"; }
    }

    public static class ShortestPaths {
        public final int[] distance; // INF when a vertex is unreachable
        public final int[] previous; // -1 when a vertex has no predecessor

        ShortestPaths(int[] distance, int[] previous) {
            this.distance = distance;
            this.previous = previous;
        }
    }

    private int vertexCount;
    private final boolean directed;
    private final List<List<Edge>> adjacency = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>(); // each edge once, in insertion order

    public AdjacencyListGraph(int vertexCount, boolean directed) {
        if (vertexCount < 0) {
            throw new IllegalArgumentException("vertexCount must not be negative");
        }
        this.directed = directed;
        for (int i = 0; i < vertexCount; i++) {
            adjacency.add(new ArrayList<>());
        }
        this.vertexCount = vertexCount;
    }

    public int addVertex() {
        adjacency.add(new ArrayList<>());                       // @addVertex
        return vertexCount++;                                   // @addVertex
    }

    public void addEdge(int from, int to, int weight) {
        checkVertex(from);
        checkVertex(to);
        if (weight < 0) {
            throw new IllegalArgumentException("Negative weights are not supported");
        }
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);                                        // @addEdge
        adjacency.get(from).add(edge);                          // @addEdge
        if (!directed) {
            adjacency.get(to).add(new Edge(to, from, weight));  // @addEdge
        }
    }

    public int vertexCount() { return vertexCount; }

    private void checkVertex(int v) {
        if (v < 0 || v >= vertexCount) {
            throw new IllegalArgumentException("No such vertex: " + v);
        }
    }

    // Breadth-first search: returns the vertices reachable from start, nearest first.
    public List<Integer> bfs(int start) {
        checkVertex(start);
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[vertexCount];
        Queue<Integer> queue = new ArrayDeque<>();
        visited[start] = true;                                  // @bfsStart
        queue.add(start);                                       // @bfsStart
        while (!queue.isEmpty()) {                              // @bfsVisit
            int current = queue.remove();                       // @bfsVisit
            order.add(current);                                 // @bfsVisit
            for (Edge edge : adjacency.get(current)) {          // @bfsExpand
                if (!visited[edge.to]) {                        // @bfsExpand
                    visited[edge.to] = true;                    // @bfsExpand
                    queue.add(edge.to);                         // @bfsExpand
                }
            }
        }
        return order;
    }

    // Depth-first search with an explicit stack. Neighbours are pushed in reverse so the first neighbour is explored first.
    public List<Integer> dfs(int start) {
        checkVertex(start);
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[vertexCount];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);                                      // @dfsStart
        while (!stack.isEmpty()) {
            int current = stack.pop();                          // @dfsVisit
            if (visited[current]) {                             // @dfsVisit
                continue;                                       // @dfsVisit
            }
            visited[current] = true;                            // @dfsVisit
            order.add(current);                                 // @dfsVisit
            List<Edge> neighbours = adjacency.get(current);
            for (int i = neighbours.size() - 1; i >= 0; i--) {  // @dfsExpand
                if (!visited[neighbours.get(i).to]) {           // @dfsExpand
                    stack.push(neighbours.get(i).to);           // @dfsExpand
                }
            }
        }
        return order;
    }

    // Dijkstra's algorithm, O(V^2) version (no priority queue). Weights must be non-negative.
    public ShortestPaths dijkstra(int start) {
        checkVertex(start);
        int[] distance = new int[vertexCount];
        int[] previous = new int[vertexCount];
        boolean[] settled = new boolean[vertexCount];
        Arrays.fill(distance, INF);                             // @dijkInit
        Arrays.fill(previous, -1);                              // @dijkInit
        distance[start] = 0;                                    // @dijkInit
        for (int round = 0; round < vertexCount; round++) {
            int u = -1;
            for (int v = 0; v < vertexCount; v++) {             // @dijkSettle
                if (!settled[v] && (u == -1 || distance[v] < distance[u])) {   // @dijkSettle
                    u = v;                                      // @dijkSettle
                }
            }
            if (u == -1 || distance[u] == INF) {                // @dijkSettle
                break; // everything left is unreachable
            }
            settled[u] = true;                                  // @dijkSettle
            for (Edge edge : adjacency.get(u)) {                // @dijkRelax
                if ((long) distance[u] + edge.weight < distance[edge.to]) {   // @dijkRelax
                    distance[edge.to] = distance[u] + edge.weight;            // @dijkRelax
                    previous[edge.to] = u;                      // @dijkRelax
                }
            }
        }
        return new ShortestPaths(distance, previous);
    }

    // Prim's algorithm on an undirected graph: repeatedly adds the cheapest edge that leaves the tree.
    public List<Edge> prim(int start) {
        if (directed) {
            throw new IllegalStateException("Prim's algorithm needs an undirected graph");
        }
        checkVertex(start);
        boolean[] inTree = new boolean[vertexCount];
        List<Edge> tree = new ArrayList<>();
        inTree[start] = true;                                   // @primInit
        int covered = 1;
        while (covered < vertexCount) {
            Edge best = null;
            for (Edge edge : edges) {                           // @primPick
                boolean crosses = inTree[edge.from] != inTree[edge.to];   // @primPick
                if (crosses && (best == null || edge.weight < best.weight)) {   // @primPick
                    best = edge;                                // @primPick
                }
            }
            if (best == null) {
                break; // disconnected: only the start's component is covered
            }
            inTree[best.from] = true;                           // @primAdd
            inTree[best.to] = true;                             // @primAdd
            tree.add(best);                                     // @primAdd
            covered++;
        }
        return tree;
    }

    // Kruskal's algorithm on an undirected graph: cheapest edges first, skipping any edge that would close a cycle.
    public List<Edge> kruskal() {
        if (directed) {
            throw new IllegalStateException("Kruskal's algorithm needs an undirected graph");
        }
        List<Edge> sorted = new ArrayList<>(edges);
        sorted.sort(Comparator.comparingInt(e -> e.weight));    // @kruskalSort
        int[] parent = new int[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            parent[i] = i;
        }
        List<Edge> tree = new ArrayList<>();
        for (Edge edge : sorted) {                              // @kruskalFind
            int rootA = find(parent, edge.from);                // @kruskalFind
            int rootB = find(parent, edge.to);                  // @kruskalFind
            if (rootA != rootB) {                               // @kruskalFind
                parent[rootA] = rootB;                          // @kruskalAccept
                tree.add(edge);                                 // @kruskalAccept
            }
        }
        return tree;
    }

    // Union-find root lookup with path halving.
    private int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    public static void main(String[] args) {
        AdjacencyListGraph g = new AdjacencyListGraph(6, false); // vertices 0..5 stand for A..F
        int[][] edgeList = {{0, 1, 4}, {0, 2, 2}, {1, 3, 5}, {2, 3, 8}, {2, 4, 10}, {3, 4, 2}, {3, 5, 6}, {4, 5, 3}};
        for (int[] e : edgeList) {
            g.addEdge(e[0], e[1], e[2]);
        }
        System.out.println("BFS from A:  " + g.bfs(0));
        System.out.println("DFS from A:  " + g.dfs(0));
        ShortestPaths sp = g.dijkstra(0);
        System.out.println("Distances:   " + Arrays.toString(sp.distance));
        System.out.println("Prim MST:    " + g.prim(0));
        System.out.println("Kruskal MST: " + g.kruskal());
    }
}
