import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Solver {
    // -------- nested class SearchNode --------
    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode prev;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }
    } // -------- end of nested class SearchNode --------

    private Board initial;
    private SearchNode goalNode = null;
    private Comparator<SearchNode> comp = Comparator.comparingInt(o -> o.board.manhattan());

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (null == initial) {
            throw new IllegalArgumentException("argument is null");
        }
        this.initial = initial;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>(1, comp);
        minPQ.insert(new SearchNode(initial, 0, null));
        MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>(1, comp);
        minPQTwin.insert(new SearchNode(initial.twin(), 0, null));

        while (true) {
            SearchNode node = minPQ.delMin();
            if (node.board.isGoal()) {
                goalNode = node;
                return true;
            } else {
                for (Board b : node.board.neighbors()) {
                    if (node.prev == null || !node.prev.board.equals(b)) {
                        minPQ.insert(new SearchNode(b, node.moves+1, node));
                    }
                }
            }

            SearchNode nodeTwin = minPQTwin.delMin();
            if (nodeTwin.board.isGoal()) {
                return false;
            } else {
                for (Board bTwin : nodeTwin.board.neighbors()) {
                    if (nodeTwin.prev == null || !nodeTwin.prev.board.equals(bTwin)) {
                        minPQTwin.insert(new SearchNode(bTwin, nodeTwin.moves+1, nodeTwin));
                    }
                }
            }
        }
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return goalNode == null ? -1 : goalNode.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (goalNode == null) {
            return null;
        }
        List<Board> temp = new LinkedList<Board>();
        SearchNode node = goalNode;
        while (node != null) {
            temp.add(0, node.board);
            node = node.prev;
        }
        return temp;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
//        int[][] tiles = {{0,1,3},{4,2,5},{7,8,6}};
//                Board initial = new Board(tiles);
//
//        // solve the puzzle
//        Solver solver = new Solver(initial);
//
//        // print solution to standard output
//        if (!solver.isSolvable())
//            StdOut.println("No solution possible");
//        else {
//            StdOut.println("Minimum number of moves = " + solver.moves());
//            for (Board board : solver.solution())
//                StdOut.println(board);
//        }

        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
