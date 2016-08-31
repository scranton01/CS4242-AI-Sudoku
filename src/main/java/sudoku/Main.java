/**
 * CS4242 Artificial Intelligence
 * @Date 8/31/2016
 * @Author Jun Nguyen
 */
package sudoku;

import static sudoku.Utility.readTableCsv;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Sudoku Solver");

        System.out.println("type number from 1 to 5 to select the puzzle");
        System.out.println("1: easyPuzzle1");
        System.out.println("2: easyPuzzle2");
        System.out.println("3: easyPuzzle3");
        System.out.println("4: intermediatePuzzle1");
        System.out.println("5: intermediatePuzzle2");
        
        String csvFile = "C://Git/CS4242-AI-Sudoku/src/main/resources/easyPuzzle2.csv";
        Table table = readTableCsv(csvFile);
        table.printTable();
        table.solvePuzzle();
        table.printTable();
    }
}
