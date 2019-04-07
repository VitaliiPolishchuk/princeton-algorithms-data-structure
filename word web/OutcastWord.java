public class OutcastWord {
    String word;
    int distance;

    public OutcastWord(String word, int distance) {
        this.word = word;
        this.distance = distance;
    }

    public static java.util.Comparator<OutcastWord> Comparator = new java.util.Comparator<OutcastWord>() {
        public int compare(OutcastWord i, OutcastWord j){
            if(i.distance > j.distance){
                return 1;
            } else if(i.distance < j.distance){
                return -1;
            } else {
                return 0;
            }
        }
    };

    public String getWord() {
        return word;
    }
}
