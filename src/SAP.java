import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;

public class SAP {

    Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        checkNotNull(G, "digraph is null");
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        checkNotNull(v, "vertex is null");
        checkNotNull(w, "vertex is null");
        BreadthFirstDirectedPaths BFDPfirst = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths BFDPsecond = new BreadthFirstDirectedPaths(digraph, w);

        int ancestor = this.ancestor(v,w);

        if(ancestor == -1){
            return Integer.MAX_VALUE;
        }
        int length = 0;
        if(BFDPfirst.hasPathTo(ancestor) && BFDPsecond.hasPathTo(ancestor)){
            length = BFDPfirst.distTo(ancestor) + BFDPsecond.distTo(ancestor);
        }
        return length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        checkNotNull(v, "vertex is null");
        checkNotNull(w, "vertex is null");
        BreadthFirstDirectedPaths BFDPfirst = new BreadthFirstDirectedPaths(digraph, v);
        Queue<Integer> queueFirst = new Queue<Integer>();
        queueFirst.enqueue(v);

        BreadthFirstDirectedPaths BFDPsecond = new BreadthFirstDirectedPaths(digraph, w);
        Queue<Integer> queueSecond = new Queue<Integer>();
        queueSecond.enqueue(w);

        int ancestor = -1;

        while(!queueFirst.isEmpty() || !queueSecond.isEmpty()){
            if (!queueFirst.isEmpty()) {
                int vertexFirst = queueFirst.dequeue();
                if (BFDPsecond.hasPathTo(vertexFirst)) {
                    ancestor = vertexFirst;
                    break;
                }
                for (int vertexAdj : digraph.adj(vertexFirst)) {
                    queueFirst.enqueue(vertexAdj);
                }
            }
            if(!queueSecond.isEmpty()) {
                int vertexSecond = queueSecond.dequeue();
                if (BFDPfirst.hasPathTo(vertexSecond)) {
                    ancestor = vertexSecond;
                    break;
                }
                for (int vertexAdj : digraph.adj(vertexSecond)) {
                    queueSecond.enqueue(vertexAdj);
                }
            }
        }

        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        checkNotNullInArray(v, "iterable argument contains a null item");
        checkNotNullInArray(w, "iterable argument contains a null item");
        MinPQ<AncestorAndLenght> minPQAncestors = new MinPQ<AncestorAndLenght>(AncestorAndLenght.Comparator);
        for(int vertexV: v){
            for (int vertexW: w){
                AncestorAndLenght ancestorAndLenght = new AncestorAndLenght(this.ancestor(vertexV, vertexW),
                                                                            this.length(vertexV,vertexW));
                minPQAncestors.insert(ancestorAndLenght);
            }
        }

        return minPQAncestors.delMin().getLength();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        checkNotNullInArray(v, "iterable argument contains a null item");
        checkNotNullInArray(w, "iterable argument contains a null item");

        MinPQ<AncestorAndLenght> minPQAncestors = new MinPQ<AncestorAndLenght>(AncestorAndLenght.Comparator);
        for(int vertexV: v){
            for (int vertexW: w){
                AncestorAndLenght ancestorAndLenght = new AncestorAndLenght(this.ancestor(vertexV, vertexW),
                        this.length(vertexV,vertexW));
                minPQAncestors.insert(ancestorAndLenght);
            }
        }

        return minPQAncestors.delMin().getAncestor();
    }

    private static void checkNotNull(Object o, String messageIfObjectIsNull) {
        if (o == null) throw new IllegalArgumentException(messageIfObjectIsNull);
    }

    private static void checkNotNullInArray(Iterable<Integer> o, String messageIfObjectIsNull) {
        for(Integer i: o){
            checkNotNull(i,messageIfObjectIsNull);
        }
    }


    // do unit testing of this class
    public static void main(String[] args){
        Digraph g = new Digraph(7);
        g.addEdge(0, 5);
        g.addEdge(5, 4);
        g.addEdge(4, 3);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(2, 6);
        SAP sap = new SAP(g);

        ArrayList<Integer> arrayList1 = new ArrayList<Integer>();
        ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
        arrayList1.add(0,null);
        System.out.println(Integer.toString(sap.length(arrayList1,arrayList2)));
     }
}
