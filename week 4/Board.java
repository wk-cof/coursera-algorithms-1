/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

public class Board {
    private final int size;
    private int[] state;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
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
            totalDistance += rowColManhattanDiff(get2dCoordinate(i + 1), get2dCoordinate(state[i]));
        }
        return totalDistance;
    }

    // input and output values are 0-based.
    // E.g. size = 3; num = 4; coord = (1, 0)
    private int[] get2dCoordinate(int num) {
        if (num == 0) {
            throw new IllegalArgumentException("Can't find the distance of an empty space");
        }
        int[] rowCol = new int[2];
        rowCol[0] = (num - 1) / size; // row
        rowCol[1] = (num - 1) % size; // col
        return rowCol;
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
        return false;
    }

    // public Iterable<Board> neighbors()     // all neighboring boards
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
        // System.out.println(initial.manhattan());
        // System.out.println(initial.hamming());
        // System.out.println(initial.twin().hamming());
        // System.out.println(initial.twin().manhattan());
        System.out.println(initial);
        System.out.println(initial.twin());
    }
}
