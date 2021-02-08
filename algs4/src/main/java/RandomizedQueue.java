import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // -------- nested class RandomizedQueueIterator --------
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int[] randomIndices;
        private int current;

        public RandomizedQueueIterator() {
            this.current = 0;
            randomIndices = new int[size];
            for (int i = 0; i < size; i++) {
                randomIndices[i] = i;
            }
            StdRandom.shuffle(randomIndices);
        }

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no next element");
            }
            Item temp = data[(head + randomIndices[current]) % data.length];
            current++;
            return temp;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    } // -------- end of nested class RandomizedQueueIterator --------

    // trick minimal capacity is 2, so we can easily to deal with hasNext
    // because hasNext() {return current < (head + size) % data.length;}
    // will always be false when data.length == 1
    private static final int MINI_CAPACITY = 2;
    private Item[] data;
    private int head;
    private int size;

    // construct an empty randomized queue
    @SuppressWarnings("unchecked")
    public RandomizedQueue() {
        data = (Item[]) new Object[MINI_CAPACITY];
        head = 0;
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return 0 == size;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        resizeWhenNeeded();
        data[(head + size) % data.length] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        int r = StdRandom.uniform(size);
        swap(head, (head + r) % data.length);
        Item temp = data[head];
        data[head++] = null;
        head = head % data.length;
        size--;
        resizeWhenNeeded();
        return temp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        int r = StdRandom.uniform(size);
        return data[(head + r) % data.length];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    @SuppressWarnings("unchecked")
    private void resizeWhenNeeded() {
        int newCapacity = 0;
        if (size == data.length) {
            // resize to 2n
            newCapacity = 2 * data.length;
        } else if (size < data.length / 4) {
            // resize to n/2
            newCapacity = data.length / 2;
            newCapacity = Math.max(newCapacity, MINI_CAPACITY);
        } else {
            return;
        }
        if (newCapacity > 0) {
            Item[] temp = data;
            data = (Item[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                data[i] = temp[(head + i) % temp.length];
            }
            head = 0;
        }
    }

    private void swap(int a, int b) {
        Item temp = data[a];
        data[a] = data[b];
        data[b] = temp;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue1 = new RandomizedQueue<>();
        StdOut.println("isEmpty:" + queue1.isEmpty());
        StdOut.println("size: " + queue1.size());
        queue1.enqueue(0);
        StdOut.println("isEmpty:" + queue1.isEmpty());
        StdOut.println("size: " + queue1.size());
        StdOut.println("elements in Queue:");
        for (Integer i : queue1) {
            StdOut.println("  " + i);
        }
        StdOut.println("dequeue: " + queue1.dequeue());
        StdOut.println("isEmpty:" + queue1.isEmpty());
        StdOut.println("size: " + queue1.size());

        for (int i = 0; i < 10; i++) {
            queue1.enqueue(i);
        }
        StdOut.println("isEmpty:" + queue1.isEmpty());
        StdOut.println("size: " + queue1.size());
        StdOut.println("elements in Queue:");
        for (Integer i : queue1) {
            StdOut.println("  " + i);
        }

        int sz = queue1.size();
        for (int i = 0; i < sz; i++) {
            StdOut.println("sample: " + queue1.sample());
        }
        for (int i = 0; i < sz; i++) {
            StdOut.println("dequeue: " + queue1.dequeue());
        }

        StdOut.println("isEmpty:" + queue1.isEmpty());
        StdOut.println("size: " + queue1.size());
        StdOut.println("elements in Queue:");
        for (Integer i : queue1) {
            StdOut.println("  " + i);
        }

        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }
}