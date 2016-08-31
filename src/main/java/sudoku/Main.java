package sudoku;

import static sudoku.Utility.isOnlyCandidate;
import static sudoku.Utility.readTableCsv;
/**
 * CS4242 Artificial Inteligence
 * @Date 8/31/2016
 * @Author Jun Nguyen
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Sudoku Solver");


        String csvFile = "C://Git/CS4242-AI-Sudoku/src/main/resources/easyPuzzle3.csv";
        Table table = readTableCsv(csvFile);
        table.printTable();

        table.solvePuzzle();
        isOnlyCandidate(6,4,5,table);
        table.printTable();
    }
}
