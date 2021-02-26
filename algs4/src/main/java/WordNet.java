import edu.princeton.cs.algs4.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class WordNet {
    public static final String FIELD_SEPARATOR = ",";
    public static final String NOUN_SEPARATOR = " ";

    final Map<String, Set<Integer>> nounMap;
    final Digraph digraph;
    final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("arguments are null.");
        }

        // read synsets file
        int count = 0;
        In inSynSets = new In(synsets);
        nounMap = new HashMap<>();
        while (inSynSets.hasNextLine()) {
            count++;
            String line = inSynSets.readLine();
            String[] fields = line.split(FIELD_SEPARATOR);
            Integer id = Integer.parseInt(fields[0]);
            String[] nouns = fields[1].split(NOUN_SEPARATOR);
            for (String noun : nouns) {
                if (!nounMap.containsKey(noun)) {
                    nounMap.put(noun, new TreeSet<>());
                }
                nounMap.get(noun).add(id);
            }
        }

        // read hypernyms
        In inHypernyms = new In(hypernyms);
        digraph = new Digraph(count);
        while (inHypernyms.hasNextLine()) {
            String line = inHypernyms.readLine();
            String[] ids = line.split(FIELD_SEPARATOR);
            int v = Integer.parseInt(ids[0]);
            for (int i = 1; i < ids.length; i++) {
                int w = Integer.parseInt(ids[i]);
                digraph.addEdge(v, w);
            }
        }

        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("arguments are null.");
        }
        return -1;
    }

    // a synset (second field of synsets.txt) that is
    // the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("arguments are null.");
        }
        return null;
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
