import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    public static void main (String [] args){
        System.out.println("hello world");

        String csvFile = "C://Git/CS4242-AI-Sudoku/src//main/resources/easyPuzzle1.csv";
        String line;
        String csvSplitBy = ",";

        try(Stream<String> stream = Files.lines(Paths.get(csvFile))){
            stream.forEach(System.out::println);
        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
