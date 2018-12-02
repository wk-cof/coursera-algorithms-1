import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> segs;
    private final List<TwoPoints> potentialSegments;

    private class TwoPoints implements Comparable<TwoPoints> {
        private final Point innerP;
        private final Point innerQ;

        TwoPoints(Point p1, Point p2) {
            innerP = p1;
            innerQ = p2;
        }

        private boolean includedIn(List<TwoPoints> pts) {
            for (TwoPoints p : pts) {
                if (p.compareTo(this) == 0) {
                    return true;
                }
            }
            return false;
        }


        public Point p() {
            return innerP;
        }

        public Point q() {
            return innerQ;
        }

        @Override
        public int compareTo(TwoPoints tp) {
            return this.p().compareTo(tp.p()) == 0 && this.q().compareTo(tp.q()) == 0 ? 0 : 1;
        }

        @Override
        public String toString() {
            return p().toString() + " -> " + q().toString();
        }
    }

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        final int numOfPoints = points.length;
        Point[] originalPoints = new Point[points.length];

        Arrays.sort(points);
        for (int i = 0; i < numOfPoints; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Illegal argument");
            }
            originalPoints[i] = points[i];
        }

        potentialSegments = new ArrayList<>();
        segs = new ArrayList<>();

        for (int i = 1; i < numOfPoints; i++) {
            if (points[i - 1] == points[i]) {
                throw new IllegalArgumentException("Duplicate point");
            }
        }
        for (int i = 0; i < numOfPoints; i++) {
            Arrays.sort(points, originalPoints[i].slopeOrder());
            // for (Point pt : points) {
            //     System.out.print(pt + "  ");
            // }
            // System.out.println();
            getCollinearPointsFromSortedArray(points);
        }

        List<TwoPoints> noDups = new ArrayList<>();

        for (TwoPoints tp : potentialSegments) {
            if (!tp.includedIn(noDups)) {
                noDups.add(tp);
            }
        }
        for (TwoPoints tp : noDups) {
            segs.add(new LineSegment(tp.p(), tp.q()));
        }
    }

    private void getCollinearPointsFromSortedArray(Point[] points) {
        int numOfPoints = points.length;

        int k;
        Point origin = points[0];
        for (int i = 1; i < numOfPoints - 1; i++) {
            k = 0;
            while (i < numOfPoints - 1 &&
                    Double.compare(origin.slopeTo(points[i]), origin.slopeTo(points[i + 1])) == 0) {
                // System.out.println(
                //         "k = " + k + ", i = " + i + " ~ " + points[i] + " ~ " + points[i + 1]);
                k++;
                i++;
            }

            if (k > 1) { // 2 or more means we have 4 collinear points
                List<Point> myPoints = new ArrayList<>();
                myPoints.add(origin);
                for (int j = 0; j <= k; j++) {
                    myPoints.add(points[i - j]);
                }

                TwoPoints tp = getEdgePoints(myPoints);
                // StdOut.println("Potential Segment: " + tp);
                potentialSegments.add(tp);
            }
        }
    }

    private TwoPoints getEdgePoints(List<Point> points) {

        Point maxPoint = points.get(0);
        Point minPoint = points.get(0);

        for (int i = 1; i < points.size(); i++) {
            if (maxPoint.compareTo(points.get(i)) < 0) {
                maxPoint = points.get(i);
            }
            if (minPoint.compareTo(points.get(i)) > 0) {
                minPoint = points.get(i);
            }
        }
        return new TwoPoints(minPoint, maxPoint);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segs.size();
    }

    // // the line segments
    public LineSegment[] segments() {
        LineSegment[] arr = new LineSegment[segs.size()];
        for (int i = 0; i < segs.size(); i++) {
            arr[i] = segs.get(i);
        }
        return arr;
    }

    public static void main(String[] args) {
        if (args[0] == null) {
            throw new IllegalArgumentException("need an argument");
        }
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
