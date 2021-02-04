import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BinarySearch {
    public static int rank(int key, int[] a) {
        // Array must be sorted.
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;   // why not just use (lo+hi)/2
            if (key < a[mid]) {
                hi = mid - 1;
            } else if (key > a[mid]) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] whiteList = new In("tinyW.txt").readAllInts();
        Arrays.sort(whiteList);
        int[] testList = new In("tinyT.txt").readAllInts();
        for (int key : testList) {
            // Read key, print if not in whitelist.
            if (rank(key, whiteList) < 0) {
                StdOut.println(key);
            }
        }
    }
}
