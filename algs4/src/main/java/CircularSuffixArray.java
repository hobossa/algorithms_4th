import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CircularSuffixArray {
    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final int index;

        public CircularSuffix(int index) {
            this.index = index;
        }

        private int getLength() {
            return str.length();
        }

        private char charAt(int i) {
            return str.charAt((i + index) % str.length());
        }

        @Override
        public int compareTo(CircularSuffix o) {
            int i = 0;
            while (i < getLength() && i < o.getLength()) {
                if (charAt(i) < o.charAt(i)) {
                    return -1;
                } else if (charAt(i) > o.charAt(i)) {
                    return 1;
                }
                i++;
            }
            return getLength() - o.getLength();
        }
    }

    final private String str;
    final private int[] indexes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (null == s) {
            throw new IllegalArgumentException();
        }
        str = s;
        indexes = new int[str.length()];
        sort();
    }

    // length of s
    public int length() {
        return str.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > str.length() - 1) {
            throw new IllegalArgumentException();
        }
        return indexes[i];
    }

    private void sort() {
        List<CircularSuffix> l = new LinkedList<>();
        int i = 0;
        for (i = 0; i < str.length(); i++) {
            l.add(new CircularSuffix(i));
        }
        Collections.sort(l);
        i = 0;
        for (CircularSuffix cs: l) {
            indexes[i++] = cs.index;
        }
        // chapter 5.1 LSD Radix sort / MSD Radix sort

//        int n = str.length();
//        CircularSuffix[] a = new CircularSuffix[n];
//        for (int i = 0; i < n; i++) {
//            a[i] = new CircularSuffix(i);
//        }
//
//        int R = 256;
//        CircularSuffix[] aux = new CircularSuffix[n];
//        for (int d = n - 1; d >= 0; d--) {
//            // sort by key-indexed counting on dth character
//
//            // compute frequency counts
//            int[] count = new int[R + 1];
//            for (int i = 0; i < n; i++) {
//                count[a[i].charAt(d) + 1]++;
//            }
//            // compute cumulates
//            for (int r = 0; r < R; r++) {
//                count[r + 1] += count[r];
//            }
//            // move data
//            for (int i = 0; i < n; i++) {
//                aux[count[a[i].charAt(d)]++] = a[i];
//            }
//            // copy back
//            for (int i = 0; i < n; i++) {
//                a[i] = aux[i];
//            }
//        }
//
//        for (int i = 0; i < n; i++) {
//            indexes[i] = a[i].index;
//        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++) {
            System.out.println(csa.index(i));
        }
    }
}
