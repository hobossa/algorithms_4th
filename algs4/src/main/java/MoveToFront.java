import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] chs = new char[256];
        for (char i = 0; i < 256; i++) {
            chs[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char i = 0;
            for (i = 0; i < 256; i++) {
                if (chs[i] == c) {
                    BinaryStdOut.write(i);
                    break;
                }
            }
            while (i > 0) {
                chs[i] = chs[--i];
            }
            chs[0] = c;
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chs = new char[256];
        for (char i = 0; i < 256; i++) {
            chs[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar();
            char c = chs[i];
            BinaryStdOut.write(c);
            while (i > 0) {
                chs[i] = chs[--i];
            }
            chs[0] = c;
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
//        FileInputStream is = null;
//        try {
//            is = new FileInputStream(new File("./testData/abra.txt"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        System.setIn(is);

        if ("-".compareTo(args[0]) == 0) {
            encode();
        }
        if ("+".compareTo(args[0]) == 0) {
            decode();
        }
    }
}
