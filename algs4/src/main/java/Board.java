import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
    // -------- nested class BoardIterator --------
    private class BoardIterator implements Iterator<Board> {
        private int curNeighbor = 0;

        public BoardIterator() {
            if (-1 == zeroRow) {
                for (int i = 0; i < dimension; i++) {
                    for (int j = 0; j < dimension; j++) {
                        if (tiles[i][j] == 0) {
                            zeroRow = i;
                            zeroCol = j;
                        }
                    }
                }
            }
            if (null == neighborMarks) {
                neighborMarks = new int[4][2];
                for (int i = 0; i < 4; i++) {
                    neighborMarks[i][0] = -1;
                    neighborMarks[i][1] = -1;
                }
                int n = 0;
                if (zeroCol > 0) {
                    neighborMarks[n][0] = zeroRow;
                    neighborMarks[n][1] = zeroCol - 1;
                    n++;
                }
                if (zeroCol < dimension - 1) {
                    neighborMarks[n][0] = zeroRow;
                    neighborMarks[n][1] = zeroCol + 1;
                    n++;
                }
                if (zeroRow > 0) {
                    neighborMarks[n][0] = zeroRow - 1;
                    neighborMarks[n][1] = zeroCol;
                    n++;
                }
                if (zeroRow < dimension - 1) {
                    neighborMarks[n][0] = zeroRow + 1;
                    neighborMarks[n][1] = zeroCol;
                }
            }
        }

        @Override
        public boolean hasNext() {
            if (curNeighbor < 4) {
                return neighborMarks[curNeighbor][0] != -1;
            }
            return false;
        }

        @Override
        public Board next() {
            //if (!hasNext()) {
            //    throw new NoSuchElementException("nothing left");
            //}
            Board neighbor = new Board(tiles);
            int neighborRow = neighborMarks[curNeighbor][0];
            int neighborCol = neighborMarks[curNeighbor][1];
            int v = neighbor.tiles[neighborRow][neighborCol];
            neighbor.tiles[zeroRow][zeroCol] = v;
            neighbor.tiles[neighborRow][neighborCol] = 0;
            if (-1 != hamming) {
                neighbor.hamming = hamming +
                        getHamming(v, zeroRow, zeroCol) - getHamming(v, neighborRow, neighborCol);
            }
            if (-1 != manhattan) {
                neighbor.manhattan = manhattan +
                        getManhattan(v, zeroRow, zeroCol) - getManhattan(v, neighborRow, neighborCol);
            }
            curNeighbor++;
            return neighbor;
        }
    } // -------- end of nested class BoardIterator --------

    // -------- nested class BoardIterable --------
    private class BoardIterable implements Iterable<Board> {

        @Override
        public Iterator<Board> iterator() {
            return new BoardIterator();
        }
    }
    // -------- end of nested class BoardIterable --------

    final private int dimension;
    final private int[][] tiles;
    private int hamming = -1;
    private int manhattan = -1;

    private int zeroRow = -1;
    private int zeroCol = -1;
    private int[][] neighborMarks = null;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles.length;
        this.tiles = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension).append('\n');
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                sb.append(' ').append(tiles[i][j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        if (-1 == hamming) {
            hamming = 0;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int v = tiles[i][j];
                    if (v == 0) {
                        zeroRow = i;
                        zeroCol = j;
                    } else {
                        hamming += getHamming(v, i, j);
                    }
                }
            }
        }
        return hamming;
    }

    private int getHamming(int v, int row, int col) {
        if (v != 0 && v - 1 != row * dimension + col) {
            return 1;
        }
        return 0;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (-1 == manhattan) {
            manhattan = 0;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int v = tiles[i][j];
                    if (v == 0) {
                        zeroRow = i;
                        zeroCol = j;
                    } else {
                        // (v-1)/dimension, (v-1)%dimension
                        manhattan += getManhattan(v, i, j);
                    }
                }
            }
        }
        return manhattan;
    }

    private int getManhattan(int v, int row, int col) {
        if (v != 0 && v - 1 != row * dimension + col) {
            return Math.abs((v - 1) / dimension - row) + Math.abs((v - 1) % dimension - col);
        }
        return 0;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (-1 != hamming) {
            return 0 == hamming;
        }
        if (-1 != manhattan) {
            return 0 == manhattan;
        }
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int v = tiles[i][j];
                if (v == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    if (i == dimension - 1 && j == dimension - 1) {
                        // true;
                    } else {
                        return false;
                    }
                } else if (v - 1 != i * dimension + j) {
                    return false;
                }
            }
        }
        hamming = 0;
        manhattan = 0;
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y instanceof Board) {
            Board o = (Board) y;
            if (dimension != o.dimension) {
                return false;
            }
            return Arrays.deepEquals(tiles, o.tiles);
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new BoardIterable();
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(tiles);
        if (tiles[0][0] != 0 && tiles[0][1] != 0) {
            twin.tiles[0][0] = tiles[0][1];
            twin.tiles[0][1] = tiles[0][0];
        } else if (tiles[1][0] != 0 && tiles[1][1] != 0) {
            twin.tiles[1][0] = tiles[1][1];
            twin.tiles[1][1] = tiles[1][0];
        }
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        //int[][] tiles = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
        //int[][] tiles = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        int[][] tiles = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board = new Board(tiles);
        StdOut.println(board);
        StdOut.println("Hamming = " + board.hamming());
        StdOut.println("Manhattan = " + board.manhattan());
        StdOut.println("IsGoal = " + board.isGoal());
        StdOut.println("Twin = \n" + board.twin());
        StdOut.println("neighbours:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("Hamming = " + neighbor.hamming());
            StdOut.println("Manhattan = " + neighbor.manhattan());
        }
    }
}
