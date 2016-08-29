package sudoku;

import static sudoku.Utility.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Sudoku Solver");
        Table table = new Table();

        String csvFile = "C://Git/CS4242-AI-Sudoku/src/main/resources/easyPuzzle1.csv";
        table = readTableCsv(csvFile);
        table.printTable();

        System.out.println(containsUnique(table.getRow(0)));
        System.out.println(tableIsCorrect(table));

    }
}
