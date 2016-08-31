package sudoku;

import static sudoku.Utility.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Sudoku Solver");


        String csvFile = "C://Git/CS4242-AI-Sudoku/src/main/resources/test.csv";
        Table table = readTableCsv(csvFile);
        table.printTable();

        System.out.println(containsUnique(table.getRow(0)));
        System.out.println(table.isCorrect());
        System.out.println(table.isSolved());
        table.solvePuzzle();

        System.out.println(isOnlyCandidate(3,1,1,table));
    }
}
