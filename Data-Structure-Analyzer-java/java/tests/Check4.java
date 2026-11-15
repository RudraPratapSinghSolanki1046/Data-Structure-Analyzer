import java.util.*;

// Test harness (not shown in the app): graph algorithms against independent references.
public class Check4 {
    static int failures = 0;
    static void fail(String m) { failures++; if (failures < 15) System.out.println("FAIL: " + m); }
    static final int INF = AdjacencyListGraph.INF;

    public static void main(String[] args) {
        Random rnd = new Random(21);
        for (int round = 0; round < 400; round++) {
            int n = 1 + rnd.nextInt(7);
            boolean directed = rnd.nextBoolean();
            AdjacencyListGraph g = new AdjacencyListGraph(n, directed);
            AdjacencyMatrixGraph m = new AdjacencyMatrixGraph(n, directed);
            long[][] fw = new long[n][n];
            for (long[] row : fw) Arrays.fill(row, Long.MAX_VALUE / 4);
            for (int i = 0; i < n; i++) fw[i][i] = 0;
            List<int[]> edges = new ArrayList<>();
            Set<String> seen = new HashSet<>();
            for (int k = rnd.nextInt(n * 2 + 1); k > 0; k--) {
                int a = rnd.nextInt(n), b = rnd.nextInt(n), w = rnd.nextInt(10);
                String key = directed ? a + ">" + b : Math.min(a, b) + "-" + Math.max(a, b);
                if (a == b || !seen.add(key)) continue;
                g.addEdge(a, b, w); m.addEdge(a, b, w); edges.add(new int[]{a, b, w});
                fw[a][b] = Math.min(fw[a][b], w);
                if (!directed) fw[b][a] = Math.min(fw[b][a], w);
            }
            for (int k = 0; k < n; k++) for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) fw[i][j] = Math.min(fw[i][j], fw[i][k] + fw[k][j]);
            // matrix agrees with the edge list
            for (int[] e : edges) if (!m.hasEdge(e[0], e[1]) || m.weight(e[0], e[1]) != e[2] || (!directed && !m.hasEdge(e[1], e[0]))) fail("matrix edge");
            int start = rnd.nextInt(n);
            Set<Integer> reach = new TreeSet<>();
            for (int v = 0; v < n; v++) if (fw[start][v] < Long.MAX_VALUE / 8) reach.add(v);
            List<Integer> bfs = g.bfs(start), dfs = g.dfs(start);
            if (!new TreeSet<>(bfs).equals(reach) || bfs.size() != reach.size()) fail("bfs reach " + bfs + " vs " + reach);
            if (!new TreeSet<>(dfs).equals(reach) || dfs.size() != reach.size()) fail("dfs reach " + dfs + " vs " + reach);
            AdjacencyListGraph.ShortestPaths sp = g.dijkstra(start);
            for (int v = 0; v < n; v++) {
                long expected = fw[start][v] >= Long.MAX_VALUE / 8 ? INF : fw[start][v];
                if (sp.distance[v] != expected) fail("dijkstra " + v + ": " + sp.distance[v] + " vs " + expected);
                if (sp.distance[v] != INF && v != start) {
                    int p = sp.previous[v];
                    long w = Long.MAX_VALUE;
                    for (int[] e : edges) { if (e[0] == p && e[1] == v) w = Math.min(w, e[2]); if (!directed && e[1] == p && e[0] == v) w = Math.min(w, e[2]); }
                    if (p < 0 || sp.distance[p] + w != sp.distance[v]) fail("dijkstra predecessor " + v);
                }
            }
            if (directed) {
                try { g.prim(0); fail("prim should reject directed"); } catch (IllegalStateException e) { /* expected */ }
                try { g.kruskal(); fail("kruskal should reject directed"); } catch (IllegalStateException e) { /* expected */ }
                continue;
            }
            List<AdjacencyListGraph.Edge> pt = g.prim(start), kt = g.kruskal();
            // Prim covers the start component; Kruskal builds a spanning forest. On a connected graph the totals must match.
            int pw = 0, kw = 0;
            for (AdjacencyListGraph.Edge e : pt) pw += e.weight;
            for (AdjacencyListGraph.Edge e : kt) kw += e.weight;
            if (reach.size() == n) {
                if (pt.size() != n - 1 || kt.size() != n - 1 || pw != kw) fail("MST totals prim=" + pw + " kruskal=" + kw + " n=" + n);
                if (n <= 6 && edges.size() <= 10) { // brute force the minimum spanning tree
                    int best = Integer.MAX_VALUE;
                    for (int mask = 0; mask < (1 << edges.size()); mask++) {
                        if (Integer.bitCount(mask) != n - 1) continue;
                        int[] parent = new int[n];
                        for (int i = 0; i < n; i++) parent[i] = i;
                        int comps = n, total = 0;
                        for (int i = 0; i < edges.size(); i++) if ((mask >> i & 1) == 1) {
                            int a = edges.get(i)[0], b = edges.get(i)[1];
                            while (parent[a] != a) a = parent[a];
                            while (parent[b] != b) b = parent[b];
                            if (a != b) { parent[a] = b; comps--; }
                            total += edges.get(i)[2];
                        }
                        if (comps == 1) best = Math.min(best, total);
                    }
                    if (best != Integer.MAX_VALUE && best != kw) fail("MST brute force " + best + " vs " + kw);
                }
            } else if (pt.size() != reach.size() - 1) fail("prim component size");
        }
        try { new AdjacencyListGraph(2, false).addEdge(0, 5, 1); fail("bad vertex accepted"); } catch (IllegalArgumentException e) { /* expected */ }
        try { new AdjacencyListGraph(2, false).addEdge(0, 1, -1); fail("negative weight accepted"); } catch (IllegalArgumentException e) { /* expected */ }
        AdjacencyListGraph empty = new AdjacencyListGraph(0, false);
        if (empty.kruskal().size() != 0) fail("empty graph kruskal");
        AdjacencyListGraph grow = new AdjacencyListGraph(1, true);
        int v = grow.addVertex();
        grow.addEdge(0, v, 3);
        if (grow.bfs(0).size() != 2) fail("addVertex");
        System.out.println(failures == 0 ? "ALL GRAPH TESTS PASSED (400 random graphs vs Floyd-Warshall, brute-force MST, reachability)" : failures + " FAILURES");
        if (failures > 0) System.exit(1);
    }
}
