/**
 * CS4242 Artificial Intelligence
 * @Date 8/31/2016
 * @Author Jun Nguyen
 */
package sudoku;

import java.util.Scanner;

import static sudoku.Utility.readTableCsv;

public class Main {
    public static void main(String[] args) {
        printBanner();
        String input;
        A: while(true) {
            System.out.println("type number from 1 to 5 to select the puzzle, or type 'q' to quit this program");
            System.out.println("1: easyPuzzle1");
            System.out.println("2: easyPuzzle2");
            System.out.println("3: easyPuzzle3");
            System.out.println("4: easyPuzzle4");
            System.out.println("5: easyPuzzle5");
            System.out.println("6: mediumPuzzle1");
            System.out.println("7: mediumPuzzle2");
            Scanner in = new Scanner(System.in);
            input = in.next();
            String csvFile ="";
            switch(input){
                case "1":
                    csvFile = "easyPuzzle1.csv";
                    break;
                case "2":
                    csvFile = "easyPuzzle2.csv";
                    break;
                case "3":
                    csvFile = "easyPuzzle3.csv";
                    break;
                case "4":
                    csvFile = "easyPuzzle4.csv";
                    break;
                case "5":
                    csvFile = "easyPuzzle5.csv";
                    break;
                case "6":
                    csvFile = "mediumPuzzle1.csv";
                    break;
                case "7":
                    csvFile = "mediumPuzzle2.csv";
                    break;
                case "q":
                    break A;
            }
            if(csvFile ==""){
                System.out.println("Invalid input, try again");
            }
            else {
                Table table = readTableCsv(csvFile);
                table.printTable();
                table.solvePuzzle();
                table.printTable();
            }
        }
    }

    static void printBanner(){
        System.out.println("\n" +
                "  _________         .___      __          \n" +
                " /   _____/__ __  __| _/____ |  | ____ __ \n" +
                " \\_____  \\|  |  \\/ __ |/  _ \\|  |/ /  |  \\\n" +
                " /        \\  |  / /_/ (  <_> )    <|  |  /\n" +
                "/_______  /____/\\____ |\\____/|__|_ \\____/ \n" +
                "        \\/           \\/           \\/      \n" +
                "  _________      .__                     \n" +
                " /   _____/ ____ |  |___  __ ___________ \n" +
                " \\_____  \\ /  _ \\|  |\\  \\/ // __ \\_  __ \\\n" +
                " /        (  <_> )  |_\\   /\\  ___/|  | \\/\n" +
                "/_______  /\\____/|____/\\_/  \\___  >__|   \n" +
                "        \\/                      \\/         ver1.2");
    }
}
