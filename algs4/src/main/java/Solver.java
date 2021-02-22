import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

public class Solver {
    // -------- nested class SearchNode --------
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode prev;
        private int priority;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = this.moves + board.manhattan();
        }

        @Override
        public int compareTo(SearchNode o) {
            return this.priority - o.priority;
        }
    } // -------- end of nested class SearchNode --------

    private static final int MAX_MOVES = 100;

    private Board initial;
    private boolean isSolvable;
    private SearchNode goalNode = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (null == initial) {
            throw new IllegalArgumentException("argument is null");
        }
        this.initial = initial;
        this.isSolvable = trySolvable();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    private boolean trySolvable() {
        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
        minPQ.insert(new SearchNode(initial, 0, null));
        MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>();
        minPQTwin.insert(new SearchNode(initial.twin(), 0, null));

        while (true) {
            SearchNode node = minPQ.delMin();
            if (node.board.isGoal()) {
                goalNode = node;
                return true;
            } else if (node.moves >= MAX_MOVES) {
                break;
            } else {
                for (Board neighbor : node.board.neighbors()) {
                    if (node.prev == null || !node.prev.board.equals(neighbor)) {
                        minPQ.insert(new SearchNode(neighbor, node.moves + 1, node));
                    }
                }
            }

            SearchNode nodeTwin = minPQTwin.delMin();
            if (nodeTwin.board.isGoal()) {
                return false;
            } else {
                for (Board neighborTwin : nodeTwin.board.neighbors()) {
                    if (nodeTwin.prev == null || !nodeTwin.prev.board.equals(neighborTwin)) {
                        minPQTwin.insert(new SearchNode(neighborTwin, nodeTwin.moves + 1, nodeTwin));
                    }
                }
            }
        }
        return false;
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
        if (args.length == 0) {
            // create initial board from file
            //int[][] tiles = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
            int[][] tiles = {{1, 0, 2}, {7, 5, 4}, {8, 6, 3}};
            //int[][] tiles = {{4, 8, 2}, {3, 6, 5}, {1, 7, 0}};
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
        } else {
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
}
