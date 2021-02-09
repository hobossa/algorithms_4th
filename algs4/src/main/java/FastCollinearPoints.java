
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> segments = new LinkedList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // checks null;
        validate(points);
        for (Point p : points) {
            validate(p);
        }
        // checks duplicate elements
        Point[] pointsEx = new Point[points.length];
        System.arraycopy(points, 0, pointsEx, 0, pointsEx.length);

        Arrays.sort(pointsEx);
        for (int i = 1; i < pointsEx.length; i++) {
            if (pointsEx[i - 1].compareTo(pointsEx[i]) == 0) {
                throw new IllegalArgumentException("contains a repeated point.");
            }
        }
        // finds segments containing 4 or more points
        Point[] auxiliaryPoints = new Point[pointsEx.length];
        for (int i = 0; i < pointsEx.length; i++) {
            // copy point to auxiliaryPoints
            System.arraycopy(pointsEx, 0, auxiliaryPoints, 0, auxiliaryPoints.length);
            // Sort
            Arrays.sort(auxiliaryPoints, pointsEx[i].slopeOrder());

            // find elements with same slopTo point[i]
            int m = 0;
            int n = 0;
            for (int j = 1; j < auxiliaryPoints.length; j++) {
                if (pointsEx[i].slopeTo(auxiliaryPoints[j]) == pointsEx[i].slopeTo(auxiliaryPoints[m])) {
                    n = j;
                } else {
                    // auxiliaryPoints[m..n] are collinear
                    if (n - m >= 2) {
                        // Arrays.sort sorts the array in place. So auxiliaryPoints[m] is the smallest
                        // and the auxiliaryPoints[n] is the biggest. Add LineSegment in to segments
                        // only if pointsEx[i] is the start point (smaller than auxiliaryPoint[m])
                        if (pointsEx[i].compareTo(auxiliaryPoints[m]) < 0) {
                            segments.add(new LineSegment(pointsEx[i], auxiliaryPoints[n]));
                        }
                    }
                    m = j;
                }
            }
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] array = new LineSegment[numberOfSegments()];
        int i = 0;
        for (LineSegment line : segments) {
            array[i++] = line;
        }
        return array;
    }

    private void validate(Object obj) {
        if (null == obj) {
            throw new IllegalArgumentException("null value");
        }
    }

    ///// test
//    public static void main(String[] args) {
//        //10000,  0
//        //0,      10000
//        //3000,   7000
//        //7000,   3000
//        //20000,  21000
//        //3000,   4000
//        //14000,  15000
//        //6000,   7000
//        Point[] points = new Point[8];
//        points[0] = new Point(10000, 0);
//        points[1] = new Point(0, 10000);
//        points[2] = new Point(3000, 7000);
//        points[3] = new Point(7000, 3000);
//        points[4] = new Point(20000, 21000);
//        points[5] = new Point(3000, 4000);
//        points[6] = new Point(14000, 15000);
//        points[7] = new Point(6000, 7000);
//        FastCollinearPoints fcp = new FastCollinearPoints(points);
//        StdOut.println(fcp.numberOfSegments());
//        for (LineSegment line : fcp.segments()) {
//            StdOut.println(line);
//        }
//    }

}
