import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Table {
    List<List<GridNumber>> table;

    Table() {
        table = new ArrayList<>();
    }

    String numToString(int i, int j) {
        return table.get(i).get(j).toString();
    }

    GridNumber get(int i, int j) {
        return table.get(i).get(j);
    }

    List<GridNumber> getRow(int i) {
        return table.get(i);
    }

    List<GridNumber> getColumn(int j) {
//        AtomicInteger index = new AtomicInteger();
        return table.stream()
                .map(row -> row.get(j))
                .collect(Collectors.toList());
    }

    List<GridNumber> getBox(int k) {
        switch (k){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
        }
        return null;
    }

    void add(List<GridNumber> row) {
        this.table.add(row);
    }

    void printTable() {
        printRow(0);
        printRow(1);
        printRow(2);
        System.out.println();
        printRow(3);
        printRow(4);
        printRow(5);
        System.out.println();
        printRow(6);
        printRow(7);
        printRow(8);

    }

    void printRow(int i) {
        System.out.println(numToString(i, 0) + numToString(i, 1) + numToString(i, 2) + "  " +
                numToString(i, 3) + numToString(i, 4) + numToString(i, 5) + "  " +
                numToString(i, 6) + numToString(i, 7) + numToString(i, 8));
    }

    void solvePuzzle() {

    }
}
