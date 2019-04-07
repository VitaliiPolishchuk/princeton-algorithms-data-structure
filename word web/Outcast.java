import edu.princeton.cs.algs4.MaxPQ;

public class Outcast {
    WordNet wordNet;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        this.wordNet = wordnet;
    }
    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        MaxPQ<OutcastWord> maxPQOutcastWord = new MaxPQ<OutcastWord>(OutcastWord.Comparator);
        for(int i = 0; i < nouns.length; i++){

            int distance = 0;

            for(int j = 0; j < nouns.length; j++){
                if(i == j){
                    continue;
                }

                distance += wordNet.distance(nouns[i], nouns[j]);
            }

            maxPQOutcastWord.insert(new OutcastWord(nouns[i], distance));
        }

        return maxPQOutcastWord.delMax().getWord();
    }
    public static void main(String[] args)  // see test client below
    {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        String [] nouns = "horse zebra cat bear table".split(" ");
        System.out.println(outcast.outcast(nouns));
    }
}