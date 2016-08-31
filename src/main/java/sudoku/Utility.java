package sudoku;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Utility {
    static Table readTableCsv(String csvFile) {
        Table table = new Table();
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] nextLine;
            AtomicInteger rowIndex = new AtomicInteger();
            while ((nextLine = reader.readNext()) != null) {
                List<GridNumber> row = new ArrayList<>();
                AtomicInteger columnIndex = new AtomicInteger();
                for (String value : nextLine) {
                    row.add(new GridNumber(Integer.parseInt(value), rowIndex.get(), columnIndex.getAndIncrement()));
                }
                rowIndex.getAndIncrement();
                table.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

//    static boolean tableIsCorrect(Table table) {
//        return IntStream.range(0, 9).allMatch(row -> containsUnique(table.getRow(row))) &&
//                IntStream.range(0, 9).allMatch(column -> containsUnique(table.getColumn(column))) &&
//                IntStream.range(0, 9).allMatch(box -> containsUnique(table.getBox(box)));
//    }

    static boolean containsUnique(List<GridNumber> list) {
        List<Integer> fixedList = list.stream()
                .filter(i -> i.getFixed() != 0)
                .map(i -> i.getFixed())
                .collect(Collectors.toList());
        Set<Integer> set = new HashSet<>();
        return fixedList.stream().allMatch(t -> set.add(t));
    }

//    static boolean isSolved(Table table) {
//        return table.getTable().stream()
//                .flatMap(num -> num.stream())
//                .map(GridNumber::getFixed)
//                .noneMatch(i -> i == 0) &&
//                tableIsCorrect(table);
//    }

    static boolean isOnlyCandidate(int candidate, int row, int column, Table table) {
       List<Integer> list = table.getRow(row).stream().map(num -> num.getCandidates()).flatMap(i -> i.stream()).collect(Collectors.toList());
        return table.getRow(row).stream()
                .map(num -> num.getCandidates())
                .flatMap(i -> i.stream())
                .noneMatch(i -> i == candidate) ||

                table.getColumn(column).stream()
                        .map(num -> num.getCandidates())
                        .flatMap(i -> i.stream())
                        .noneMatch(i -> i == candidate) ||

                table.getBox(getBoxNumberBy(row, column)).stream()
                        .map(num -> num.getCandidates())
                        .flatMap(i -> i.stream())
                        .noneMatch(i -> i == candidate);
    }
    static int getBoxNumberBy(int row, int column){
        return row/3*3 + column/3;
    }
}
