import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output

    private final static int BIT_INT = 8;
    public static void encode(){
        LinkedList<Character> alphabetInSequence = getLinkedListAlphabetInSequence();
        while(!BinaryStdIn.isEmpty()){
            char c = BinaryStdIn.readChar();
            for(int i = 0; i < alphabetInSequence.size(); i++){
                if(alphabetInSequence.get(i) == c){
                    alphabetInSequence.remove(i);
                    alphabetInSequence.addFirst(c);
                    BinaryStdOut.write(i,BIT_INT);
                    System.out.println(i);
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    private static LinkedList<Character> getLinkedListAlphabetInSequence(){
        Alphabet extendedASCII = Alphabet.EXTENDED_ASCII;
        LinkedList<Character> alphabetInSequence = new LinkedList<Character>();
        for(int i = 0; i < extendedASCII.radix(); i++){
            alphabetInSequence.add(extendedASCII.toChar(i));
        }
        return alphabetInSequence;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode(){
        LinkedList<Character> alphabetInSequence = getLinkedListAlphabetInSequence();
        while(!BinaryStdIn.isEmpty()){
            int index = BinaryStdIn.readInt(BIT_INT);
            char c = alphabetInSequence.remove(index);
            alphabetInSequence.addFirst(c);
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args){
        if(args[0].equals("-")){
            encode();
        } else if(args[0].equals("+")){
            decode();
        } else{
            throw new java.lang.IllegalArgumentException("Usage: input '+' for encoding or '-' for decoding");
        }
    }
}
