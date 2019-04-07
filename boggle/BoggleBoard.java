import edu.princeton.cs.algs4.TST;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class BoggleBoard {

    char[][] board;
    int rows, cols;

    // Initializes a random 4-by-4 Boggle board.
    // (by rolling the Hasbro dice)
    public BoggleBoard(){
        this(4,4);
    }

    // Initializes a random m-by-n Boggle board.
    // (using the frequency of letters in the English language)

    public BoggleBoard(int cols, int rows){

        this.rows = rows;
        this.cols = cols;

        board = new char[rows][];
        for(int i = 0; i < rows; i++){
            board[i] = new char[cols];
        }

        Random r = new Random();

        String alphabet = "QWERTYUIOPLKJHGFDSAZXCVBNM";
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++){
                board[i][j] = alphabet.charAt(r.nextInt(alphabet.length()));
            }
        } // prints 50 random characters from alphabet
    }

    // Initializes a Boggle board from the specified filename.
    public BoggleBoard(String filename){
        Charset charset = Charset.forName("US-ASCII");

        try (BufferedReader reader = (BufferedReader) Files.newBufferedReader(Paths.get(filename), charset)) {

            String rowsAndCols = reader.readLine();
            String[] arrRowsAndCols = rowsAndCols.split("\\s+");
            int rows = Integer.parseInt(arrRowsAndCols[0]);
            int cols = Integer.parseInt(arrRowsAndCols[1]);

            this.rows = rows;
            this.cols = cols;

            board = new char[rows][];
            for (int i = 0; i < rows; i++) {
                board[i] = new char[cols];
            }

            String lineRowsItem;
            int currRow = 0;

            while ((lineRowsItem = reader.readLine()) != null) {
                String[] arrRowsItem = lineRowsItem.split("\\s+");
                for(int col = 0; col < cols; col++){
                    board[currRow][col] = arrRowsItem[col].charAt(0);
                }
                currRow++;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        };
    }

    // Initializes a Boggle board from the 2d char array.
    // (with 'Q' representing the two-letter sequence "Qu")
    public BoggleBoard(char[][] a){
        board = a;
    }

    // Returns the number of rows.
    public int rows(){
        return rows;
    }

    // Returns the number of columns.
    public int cols(){
        return cols;
    }

    // Returns the letter in row i and column j.
    // (with 'Q' representing the two-letter sequence "Qu")
    public char getLetter(int i, int j){
        return board[i][j];
    }

    // Returns a string representation of the board.
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                stringBuilder.append(board[i][j] + " ");
            }

            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args){
        BoggleBoard board = new BoggleBoard();
        System.out.println(board.toString());
        System.out.println("");
        BoggleBoard board2 = new BoggleBoard(7,8);
        System.out.println(board2.toString());
        System.out.println("");
        BoggleBoard board3 = new BoggleBoard("board-q.txt");
        System.out.println(board3.toString());


        TST<String> tst = new TST<String>();
        tst.put("God", "God");
        System.out.println(tst.get("God"));
    }
}
