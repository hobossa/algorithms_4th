import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class BurrowsWheeler {

    // reading from standard input and writing to standard output
    public static void transform() {
        String str = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(str);
        int n = str.length();
        for (int i = 0; i < n; i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(str.charAt((csa.index(i) + n - 1) % n));
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        char[] str = t.toCharArray();
        Arrays.sort(str);

        // construct next[] array
        int [] mark = new int[t.length()];
        for (int i = 0; i < t.length(); i++) {
            mark[i] = 0;
        }
        int [] next = new int[t.length()];
        int preJ = -1;
        for (int i = 0; i < t.length(); i++) {
            int start = 0;
            if (i > 0 && str[i] == str[i-1]) {
                start = preJ + 1;
            }
            for (int j = start; j < t.length(); j++) {
                if (str[i] == t.charAt(j) && mark[j] == 0) {
                    mark[j] = 1;
                    next[i] = j;
                    preJ = j;
                    break;
                }
            }
        }

        for (int i = 0; i < t.length(); i++) {
            char c = str[first];
            BinaryStdOut.write(c);
            first = next[first];
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
       // FileInputStream is = null;
       // try {
       //     is = new FileInputStream(new File("./testData/abra.txt.bwt"));
       // } catch (FileNotFoundException e) {
       //     e.printStackTrace();
       // }
       // System.setIn(is);

        if ("-".compareTo(args[0]) == 0) {
            transform();
        }
        if ("+".compareTo(args[0]) == 0) {
            inverseTransform();
        }
    }
}
