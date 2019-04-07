import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class BoggleSolver
{
    TST<String> dictionary;
    BoggleBoard board;
    Set<String> validWords;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        this.dictionary = new TST<String>();

        for(String word: dictionary){
            this.dictionary.put(word, word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        this.board = board;
        this.validWords = new HashSet<String>();
        for (int i = 0; i < board.rows(); i++) {
            for(int j = 0; j < board.cols(); j++){
                Square sq = new Square(i, j);
                StringBuilder word = new StringBuilder();
                word.append(sq.getLetter());
                solver(sq, word);
            }
        }

        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        int len = word.length();
        if(len > 8) return 11;
        else if(len >= 7) return 5;
        else if(len >= 6) return 3;
        else if(len >= 5) return 2;
        else if(len >= 3) return 1;
        else return 0;
    }

    private void solver(Square sq, StringBuilder word){
        for(Square neighborn: sq.getNeighborns()){
            StringBuilder newWord = new StringBuilder(word);
            newWord.append(neighborn.getLetter());
            if(wordValidity(newWord.toString())){
                validWords.add(newWord.toString());
            }
            solver(neighborn,newWord);
        }
    }

    private boolean wordValidity(String word){
        if(word.length() >= 3 && dictionary.contains(word))
            return true;
        return false;
    }

    private class Square{
        private int row, col;
        private char letter;
        boolean[][] boardAvailableNeighborns;
        ArrayList<Square> neighborns;

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public Character getLetter() {
            return letter;
        }

        Square(int row, int col){
            this.row = row;
            this.col = col;
            this.letter = board.getLetter(row, col);
            boardAvailableNeighborns = new boolean[board.rows()][];
            for(int i = 0; i < board.rows(); i++){
                boardAvailableNeighborns[i] = new boolean[board.cols()];
            }

            for (int i = 0; i < board.rows(); i++) {
                for(int j = 0; j < board.cols(); j++){
                    boardAvailableNeighborns[i][j] = true;
                }
            }

            boardAvailableNeighborns[row][col] = false;
        }

        Square(int row, int col, Square sq){
            this(row,col);

            for (int i = 0; i < board.rows(); i++) {
                for(int j = 0; j < board.cols(); j++){
                    boardAvailableNeighborns[i][j] = sq.boardAvailableNeighborns[i][j];
                }
            }

            boardAvailableNeighborns[row][col] = false;
        }

        public Iterable<Square> getNeighborns(){
            neighborns = new ArrayList<Square>();

            if(row - 1 >= 0){
                addNeighBornsInRow(row - 1);
            }

            addNeighBornsInRow(row);

            if(row + 1 < board.rows()){
                addNeighBornsInRow(row + 1);
            }

            return neighborns;

        }

        private void addNeighBornsInRow(int row){
            if(col - 1 >= 0 && boardAvailableNeighborns[row][col - 1]){
                addNeighborn(row, col - 1);
            }
            if(boardAvailableNeighborns[row][col]){
                addNeighborn(row, col);
            }
            if(col + 1 < board.cols() && boardAvailableNeighborns[row][col + 1]){
                addNeighborn(row, col + 1);
            }
        }

        private void addNeighborn(int row, int col){
            Square neighborn = new Square(row,col, this);
            neighborn.boardAvailableNeighborns[row][col] = false;
            neighborns.add(neighborn);
        }
    }

    public static void main(String[] args) {
        In in = new In("dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board4x4.txt");
        int score = 0;
        int counter = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            StdOut.println(counter++);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}