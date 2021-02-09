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
        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i-1] == points[i]) {
                throw new IllegalArgumentException("contains a repeated point.");
            }
        }
        // finds segments
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double slp1 = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length; k++) {
                    double slp2 = points[i].slopeTo(points[k]);
                    if ( slp1 == slp2){
                        for (int l = k + 1; l < points.length; l++) {
                            double slp3 = points[i].slopeTo(points[l]);
                            if (slp1 == slp3) {
                                segments.add(new LineSegment(points[i], points[l]));
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
