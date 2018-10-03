import java.util.Comparator;

public class AncestorAndLenght {
    int ancestor, length;

    public AncestorAndLenght(int ancestor, int length){
        this.ancestor = ancestor;
        this.length = length;
    }

    public int getAncestor() {
        return ancestor;
    }

    public int getLength() {
        return length;
    }

    public static java.util.Comparator<AncestorAndLenght> Comparator = new Comparator<AncestorAndLenght>() {
        public int compare(AncestorAndLenght i, AncestorAndLenght j){

            if(i.length > j.length){
                return 1;
            }
            else if(i.length < j.length){
                return -1;
            }
            return 0;
        }
    };
}
