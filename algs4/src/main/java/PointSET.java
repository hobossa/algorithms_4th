import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

// Implementation requirements.
// You must use either edu.princeton.cs.algs4.SET or java.util.TreeSet;
// do not implement your own red–black BST.

// Brute-force implementation.
public class PointSET {
    private final Set<Point2D> points = new TreeSet<>();

    // construct an empty set of points
    public PointSET() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (null == p) {
            throw new IllegalArgumentException();
        }
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (null == p) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (null == rect) {
            throw new IllegalArgumentException();
        }
        List<Point2D> listP = new LinkedList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                listP.add(p);
            }
        }
        return listP;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (null == p) {
            throw new IllegalArgumentException();
        }
        Point2D pNearest = null;
        double minDistance = Double.MAX_VALUE;
        for (Point2D point : points) {
            double distance = point.distanceTo(p);
            if (distance < minDistance) {
                minDistance = distance;
                pNearest = point;
            }
        }
        return pNearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
