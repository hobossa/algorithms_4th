import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    // nested class DequeIterator
    private static class DequeIterator<Item> implements Iterator<Item> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Item next() {
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
    private int size = 0;
    // construct an empty deque
    public Deque() {

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
    }

    // add the item to the back
    public void addLast(Item item) {
        if (null == item) {
            throw new IllegalArgumentException("item is null");
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw  new NoSuchElementException("Dequeue is empty.");
        }
        return null;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw  new NoSuchElementException("Dequeue is empty.");
        }
        return null;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator<Item>();
    }

    // unit testing (required)
    public static void main(String[] args) {

    }
}