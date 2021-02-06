import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final WeightedQuickUnionUF uf;
    private int numberOfOpenSites = 0;
    private final boolean[] opened;
    private final int topNode;
    private final int bottomNode;
    //private boolean isPercolates = false;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <=0 ) {
            throw new IllegalArgumentException("Invalid n");
        }
        this.n = n;
        opened = new boolean[n * n];
        uf = new WeightedQuickUnionUF(n * n + 2);
        topNode = n * n;
        bottomNode = n * n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int i = validate(row, col);
        if (isOpen(row, col)) {
            return;
        }
        opened[i] = true;
        numberOfOpenSites++;

        // check and union
        // up
        if (row == 1) {
            // union the cell in the first layer with topNode
            uf.union(topNode, i);

        } else {
            unionWithOpenedNeighbour(i, row - 1, col);
        }
        // down
        if (row == n) {
            // union the cell in the last layer with bottomNode
            uf.union(bottomNode, i);
        } else {
            unionWithOpenedNeighbour(i, row + 1, col);
        }
        // left
        if (col != 1) {
            unionWithOpenedNeighbour(i, row, col - 1);
        }
        // right
        if (col != n) {
            unionWithOpenedNeighbour(i, row, col + 1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int i = validate(row, col);
        return opened[i];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int i = validate(row, col);
        return uf.find(i) == uf.find(topNode);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(topNode) == uf.find(bottomNode);
    }


    // private methods
    private int validate(int row, int col) {
        if (row <= 0 || row > n) {
            throw new IllegalArgumentException("Invalid row");
        }
        if (col <= 0 || col > n) {
            throw new IllegalArgumentException("Invalid col");
        }
        return (this.n * (row - 1)) + (col - 1);
    }

    private void unionWithOpenedNeighbour(int i, int row, int col) {
        if (isOpen(row, col)) {
            int neighbour = validate(row, col);
            uf.union(neighbour, i);
        }
    }
}
