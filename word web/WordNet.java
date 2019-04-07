import edu.princeton.cs.algs4.Digraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeSet;

public class WordNet {
    private Digraph digraph;
    private ArrayList<Synset> synsets;
    private SAP sap;
    private TreeSet<Noun> nouns;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFile, String hypernymsFile){
        this.synsets = new ArrayList<Synset>();

        //read by line synsetsFile
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = (BufferedReader) Files.newBufferedReader(Paths.get(synsetsFile), charset)) {
            String synsetLine = null;
            while ((synsetLine = reader.readLine()) != null) {
                String[] synsetLineElem = synsetLine.split(",");
                String[] synonums = synsetLineElem[1].split(" ");
                ArrayList<String> arraySynonums = new ArrayList<>();
                for(String synonum: synonums){
                    arraySynonums.add(synonum);
                }
                Synset synset = new Synset(Integer.parseInt(synsetLineElem[0]), arraySynonums, synsetLineElem[2]);
                this.synsets.add(Integer.parseInt(synsetLineElem[0]), synset);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        };

        nouns = new TreeSet<Noun>(Noun.Comparator);

        synsets.forEach(synset -> {
            synset.getSynonums().forEach(nounFromSet -> {
                Noun noun = new Noun(synset.getId(), nounFromSet, synset.getDefinition());
                this.nouns.add(noun);
            });
        });

        digraph = new Digraph(this.synsets.size());

        try (BufferedReader reader = (BufferedReader) Files.newBufferedReader(Paths.get(hypernymsFile), charset)) {
            String hypernymLine = null;
            while ((hypernymLine = reader.readLine()) != null) {
                String[] hypernymLineElem = hypernymLine.split(",");
                String hyponym = hypernymLineElem[0];
                for(int i = 1; i < hypernymLineElem.length; i++){
                    digraph.addEdge(Integer.parseInt(hyponym), Integer.parseInt(hypernymLineElem[i]));
                }
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        sap = new SAP(digraph);

    }

    // returns all WordNet nouns
    public Iterable<String> nouns(){
        ArrayList<String> arrayNouns = new ArrayList<String>();
        this.nouns.iterator().forEachRemaining(noun -> {
            arrayNouns.add(noun.getNoun());
        });

        return arrayNouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        Noun noun = new Noun(word);

        return nouns.contains(noun);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        Noun nounFirst = new Noun(nounA);
        nounFirst = nouns.ceiling(nounFirst);

        Noun nounSecond = new Noun(nounB);
        nounSecond = nouns.ceiling(nounSecond);

        return sap.length(nounFirst.getId(), nounSecond.getId());
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        Noun nounFirst = new Noun(nounA);
        nounFirst = nouns.ceiling(nounFirst);

        Noun nounSecond = new Noun(nounB);
        nounSecond = nouns.ceiling(nounSecond);

        return synsets.get(sap.ancestor(nounFirst.getId(), nounSecond.getId())).getSynonums().get(0);
    }

    // do unit testing of this class
    public static void main(String[] args){
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(wordNet.sap("zebra", "horse"));
        System.out.println(wordNet.distance("zebra", "horse"));
    }
}
