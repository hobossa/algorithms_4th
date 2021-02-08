import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    // -------- nested class Node --------
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;

        public Node(Item item, Node<Item> next, Node<Item> prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public Node<Item> getNext() {
            return next;
        }

        public void setNext(Node<Item> next) {
            this.next = next;
        }

        public Node<Item> getPrev() {
            return prev;
        }

        public void setPrev(Node<Item> prev) {
            this.prev = prev;
        }
    } // -------- end of nested class Node --------

    // -------- nested class DequeIterator --------
    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current;

        public DequeIterator() {
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            // we has a tail element
            return current.next != tail;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no next element");
            }
            current = current.next;
            return current.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    } // -------- end of nested class DequeIterator --------

    private Node<Item> head;
    private Node<Item> tail;
    private int size;

    // construct an empty deque
    public Deque() {
        head = new Node<>(null, null, null);
        tail = new Node<>(null, null, head);
        head.setNext(tail);
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return 0 == size;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (null == item) {
            throw new IllegalArgumentException("item is null");
        }
        Node<Item> newNode = new Node<>(item, null, null);
        insertNodeBetween(newNode, head, head.next);
    }

    // add the item to the back
    public void addLast(Item item) {
        if (null == item) {
            throw new IllegalArgumentException("item is null");
        }
        Node<Item> newNode = new Node<>(item, null, null);
        insertNodeBetween(newNode, tail.prev, tail);
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Dequeue is empty.");
        }
        return removeNodeBetween(head.getNext(), head, head.getNext().getNext());
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Dequeue is empty.");
        }
        return removeNodeBetween(tail.getPrev(), tail.getPrev().getPrev(), tail);
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private void insertNodeBetween(Node<Item> node, Node<Item> prevNode, Node<Item> nextNode) {
        node.setPrev(prevNode);
        node.setNext(nextNode);
        prevNode.setNext(node);
        nextNode.setPrev(node);
        size++;
    }

    private Item removeNodeBetween(Node<Item> node, Node<Item> prevNode, Node<Item> nextNode) {
        Item item = node.getItem();
        node = null;
        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);
        size--;
        return item;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        StdOut.println("elements in Deque:");
        for (Integer i : deque) {
            StdOut.println("  " + i);
        }
        StdOut.println("removeLast: " + deque.removeLast());
        StdOut.println("isEmpty:" + deque.isEmpty());
        StdOut.println("size: " + deque.size());
        deque.addFirst(2);
        deque.addLast(3);
        deque.addFirst(1);
        deque.addLast(4);
        StdOut.println("isEmpty:" + deque.isEmpty());
        StdOut.println("size: " + deque.size());
        StdOut.println("elements in Deque:");
        for (Integer i : deque) {
            StdOut.println("  " + i);
        }
        StdOut.println("removeFirst: " + deque.removeFirst());
        StdOut.println("removeLast: " + deque.removeLast());
        StdOut.println("elements in Deque:");
        for (Integer i : deque) {
            StdOut.println("  " + i);
        }
        StdOut.println("isEmpty:" + deque.isEmpty());
        StdOut.println("size: " + deque.size());
        StdOut.println("removeFirst: " + deque.removeFirst());
        StdOut.println("removeFirst: " + deque.removeFirst());
        StdOut.println("isEmpty:" + deque.isEmpty());
        StdOut.println("size: " + deque.size());
    }
}