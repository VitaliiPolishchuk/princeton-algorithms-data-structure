import java.util.ArrayList;

public class Synset {
    int id;
    ArrayList<String> synonums;
    String definition;

    public Synset(int id, ArrayList<String> synonums, String definition) {
        this.id = id;
        this.synonums = synonums;
        this.definition = definition;
    }

    public int getId() {
        return id;
    }

    public String getDefinition() {
        return definition;
    }

    public ArrayList<String> getSynonums() {
        return synonums;
    }
}
