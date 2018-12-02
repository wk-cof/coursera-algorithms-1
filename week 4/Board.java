/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int size;
    private int[] state;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("Argument can't be null");
        }
        this.size = blocks.length;
        this.state = new int[size * size];

        int i = 0;
        for (int[] row : blocks) {
            for (int num : row) {
                this.state[i++] = num;
                // System.out.print(num + "  ");
            }
            // System.out.println();
        }

        // construct a priority queue here
    }

    // board dimension n
    public int dimension() {
        return this.size;
    }

    // number of blocks out of place
    public int hamming() {
        int numsOutOfPlace = 0;
        for (int i = 0; i < size * size; i++) {
            if (state[i] == 0) {
                continue;
            }
            if (state[i] != (i + 1)) {
                numsOutOfPlace++;
            }
        }
        return numsOutOfPlace;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int totalDistance = 0;
        for (int i = 0; i < size * size; i++) {
            if (state[i] == 0) {
                continue;
            }
            totalDistance += rowColManhattanDiff(expectedIdx2D(i + 1), expectedIdx2D(state[i]));
        }
        return totalDistance;
    }

    // input number is 1-based
    private int[] expectedIdx2D(int num) {
        if (num == 0) {
            throw new IllegalArgumentException("Can't find the distance of an empty space");
        }
        return oneDToTwoD(num - 1);
    }

    // input and output values are 0-based.
    // E.g. size = 3; idx = 3; => coord = (1, 0)
    private int[] oneDToTwoD(int idx) {
        int[] rowCol = new int[2];
        rowCol[0] = idx / size; // row
        rowCol[1] = idx % size; // col
        return rowCol;
    }

    private int twoDToOneD(int[] twoDIdx) {
        return twoDIdx[0] * size + twoDIdx[1];
    }

    private int findZero() {
        for (int i = 0; i < size * size; i++) {
            if (state[i] == 0) {
                return i;
            }
        }
        throw new IllegalArgumentException("Something went wrong");
    }

    private int rowColManhattanDiff(int[] p1, int[] p2) {
        return Math.abs(p1[0] - p2[0]) + Math.abs(p1[1] - p2[1]);
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] twin = new int[size][size];
        boolean swapped = false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                twin[i][j] = state[i * size + j];
                if (j > 0 && !swapped && twin[i][j] != 0 && twin[i][j - 1] != 0) {
                    swapped = true;
                    int temp = twin[i][j];
                    twin[i][j] = twin[i][j - 1];
                    twin[i][j - 1] = temp;
                }
            }
        }
        return new Board(twin);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) {
            throw new IllegalArgumentException("Compared object must have class Board");
        }
        Board by = (Board) y;
        if (by.dimension() != this.dimension()) {
            return false;
        }
        return by.toString().equals(this.toString());
    }

    public Iterable<Board> neighbors() {
        List<Board> nbrs = new ArrayList<>();
        int zeroIdx = findZero();
        int[] twoDZeroIdx = oneDToTwoD(zeroIdx);
        int[] idxToSwap = new int[2];
        if (twoDZeroIdx[0] > 0) {
            idxToSwap[0] = twoDZeroIdx[0] - 1;
            idxToSwap[1] = twoDZeroIdx[1];
            nbrs.add(swappedBoard(twoDZeroIdx, idxToSwap));
        }
        if (twoDZeroIdx[1] > 0) {
            idxToSwap[0] = twoDZeroIdx[0];
            idxToSwap[1] = twoDZeroIdx[1] - 1;
            nbrs.add(swappedBoard(twoDZeroIdx, idxToSwap));
        }
        if (twoDZeroIdx[0] < (size - 1)) {
            idxToSwap[0] = twoDZeroIdx[0] + 1;
            idxToSwap[1] = twoDZeroIdx[1];
            nbrs.add(swappedBoard(twoDZeroIdx, idxToSwap));
        }
        if (twoDZeroIdx[1] < (size - 1)) {
            idxToSwap[0] = twoDZeroIdx[0];
            idxToSwap[1] = twoDZeroIdx[1] + 1;
            nbrs.add(swappedBoard(twoDZeroIdx, idxToSwap));
        }
        return nbrs;
    }

    // copy of the original board with 2 indices swapped
    private Board swappedBoard(int[] idx1, int[] idx2) {
        int[][] newBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newBoard[i][j] = state[i * size + j];
            }
        }
        int temp = newBoard[idx1[0]][idx1[1]];
        newBoard[idx1[0]][idx1[1]] = newBoard[idx2[0]][idx2[1]];
        newBoard[idx2[0]][idx2[1]] = temp;
        return new Board(newBoard);
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size * size; i++) {
            if (i > 0 && i % size == 0) {
                sb.append("\n");
            }
            sb.append(state[i] + "  ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        System.out.println(initial);
        // System.out.println(initial.manhattan());
        // System.out.println(initial.hamming());
        // System.out.println(initial.twin().hamming());
        // System.out.println(initial.twin().manhattan());
        // System.out.println(initial);
        // System.out.println(initial.twin());
        // int[] x = new int[2];
        // x[0] = 0;
        // x[1] = 0;
        // int[] y = new int[2];
        // y[0] = 1;
        // y[1] = 1;
        // System.out.println(initial.swappedBoard(x, y));
        // for (Board b : initial.neighbors()) {
        //     System.out.println(b);
        // }
    }
}
