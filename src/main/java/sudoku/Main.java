package sudoku;

import java.util.stream.Collectors;

import static sudoku.Utility.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Sudoku Solver");


        String csvFile = "C://Git/CS4242-AI-Sudoku/src/main/resources/easyPuzzle1.csv";
        Table table = readTableCsv(csvFile);
        table.printTable();

        table.solvePuzzle();
        isOnlyCandidate(6,4,5,table);
        table.printTable();
    }
}
