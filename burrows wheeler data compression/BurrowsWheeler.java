import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.MSD;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform(){
        String s = BinaryStdIn.readString();

        String[] arrCircularSuffix = new String[s.length()];
        for(int i = 0; i < s.length(); i++){
            StringBuilder firstPart = new StringBuilder(s.substring(0,i));
            StringBuilder secondPart = new StringBuilder((s.substring(i,s.length())));
            secondPart.append(firstPart);
            arrCircularSuffix[i] = secondPart.toString();
        }
        //sort it
        MSD.sort(arrCircularSuffix);

        StringBuilder t = new StringBuilder();

        for(int i = 0; i < arrCircularSuffix.length; i++){
            t.append(arrCircularSuffix[i].charAt(arrCircularSuffix.length - 1));
        }

        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        System.out.println(circularSuffixArray.index(0));
        System.out.println(t);
        BinaryStdOut.write(circularSuffixArray.index(0));
        BinaryStdOut.write(t.toString());

    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform(){
//        int index = 0;
//        String s = BinaryStdIn.readString();
//
//        String[] arrCircularSuffix = new String[s.length()];
//        for(int i = 0; i < s.length(); i++){
//            StringBuilder firstPart = new StringBuilder(s.substring(0,i));
//            StringBuilder secondPart = new StringBuilder((s.substring(i,s.length())));
//            secondPart.append(firstPart);
//            arrCircularSuffix[i] = secondPart.toString();
//        }
//        //sort it
//        MSD.sort(arrCircularSuffix);
//
//        for(String str: arrCircularSuffix){
//            System.out.println(str);
//        }

//        System.out.println(arrCircularSuffix[index]);
//        BinaryStdOut.write(arrCircularSuffix[index]);

        // take first, t[] from input
        int first = 3;
        String chars = BinaryStdIn.readString();
        char[] t = chars.toCharArray();
        chars = null;
        // construct next[]
        int next[] = new int[t.length];
        // Algorithm: Brute Force requires O(n^2) =>
        // go through t, consider t as key remember positions of t's in the Queue
        Map<Character, Queue<Integer>> positions = new HashMap<Character, Queue<Integer>>();
        for (int i = 0; i < t.length; i++) {
            if(!positions.containsKey(t[i]))
                positions.put(t[i], new Queue<Integer>());
            positions.get(t[i]).enqueue(i);
        }
        // get first chars array
        Arrays.sort(t);
        // go consistently through sorted firstChars array
        for (int i = 0; i < t.length; i++) {
            next[i] = positions.get(t[i]).dequeue();
        }
        // decode msg
        // for length of the msg
        for (int i = 0, curRow = first; i < t.length; i++, curRow = next[curRow])
            // go from first to next.
            BinaryStdOut.write(t[curRow]);
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args){
        if (args.length == 0)
            throw new java.lang.IllegalArgumentException("Usage: input '+' for encoding or '-' for decoding");
        if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
        else
            throw new java.lang.IllegalArgumentException("Usage: input '+' for encoding or '-' for decoding");
    }
}
