import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

public class DijkstraChosenVertex {
    private DijkstraSP[] chosen;

    public DijkstraChosenVertex(EdgeWeightedDigraph G, int length) {
        chosen  = new DijkstraSP[G.V()];
        for (int v = 0; v < length; v++)
            chosen[v] = new DijkstraSP(G, v);
    }

    public Iterable<DirectedEdge> path(int s, int t) {
        validateVertex(s);
        validateVertex(t);
        return chosen[s].pathTo(t);
    }

    public boolean hasPath(int s, int t) {
        validateVertex(s);
        validateVertex(t);
        return dist(s, t) < Double.POSITIVE_INFINITY;
    }

    public double dist(int s, int t) {
        validateVertex(s);
        validateVertex(t);
        return chosen[s].distTo(t);
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = chosen.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
}
