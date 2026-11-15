import java.util.*;

// Test helper: args = directed(0/1) vertexCount start edge... where edge = from-to-weight (vertices are numbers).
// Prints results in the same text format as the Graph Lab summary so the two can be compared directly.
public class GraphTrace {
    static String lab(int v) { return String.valueOf((char) ('A' + v)); }

    public static void main(String[] args) {
        boolean directed = args[0].equals("1");
        int n = Integer.parseInt(args[1]), start = Integer.parseInt(args[2]);
        AdjacencyListGraph g = new AdjacencyListGraph(n, directed);
        List<int[]> edges = new ArrayList<>();
        for (int i = 3; i < args.length; i++) {
            String[] p = args[i].split("-");
            int[] e = {Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2])};
            edges.add(e);
            g.addEdge(e[0], e[1], e[2]);
        }
        StringBuilder sb = new StringBuilder();
        for (int v : g.bfs(start)) sb.append(sb.length() == 0 ? "" : " → ").append(lab(v));
        System.out.println("bfs " + sb);
        sb = new StringBuilder();
        for (int v : g.dfs(start)) sb.append(sb.length() == 0 ? "" : " → ").append(lab(v));
        System.out.println("dfs " + sb);
        AdjacencyListGraph.ShortestPaths sp = g.dijkstra(start);
        List<String> parts = new ArrayList<>();
        for (int v = 0; v < n; v++) {
            String route = "";
            if (sp.distance[v] != AdjacencyListGraph.INF) {
                LinkedList<String> r = new LinkedList<>();
                for (int c = v; c != -1; c = sp.previous[c]) r.addFirst(lab(c));
                route = " (" + String.join(" → ", r) + ")";
            }
            parts.add(lab(v) + ": " + (sp.distance[v] == AdjacencyListGraph.INF ? "∞" : sp.distance[v]) + route);
        }
        System.out.println("dijkstra " + String.join(" · ", parts));
        if (!directed) {
            for (String name : new String[]{"prim", "kruskal"}) {
                List<AdjacencyListGraph.Edge> t = name.equals("prim") ? g.prim(start) : g.kruskal();
                List<String> es = new ArrayList<>();
                int total = 0;
                for (AdjacencyListGraph.Edge e : t) { es.add(lab(e.from) + "-" + lab(e.to)); total += e.weight; }
                System.out.println(name + " " + (es.isEmpty() ? "none" : String.join(", ", es)) + " | " + total);
            }
        }
    }
}
