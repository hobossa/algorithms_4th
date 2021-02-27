import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class WordNet {
    final private static String FIELD_SEPARATOR = ",";
    final private static String NOUN_SEPARATOR = " ";

    final private Map<String, Set<Integer>> nounMap;
    final private Map<Integer, String> nounSet;
    final private Digraph digraph;
    final private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("arguments are null.");
        }

        // read synsets file
        int count = 0;
        In inSynSets = new In(synsets);
        nounMap = new HashMap<>();
        nounSet = new TreeMap<>();
        while (inSynSets.hasNextLine()) {
            count++;
            String line = inSynSets.readLine();
            String[] fields = line.split(FIELD_SEPARATOR);
            Integer id = Integer.parseInt(fields[0]);
            nounSet.put(id, fields[1]);
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
        return sap.length(nounMap.get(nounA), nounMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is
    // the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("arguments are null.");
        }
        int ancestor = sap.ancestor(nounMap.get(nounA), nounMap.get(nounB));
        return nounSet.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
