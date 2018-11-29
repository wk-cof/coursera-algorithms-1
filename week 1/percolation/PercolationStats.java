/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    // perform trials independent experiments on an n-by-n grid
    private double gridSize;
    private double nTrials;

    private double _mean;
    private double _stddev;

    public PercolationStats(int n, int trials) {
        this.gridSize = n;
        this.nTrials = trials;
        this._mean = 0;
        this._stddev = 0;
        double[] percentOfOpenSites = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                perc.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            }
            percentOfOpenSites[i] = (double) perc.numberOfOpenSites() / (n * n);
            _mean += percentOfOpenSites[i];
        }
        _mean /= trials;

        for (int i = 0; i < trials; i++) {
            _stddev += Math.pow(percentOfOpenSites[i] - mean(), 2);
        }
        _stddev = Math.sqrt(_stddev / (trials - 1));
    }

    // sample mean of percolation threshold
    public double mean() {
        return _mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return _stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(nTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(nTrials);
    }

    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        System.out.println(size);
        System.out.println(trials);
        PercolationStats ps = new PercolationStats(size, trials);
        System.out.println(ps.mean());
        System.out.println(ps.stddev());
        System.out.println("[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
