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
        if (0 == node.level) {
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
        Node near = root;
        double minDistance = Double.MAX_VALUE;

        return nearest(p, root, minDistance).p;
    }

    private Node nearest(Point2D p, Node node, double distance) {
        if (node == null) {
            return null;
        }
        Node nodeReturn = null;
        double d = p.distanceTo(node.p);
        if (d < distance) {
            distance = d;
            nodeReturn = node;
        }
        Node n1 = null, n2 = null;
        if (0 == node.level) {
            if (p.x() < node.p.x()) {
                n1 = nearest(p, node.left, distance);
                if (n1 != null) {
                    d = p.distanceTo(n1.p);
                    if (d < distance) {
                        nodeReturn = n1;
                        distance = d;
                    }
                }
                if (distance >= node.p.x() - p.x()) {
                    // we still need to search right
                    n2 = nearest(p, node.right, distance);
                }
            } else {
                n1 = nearest(p, node.right, distance);
                if (n1 != null) {
                    d = p.distanceTo(n1.p);
                    if (d < distance) {
                        nodeReturn = n1;
                        distance = d;
                    }
                }
                if (distance > p.x()-node.p.x()) {
                    // we Still need to search Left
                    n2 = nearest(p, node.left, distance);
                }
            }
        } else {
            // 1 == node.level
            if (p.y() < node.p.y()) {
                n1 = nearest(p, node.left, distance);
                if (n1 != null) {
                    d = p.distanceTo(n1.p);
                    if (d < distance) {
                        nodeReturn = n1;
                        distance = d;
                    }
                }
                if (distance >= node.p.y() - p.y()) {
                    n2 = nearest(p, node.right, distance);
                }
            } else {
                n1 = nearest(p, node.right, distance);
                if (n1 != null) {
                    d = p.distanceTo(n1.p);
                    if (d < distance) {
                        nodeReturn = n1;
                        distance = d;
                    }
                }
                if (distance > p.y() - node.p.y()) {
                    n2 = nearest(p, node.left, distance);
                }
            }
        }

        if (n2 != null) {
            d = p.distanceTo(n2.p);
            if (d < distance) {
                nodeReturn = n2;
                distance = d;
            }
        }
        return nodeReturn;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
