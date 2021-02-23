import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.List;

// simple implementation, do not considerate tree balance
public class KdTree {
    // ------ nested class Node --------
    private static class Node {
        Node left = null;
        Node right = null;
        Node parent;
        private final Point2D p;
        private final int level; // 0 for vertical splits, 1 for horizontal splits

        public Node(Point2D p) {
            this.p = p;
            this.level = 0;
            this.parent = null;
        }

        public Node(Point2D p, Node parent) {
            this.parent = parent;
            this.p = p;
            this.level = (parent.level + 1) % 2;
        }

        public boolean isVertical() {
            return 0 == level;
        }
    } // ------ end of nested class Node --------

    private Node root = null;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return 0 == size();
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (null == p) {
            throw new IllegalArgumentException();
        }
        if (null == root) {
            root = new Node(p);
            size++;
            return;
        }

        Node cur = root;
        Node parent = root;
        while (cur != null) {
            parent = cur;
            if (p.equals(cur.p)) {
                return;
            }
            if (0 == cur.level) {
                if (p.x() < cur.p.x()) {
                    cur = cur.left;
                } else {
                    cur = cur.right;
                }
            } else {
                // 1 == node.level
                if (p.y() < cur.p.y()) {
                    cur = cur.left;
                } else {
                    cur = cur.right;
                }
            }
        }
        Node node = new Node(p, parent);
        if (0 == parent.level) {
            if (p.x() < parent.p.x()) {
                parent.left = node;
            } else {
                parent.right = node;
            }
        } else {
            // 1 == node.level
            if (p.y() < parent.p.y()) {
                parent.left = node;
            } else {
                parent.right = node;
            }
        }
        size++;
    }


    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (null == p) {
            throw new IllegalArgumentException();
        }
        Node cur = root;
        while (cur != null) {
            if (p.equals(cur.p)) {
                return true;
            }
            if (cur.isVertical()) {
                if (p.x() < cur.p.x()) {
                    cur = cur.left;
                } else {
                    cur = cur.right;
                }
            } else {
                // 1 == node.level
                if (p.y() < cur.p.y()) {
                    cur = cur.left;
                } else {
                    cur = cur.right;
                }
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        // travel the tree
        // draw a black point
        // draw a red horizontal line (0 == level) or a blue vertical line (1 == level)
        // the line starts at the edge and end at the parent's line.
        // the first line (for root node) start at and end at edges.
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (null == rect) {
            throw new IllegalArgumentException();
        }
        List<Point2D> points = new LinkedList<>();
        range(rect, points, root);
        return points;
    }

    private void range(RectHV rect, List<Point2D> points, Node node) {
        if (null == node) {
            return;
        }
        if (rect.contains(node.p)) {
            points.add(node.p);
        }
        if (node.isVertical()) {
            if (rect.xmin() < node.p.x()) {
                range(rect, points, node.left);
            }
            if (rect.xmax() >= node.p.x()) {
                range(rect, points, node.right);
            }
        } else {
            // 1 == node.level
            if (rect.ymin() < node.p.y()) {
                range(rect, points, node.left);
            }
            if (rect.ymax() >= node.p.y()) {
                range(rect, points, node.right);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (null == p) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }

        double minDistance = Double.POSITIVE_INFINITY;
        return nearest(p, root, minDistance).p;
    }

    private Node nearest(Point2D p, Node node, double distance) {
        if (node == null) {
            return null;
        }
        Node nodeReturn = null;
        Node nodeTemp = null;
        double d = p.distanceTo(node.p);
        if (d < distance) {
            distance = d;
            nodeReturn = node;
        }

        double px = p.x();
        double py = p.y();
        double nx = node.p.x();
        double ny = node.p.y();

        if ((node.isVertical() && px < nx) || (!node.isVertical() && py < ny)) {
            nodeTemp = nearest(p, node.left, distance);
            if (null != nodeTemp) {
                d = p.distanceTo(nodeTemp.p);
                if (d < distance) {
                    distance = d;
                    nodeReturn = nodeTemp;
                }
            }
            if (((node.isVertical() && distance > nx - px))
                    || (!node.isVertical() && distance > ny - py)) {
                nodeTemp = nearest(p, node.right, distance);
                if (null != nodeTemp) {
                    d = p.distanceTo(nodeTemp.p);
                    if (d < distance) {
                        distance = d;
                        nodeReturn = nodeTemp;
                    }
                }
            }
        } else { //if ((node.isVertical() && px >= nx) || (!node.isVertical() && py >= ny))
            nodeTemp = nearest(p, node.right, distance);
            if (null != nodeTemp) {
                d = p.distanceTo(nodeTemp.p);
                if (d < distance) {
                    distance = d;
                    nodeReturn = nodeTemp;
                }
            }
            if ((node.isVertical() && distance > px - nx)
                    || (!node.isVertical() && distance > py - ny)) {
                nodeTemp = nearest(p, node.left, distance);
                if (null != nodeTemp) {
                    d = p.distanceTo(nodeTemp.p);
                    if (d < distance) {
                        distance = d;
                        nodeReturn = nodeTemp;
                    }
                }
            }
        }

        return nodeReturn;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
