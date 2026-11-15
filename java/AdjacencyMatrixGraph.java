import java.util.ArrayList;
import java.util.List;

// Graph stored as a V x V matrix: matrix[u][v] is the weight of edge u -> v, or NO_EDGE.
// Edge lookup is O(1) but the matrix always uses O(V^2) space.
public class AdjacencyMatrixGraph {
    public static final int NO_EDGE = -1;

    private int[][] matrix;
    private int vertexCount;
    private final boolean directed;

    public AdjacencyMatrixGraph(int vertexCount, boolean directed) {
        if (vertexCount < 0) {
            throw new IllegalArgumentException("vertexCount must not be negative");
        }
        this.vertexCount = vertexCount;
        this.directed = directed;
        this.matrix = new int[vertexCount][vertexCount];
        for (int[] row : matrix) {
            java.util.Arrays.fill(row, NO_EDGE);
        }
    }

    // Grows the matrix by one row and one column.
    public int addVertex() {
        int[][] bigger = new int[vertexCount + 1][vertexCount + 1];     // @addVertex
        for (int i = 0; i <= vertexCount; i++) {                        // @addVertex
            for (int j = 0; j <= vertexCount; j++) {                    // @addVertex
                bigger[i][j] = (i < vertexCount && j < vertexCount) ? matrix[i][j] : NO_EDGE;   // @addVertex
            }
        }
        matrix = bigger;                                                // @addVertex
        return vertexCount++;                                           // @addVertex
    }

    public void addEdge(int from, int to, int weight) {
        checkVertex(from);
        checkVertex(to);
        if (weight < 0) {
            throw new IllegalArgumentException("Negative weights are not supported");
        }
        matrix[from][to] = weight;                                      // @addEdge
        if (!directed) {
            matrix[to][from] = weight;                                  // @addEdge
        }
    }

    public void removeEdge(int from, int to) {
        checkVertex(from);
        checkVertex(to);
        matrix[from][to] = NO_EDGE;
        if (!directed) {
            matrix[to][from] = NO_EDGE;
        }
    }

    public boolean hasEdge(int from, int to) {
        checkVertex(from);
        checkVertex(to);
        return matrix[from][to] != NO_EDGE;
    }

    public int weight(int from, int to) {
        checkVertex(from);
        checkVertex(to);
        return matrix[from][to];
    }

    // Vertices reachable from v by one edge, in increasing order.
    public List<Integer> neighbours(int v) {
        checkVertex(v);
        List<Integer> result = new ArrayList<>();
        for (int u = 0; u < vertexCount; u++) {
            if (matrix[v][u] != NO_EDGE) {
                result.add(u);
            }
        }
        return result;
    }

    public int vertexCount() { return vertexCount; }

    private void checkVertex(int v) {
        if (v < 0 || v >= vertexCount) {
            throw new IllegalArgumentException("No such vertex: " + v);
        }
    }

    public void print() {
        for (int[] row : matrix) {
            StringBuilder line = new StringBuilder();
            for (int cell : row) {
                line.append(cell == NO_EDGE ? " ." : String.format("%2d", cell)).append(' ');
            }
            System.out.println(line.toString().stripTrailing());
        }
    }

    public static void main(String[] args) {
        AdjacencyMatrixGraph g = new AdjacencyMatrixGraph(4, false);
        g.addEdge(0, 1, 5);
        g.addEdge(1, 2, 3);
        g.addEdge(2, 3, 7);
        g.addVertex(); // vertex 4
        g.addEdge(3, 4, 1);
        g.print();
        System.out.println("Neighbours of 3: " + g.neighbours(3) + ", edge 0-2? " + g.hasEdge(0, 2));
    }
}
