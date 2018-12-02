import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {
    // finds all line segments containing 4 points
    private final List<LineSegment> lineSegments;

    private class TwoPoints implements Comparable<TwoPoints> {
        private final Point innerP;
        private final Point innerQ;

        TwoPoints(Point p1, Point p2) {
            innerP = p1;
            innerQ = p2;
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

    public BruteCollinearPoints(Point[] points) {
        List<TwoPoints> potentialSegments;
        for (Point pt : points) {
            if (pt == null) {
                throw new IllegalArgumentException("Illegal argument");
            }
        }

        final int numberOfPoints = points.length;
        lineSegments = new ArrayList<>();
        potentialSegments = new ArrayList<>();

        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = i + 1; j < numberOfPoints; j++) {
                for (int k = j + 1; k < numberOfPoints; k++) {
                    for (int m = k + 1; m < numberOfPoints; m++) {
                        if (
                                Double.compare(points[i].slopeTo(points[j]),
                                               points[i].slopeTo(points[k])) == 0 &&
                                        Double.compare(points[i].slopeTo(points[j]),
                                                       points[i].slopeTo(points[m])) == 0) {
                            // figure out the min and max points and add a segment
                            Point[] segmentPoints = { points[i], points[j], points[k], points[m] };
                            TwoPoints tp = getEdgePoints(segmentPoints);
                            potentialSegments.add(tp);
                        }
                    }
                }
            }
        }

        List<TwoPoints> noDups = new ArrayList<>();
        for (TwoPoints tp : potentialSegments) {
            if (!noDups.contains(tp)) {
                noDups.add(tp);
            }
        }
        for (TwoPoints tp : noDups) {
            lineSegments.add(new LineSegment(tp.p(), tp.q()));
        }
    }

    private TwoPoints getEdgePoints(Point[] points) {

        Point maxPoint = points[0];
        Point minPoint = points[0];

        for (int i = 1; i < points.length; i++) {
            if (maxPoint.compareTo(points[i]) < 0) {
                maxPoint = points[i];
            }
            if (minPoint.compareTo(points[i]) > 0) {
                minPoint = points[i];
            }
        }
        return new TwoPoints(minPoint, maxPoint);
    }


    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] arr = new LineSegment[lineSegments.size()];
        for (int i = 0; i < lineSegments.size(); i++) {
            arr[i] = lineSegments.get(i);
        }
        return arr;
    }

    public static void main(String[] args) {
        // read the n points from a file
        if (args[0] == null) {
            throw new IllegalArgumentException("need an argument");
        }
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

        BruteCollinearPoints bruteCollinear = new BruteCollinearPoints(points);
        LineSegment[] segs = bruteCollinear.segments();
        for (int i = 0; i < bruteCollinear.numberOfSegments(); i++) {
            StdOut.println(segs[i]);
            segs[i].draw();
        }
        StdDraw.show();
    }
}
