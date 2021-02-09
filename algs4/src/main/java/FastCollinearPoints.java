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
            // copy point [i+1..points.length) to auxiliaryPoints
            int len = pointsEx.length - i - 1;
            if (len < 3) {
                break;
            }
            for (int j = 0; j < len; j++) {
                auxiliaryPoints[j] = pointsEx[i + 1 + j];
            }
            // Sort
            Arrays.sort(auxiliaryPoints, 0, len, pointsEx[i].slopeOrder());
            // find elements with same slopTo point[i]
            int m = 0;
            int n = 0;
            for (int j = 1; j < len; j++) {
                if (pointsEx[i].slopeTo(auxiliaryPoints[j]) == pointsEx[i].slopeTo(auxiliaryPoints[m])) {
                    n = i;
                } else {
                    m = i;
                }
            }
            // auxiliaryPoints[m..n] are collinear
            if (n - m >= 2) {
                // Arrays.sort sorts the array in place. So we point[i] is the smallest
                // and the auxiliaryPoints[r] is the biggest.
                segments.add(new LineSegment(pointsEx[i], auxiliaryPoints[n]));
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
}
