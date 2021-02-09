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
        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i - 1] == points[i]) {
                throw new IllegalArgumentException("contains a repeated point.");
            }
        }
        // finds segments containing 4 or more points
        Point[] auxiliaryPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            // copy point [i+1..points.length) to auxiliaryPoints
            int len = points.length - i - 1;
            if (len < 3) {
                break;
            }
            for (int j = 0; j < len; j++) {
                auxiliaryPoints[j] = points[i + 1 + j];
            }
            // Sort
            Arrays.sort(auxiliaryPoints, 0, len, points[i].slopeOrder());
            // find elements with same slopTo point[i]
            int l = 0;
            int r = 0;
            for (int j = 1; j < len; j++) {
                if (points[i].slopeTo(auxiliaryPoints[j]) == points[i].slopeTo(auxiliaryPoints[l])) {
                    r = i;
                } else {
                    l = i;
                }
            }
            // auxiliaryPoints[l..r] are collinear
            if (r - l >= 2) {
                // Arrays.sort sorts the array in place. So we point[i] is the smallest
                // and the auxiliaryPoints[r] is the biggest.
                segments.add(new LineSegment(points[i], auxiliaryPoints[r]));
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
        for (LineSegment line: segments) {
            array[i++] = line;
        }
        return array;
    }

    private void validate(Object o) {
        if (null == o) {
            throw new IllegalArgumentException("null value");
        }
    }
}
