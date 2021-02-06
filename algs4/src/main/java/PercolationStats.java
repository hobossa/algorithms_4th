import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double SE = 1.96;

    private final int n;
    private final double[] fractions;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        this.n = n;
        fractions = new double[trials];
        final int total = n * n;
        for (int i = 0; i < trials; i++) {
            Percolation pc = new Percolation(n);
            while (!pc.percolates()) {
                int r = StdRandom.uniform(total);
                int row = (r / n) + 1;
                int col = (r % n) + 1;
                pc.open(row, col);
            }
            fractions[i] = ((double) pc.numberOfOpenSites()) / total;

        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (SE * stddev() / Math.sqrt(n));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (SE * stddev() / Math.sqrt(n));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats s = new PercolationStats(n, trials);
        StdOut.printf("%-23s = " + s.mean() + "\n", "mean");
        StdOut.printf("%-23s = " + s.stddev() + "\n", "stddev");
        StdOut.println("95% confidence interval = [" + s.confidenceLo()
                + ", " + s.confidenceHi() + "]");
    }
}
