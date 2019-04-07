import edu.princeton.cs.algs4.MSD;
import edu.princeton.cs.algs4.TST;

public class CircularSuffixArray {
    int[] circularSuffix;

    public CircularSuffixArray(String s)    // circular suffix array of s
    {
        circularSuffix = new int[s.length()];
        TST<String> unsortedArrayCircularSuffix = new TST<String>();
        TST<String> sortedArrayCircularSuffix = new TST<String>();

        String[] arrCircularSuffix = new String[s.length()];
        for(int i = 0; i < s.length(); i++){
            StringBuilder firstPart = new StringBuilder(s.substring(0,i));
            StringBuilder secondPart = new StringBuilder((s.substring(i,s.length())));
            secondPart.append(firstPart);
            arrCircularSuffix[i] = secondPart.toString();
        }

        for(int i = 0; i < arrCircularSuffix.length; i++){
            unsortedArrayCircularSuffix.put(Integer.toString(i) ,arrCircularSuffix[i]);
        }
        //sort it
        MSD.sort(arrCircularSuffix);

        for(int i = 0; i < arrCircularSuffix.length; i++){
            sortedArrayCircularSuffix.put(arrCircularSuffix[i], Integer.toString(i));
        }

        for(int i = 0; i < unsortedArrayCircularSuffix.size(); i++){
            String suffix = unsortedArrayCircularSuffix.get(Integer.toString(i));

            String index = sortedArrayCircularSuffix.get(suffix);

            circularSuffix[i] = Integer.parseInt(index);
        }
    }
    public int length()                     // length of s
    {
        return circularSuffix.length;
    }

    public int index(int i)                 // returns index of ith sorted suffix
    {
        return circularSuffix[i];
    }
    public static void main(String[] args)  // unit testing (required)
    {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");

        System.out.println(circularSuffixArray.index(9));
    }
}
