public class Noun {
    int id;
    String noun;
    String definition;

    public Noun(int id, String noun, String definition) {
        this.id = id;
        this.noun = noun;
        this.definition = definition;
    }

    public Noun(String noun) {
        this.noun = noun;
    }

    public String getNoun() {
        return noun;
    }

    public int getId() {
        return id;
    }

    public static java.util.Comparator<Noun> Comparator = new java.util.Comparator<Noun>() {
        public int compare(Noun i, Noun j){
            return i.noun.compareTo(j.noun);
        }
    };
}
