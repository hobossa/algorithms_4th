import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> segments = new LinkedList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
        // finds segments
        for (int p = 0; p < pointsEx.length; p++) {
            for (int q = p + 1; q < pointsEx.length; q++) {
                double slp1 = pointsEx[p].slopeTo(pointsEx[q]);
                for (int r = q + 1; r < pointsEx.length; r++) {
                    double slp2 = pointsEx[p].slopeTo(pointsEx[r]);
                    if (slp1 == slp2) {
                        for (int s = r + 1; s < pointsEx.length; s++) {
                            double slp3 = pointsEx[p].slopeTo(pointsEx[s]);
                            if (slp1 == slp3) {
                                segments.add(new LineSegment(pointsEx[p], pointsEx[s]));
                            }
                        }
                    }
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
}
