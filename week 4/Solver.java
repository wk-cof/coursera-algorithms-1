/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Board[] pq;
    private int size;
    private int maxSize;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial.isGoal()) {
            return;
        }
        Iterable<Board> neighbors = initial.neighbors();
        maxSize = 4; // space for initial neighbors
        pq = new Board[maxSize];
        for (Board n : neighbors) {
            insert(n);
        }
    }

    private void exch(int i, int k) {
        Board temp = pq[i];
        pq[i] = pq[k];
        pq[k] = temp;
    }

    private boolean less(int i, int j) {
        return pq[i].manhattan() < pq[j].manhattan();
    }

    // max pq based on manhattan
    private void swim(int k) {
        while (k > 1 && less(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= size) {
            int j = 2 * k;
            if (j < size && less(j, j + 1)) {
                j++;
            }
            if (!less(k, j)) {
                break;
            }
            exch(k, j);
            k = j;
        }
    }

    private void insert(Board b) {
        if (size == maxSize) {
            resize(maxSize * 2);
        }
        pq[++size] = b;
        swim(size);
    }

    private boolean isEmpty() {
        return size == 0;
    }

    private Board delMax() {
        Board max = pq[1];
        exch(1, size--);
        sink(1);
        pq[size + 1] = null;
        return max;
    }

    private void resize(int capacity) {
        Board[] newArr = (Board[]) new Object[capacity];

        for (int i = 0; i < size; i++) {
            newArr[i] = pq[i];
        }
        pq = newArr;
        maxSize = newArr.length;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return 0;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return null;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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
