/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF parentNodes;
    private int numberOfOpenSites;
    private int gridSize;

    public Percolation(int n) {                // create n-by-n grid, with all sites blocked
        this.numberOfOpenSites = 0;
        this.gridSize = n;
        this.grid = new boolean[n][n];
        this.parentNodes = new WeightedQuickUnionUF(n * n + 2);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.grid[i][j] = false;
            }
        }
    }

    // assuming 1-based rows and columns
    private int idxInUF(int row, int col) {
        if (row == 0) {
            return 0;
        }
        if (row == this.gridSize + 1) {
            return this.gridSize * this.gridSize + 1;
        }
        int idx = (row - 1) * this.gridSize + (col - 1) + 1;
        return idx;
    }

    public void open(int row, int col) {    // open site (row, col) if it is not open already
        if (!this.grid[row - 1][col - 1]) {
            this.numberOfOpenSites++; // site newly opened
        }
        final int currIdx = this.idxInUF(row, col);
        final int rowm1col = this.idxInUF(row - 1, col);
        final int rowp1col = this.idxInUF(row + 1, col);
        final int rowcolm1 = this.idxInUF(row, col - 1);
        final int rowcolp1 = this.idxInUF(row, col + 1);


        this.grid[row - 1][col - 1] = true;
        if (isOpen(row - 1, col)) {
            this.parentNodes.union(rowm1col, currIdx);
        }
        if (isOpen(row + 1, col)) {
            this.parentNodes.union(rowp1col, currIdx);
        }
        if (col > 1 && col <= this.gridSize && isOpen(row, col - 1)) {
            this.parentNodes.union(rowcolm1, currIdx);
        }
        if (col > 0 && col < this.gridSize && isOpen(row, col + 1)) {
            this.parentNodes.union(rowcolp1, currIdx);
        }
    }

    public boolean isOpen(int row, int col) {   // is site (row, col) open?
        if (row == 0 || row == this.gridSize + 1) { // first and last ghost nodes are always open
            return true;
        }
        return this.grid[row - 1][col - 1];
    }

    // A full site is an open site that can be connected to an open site
    // in the top row via a chain of neighboring (left, right, up, down) open sites.
    // We say the system percolates if there is a full site in the bottom row.
    public boolean isFull(int row, int col) {
        final int currIdx = this.idxInUF(row, col);
        return this.parentNodes.connected(0, currIdx);
    }

    public int numberOfOpenSites() {            // number of open sites
        return this.numberOfOpenSites;
    }

    public boolean percolates() {               // does the system percolate?
        return this.parentNodes.connected(0, this.gridSize * this.gridSize + 1);
    }

    public static void main(String[] args) {
        System.out.println("Hello, World");
        Percolation foo = new Percolation(2);
    }
}
