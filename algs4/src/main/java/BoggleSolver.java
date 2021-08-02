import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;


public class BoggleSolver {
    final private TrieSETEx dic;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dic = new TrieSETEx();
        for (String word : dictionary) {
            dic.add(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TreeSet<String> words = new TreeSet<>();
        int rows = board.rows();
        int cols = board.cols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buildWordsStartFrom(board, words, i, j);
            }
        }
        return words;
    }

    private void buildWordsStartFrom(BoggleBoard board, TreeSet<String> words, int i, int j) {
        StringBuilder strTemp = new StringBuilder();
        int rows = board.rows();
        int cols = board.cols();
        int[][] mark = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                mark[r][c] = 0;
            }
        }
        dfsWalk(board, words, strTemp, mark, i, j);
    }

    private void dfsWalk(BoggleBoard board, TreeSet<String> words, StringBuilder strBld, int[][] mark,
                         int i, int j) {
        mark[i][j] = 1;
        char c = board.getLetter(i, j);
        if (c == 'Q') {
            strBld.append("QU");
        } else {
            strBld.append(c);
        }

        String strWord = strBld.toString();

        int ki = dic.getKeyInfo(strWord);

        if (0 == ki) {
            if (c == 'Q') {
                strBld.delete(strBld.length() - 2, strBld.length());
            } else {
                strBld.delete(strBld.length() - 1, strBld.length());
            }
            mark[i][j] = 0;
            return;
        }

        if (strWord.length() > 2 && 1 == ki) {
            words.add(strBld.toString());
        }

        if (checkIJ(board, i - 1, j - 1) && mark[i - 1][j - 1] == 0) {
            dfsWalk(board, words, strBld, mark, i - 1, j - 1);
        }

        if (checkIJ(board, i - 1, j) && mark[i - 1][j] == 0) {
            dfsWalk(board, words, strBld, mark, i - 1, j);
        }

        if (checkIJ(board, i - 1, j + 1) && mark[i - 1][j + 1] == 0) {
            dfsWalk(board, words, strBld, mark, i - 1, j + 1);
        }

        if (checkIJ(board, i, j - 1) && mark[i][j - 1] == 0) {
            dfsWalk(board, words, strBld, mark, i, j - 1);
        }

        if (checkIJ(board, i, j + 1) && mark[i][j + 1] == 0) {
            dfsWalk(board, words, strBld, mark, i, j + 1);
        }

        if (checkIJ(board, i + 1, j - 1) && mark[i + 1][j - 1] == 0) {
            dfsWalk(board, words, strBld, mark, i + 1, j - 1);
        }

        if (checkIJ(board, i + 1, j) && mark[i + 1][j] == 0) {
            dfsWalk(board, words, strBld, mark, i + 1, j);
        }

        if (checkIJ(board, i + 1, j + 1) && mark[i + 1][j + 1] == 0) {
            dfsWalk(board, words, strBld, mark, i + 1, j + 1);
        }

        if (c == 'Q') {
            strBld.delete(strBld.length() - 2, strBld.length());
        } else {
            strBld.delete(strBld.length() - 1, strBld.length());
        }

        mark[i][j] = 0;
    }

    private boolean checkIJ(BoggleBoard board, int i, int j) {
        return i >= 0 && i < board.rows() && j >= 0 && j < board.cols();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dic.contains(word)) {
            return 0;
        }
        switch (word.length()) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11; // 8+
        }
    }


    public static void main(String[] args) {
        In in = new In("./boggle/dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("./boggle/board-rotavator.txt");
        // StdOut.println(board.getLetter(2, 1));
        int score = 0;
        StdOut.println("Words:");
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);

        // initialize a 4-by-4 board from a 2d char array
        /*
        StdOut.println("4-by-4 board from 2D character array:");
        char[][] a = {
                {'D', 'O', 'T', 'Y'},
                {'T', 'R', 'S', 'F'},
                {'M', 'X', 'M', 'O'},
                {'Z', 'A', 'B', 'W'}
        };
        BoggleBoard board = new BoggleBoard(a);
        StdOut.println(board);
        StdOut.println();
        TreeSet<String> words = new TreeSet<>();
        String [] dic = new String[1];
        dic[0] = "ABC";
        BoggleSolver slv = new BoggleSolver(dic);
        slv.BuildWordsStartFrom(board, words, 0, 0);
        int c = 0;
        for (String word : words) {
            if (word.length() == 3) {
                StdOut.println(word);
                c++;
                if (c > 10) {
                    // break;
                }
            }
        }
        */
    }
}
