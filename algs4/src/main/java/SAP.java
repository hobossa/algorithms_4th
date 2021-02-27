import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class SAP {

    // -------- nested class BreadthFirstDirectedPathsEx --------
    // copy from edu.princeton.cs.algs4
    // add a container to keep track of entries on the path.
    private static class BreadthFirstDirectedPathsEx {
        private static final int INFINITY = Integer.MAX_VALUE;
        private final boolean[] marked;  // marked[v] = is there an s->v path?
        private final int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
        private final int[] distTo;      // distTo[v] = length of shortest s->v path

        private final List<Integer> track = new LinkedList<>();

        public BreadthFirstDirectedPathsEx(Digraph G, int s) {
            marked = new boolean[G.V()];
            distTo = new int[G.V()];
            edgeTo = new int[G.V()];
            for (int v = 0; v < G.V(); v++)
                distTo[v] = INFINITY;
            validateVertex(s);
            bfs(G, s);
        }

        public BreadthFirstDirectedPathsEx(Digraph G, Iterable<Integer> sources) {
            marked = new boolean[G.V()];
            distTo = new int[G.V()];
            edgeTo = new int[G.V()];
            for (int v = 0; v < G.V(); v++)
                distTo[v] = INFINITY;
            validateVertices(sources);
            bfs(G, sources);
        }

        public Iterable<Integer> getTrack() {
            return track;
        }

        // BFS from single source
        private void bfs(Digraph G, int s) {
            Queue<Integer> q = new LinkedList<>();
            marked[s] = true;
            track.add(s);
            distTo[s] = 0;
            q.offer(s);
            while (!q.isEmpty()) {
                int v = q.poll();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        track.add(w);
                        q.offer(w);
                    }
                }
            }
        }

        // BFS from multiple sources
        private void bfs(Digraph G, Iterable<Integer> sources) {
            Queue<Integer> q = new LinkedList<Integer>();
            for (int s : sources) {
                marked[s] = true;
                track.add(s);
                distTo[s] = 0;
                q.offer(s);
            }
            while (!q.isEmpty()) {
                int v = q.poll();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        track.add(w);
                        q.offer(w);
                    }
                }
            }
        }

        public boolean hasPathTo(int v) {
            validateVertex(v);
            return marked[v];
        }

        public int distTo(int v) {
            validateVertex(v);
            return distTo[v];
        }

        public Iterable<Integer> pathTo(int v) {
            validateVertex(v);

            if (!hasPathTo(v)) return null;
            Stack<Integer> path = new Stack<Integer>();
            int x;
            for (x = v; distTo[x] != 0; x = edgeTo[x])
                path.push(x);
            path.push(x);
            return path;
        }

        // throw an IllegalArgumentException unless {@code 0 <= v < V}
        private void validateVertex(int v) {
            int V = marked.length;
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }

        // throw an IllegalArgumentException if vertices is null, has zero vertices,
        // or has a vertex not between 0 and V-1
        private void validateVertices(Iterable<Integer> vertices) {
            if (vertices == null) {
                throw new IllegalArgumentException("argument is null");
            }
            int V = marked.length;
            int count = 0;
            for (Integer v : vertices) {
                count++;
                if (v == null) {
                    throw new IllegalArgumentException("vertex is null");
                }
                validateVertex(v);
            }
            if (count == 0) {
                throw new IllegalArgumentException("zero vertices");
            }
        }
    }     // -------- end of nested class BreadthFirstDirectedPathsEx --------

    final private Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("arguments are null.");
        }
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validate(v);
        validate(w);
        // bidirectional search would be better.
        BreadthFirstDirectedPathsEx vPath = new BreadthFirstDirectedPathsEx(digraph, v);
        BreadthFirstDirectedPathsEx wPath = new BreadthFirstDirectedPathsEx(digraph, w);
        int ancestor = -1;
        int minLen = Integer.MAX_VALUE;
        for (Integer n : vPath.getTrack()) {
            if (wPath.hasPathTo(n)) {
                int len = vPath.distTo(n) + wPath.distTo(n);
                if (len < minLen) {
                    ancestor = n;
                    minLen = len;
                }
            }
        }
        return ancestor == -1 ? -1 : minLen;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validate(v);
        validate(w);
        // bidirectional search would be better.
        BreadthFirstDirectedPathsEx vPath = new BreadthFirstDirectedPathsEx(digraph, v);
        BreadthFirstDirectedPathsEx wPath = new BreadthFirstDirectedPathsEx(digraph, w);
        int ancestor = -1;
        int minLen = Integer.MAX_VALUE;
        for (Integer n : vPath.getTrack()) {
            if (wPath.hasPathTo(n)) {
                int len = vPath.distTo(n) + wPath.distTo(n);
                if (len < minLen) {
                    ancestor = n;
                    minLen = len;
                }
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        // bidirectional search would be better.
        BreadthFirstDirectedPathsEx vPath = new BreadthFirstDirectedPathsEx(digraph, v);
        BreadthFirstDirectedPathsEx wPath = new BreadthFirstDirectedPathsEx(digraph, w);
        int ancestor = -1;
        int minLen = Integer.MAX_VALUE;
        for (Integer n : vPath.getTrack()) {
            if (wPath.hasPathTo(n)) {
                int len = vPath.distTo(n) + wPath.distTo(n);
                if (len < minLen) {
                    ancestor = n;
                    minLen = len;
                }
            }
        }
        return ancestor == -1 ? -1 : minLen;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        // bidirectional search would be better.
        BreadthFirstDirectedPathsEx vPath = new BreadthFirstDirectedPathsEx(digraph, v);
        BreadthFirstDirectedPathsEx wPath = new BreadthFirstDirectedPathsEx(digraph, w);
        int ancestor = -1;
        int minLen = Integer.MAX_VALUE;
        for (Integer n : vPath.getTrack()) {
            if (wPath.hasPathTo(n)) {
                int len = vPath.distTo(n) + wPath.distTo(n);
                if (len < minLen) {
                    ancestor = n;
                    minLen = len;
                }
            }
        }
        return ancestor;
    }

    private void validate(Iterable<Integer> s) {
        if (null == s) {
            throw new IllegalArgumentException();
        }
        int count = 0;
        for (Integer n: s) {
            count++;
        }
        if ( 0 == count) {
            throw new IllegalArgumentException();
        }
    }

    private void validate(int n) {
        if (n < 0 || n > digraph.V()) {
            throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int v = 3;
        int w = 3;
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
