import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Sudoku Solver");
        Table table = new Table();

        String csvFile = "C://Git/CS4242-AI-Sudoku/src//main/resources/easyPuzzle1.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] nextLine;
            AtomicInteger rowIndex = new AtomicInteger();
            while ((nextLine = reader.readNext()) != null) {

                List<GridNumber> row = new ArrayList<>();
                AtomicInteger columnIndex = new AtomicInteger();
                for(String value: nextLine){

                    row.add(new GridNumber(Integer.parseInt(value),rowIndex.get(), columnIndex.getAndIncrement()));
                }
                rowIndex.getAndIncrement();
                table.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        table.printTable();




    }
}
