import java.util.Iterator;

public class Board {
    // -------- nested class BoardIterator --------
    private class BoardIterator implements Iterator<Board> {
        public BoardIterator() {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Board next() {
            return null;
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

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles.length;
        this.tiles = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, dimension);
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
                    if (tiles[i][j] - 1 != i * dimension + j) {
                        hamming++;
                    }
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (-1 == manhattan) {
            manhattan = 0;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int v = tiles[i][j];
                    if (v - 1 != i * dimension + j) {
                        // (v-1)/dimension, (v-1)%dimension
                        manhattan += Math.abs((v - 1) / dimension - i);
                        manhattan += Math.abs((v - 1) % dimension - j);
                    }
                }
            }
        }
        return manhattan;
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
                if (tiles[i][j] - 1 != i * dimension + j) {
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
            Board o = (Board)y;
            if (dimension != o.dimension) {
                return false;
            }
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (tiles[i][j] != o.tiles[i][j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new BoardIterable();
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }
}
