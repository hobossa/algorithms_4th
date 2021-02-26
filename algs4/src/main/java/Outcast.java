import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Outcast {
    final private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int nLen = nouns.length;
        int[] distances = new int[nLen];
        Arrays.fill(distances, 0);
        for (int i = 0; i < nLen; i++) {
            for (int j = 0; j < nLen; j++) {
                distances[i] += wordnet.distance(nouns[i], nouns[j]);
            }
        }
        int nMax = 0;
        for (int i = 1; i < nLen; i++) {
            if (distances[i] > distances[nMax]) {
                nMax = i;
            }
        }
        return nouns[nMax];
    }

    // see test client below
    public static void main(String[] args) {
        if (args.length == 0) {

        } else {
            WordNet wordnet = new WordNet(args[0], args[1]);
            Outcast outcast = new Outcast(wordnet);
            for (int t = 2; t < args.length; t++) {
                In in = new In(args[t]);
                String[] nouns = in.readAllStrings();
                StdOut.println(args[t] + ": " + outcast.outcast(nouns));
            }
        }
    }
}
