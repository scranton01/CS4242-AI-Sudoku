/**
 * CS4242 Artificial Intelligence
 * @Date 8/31/2016
 * @Author Jun Nguyen
 */
package sudoku;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static sudoku.Table.deepCopy;


class Utility {
    static Table readTableCsv(String csvFile) {
        Table table = new Table();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (CSVReader reader = new CSVReader(new InputStreamReader(classLoader.getResourceAsStream(csvFile)))) {
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


    static boolean containsUnique(List<GridNumber> list) {
        List<Integer> fixedList = list.stream()
                .filter(i -> i.getFixed() != 0)
                .map(i -> i.getFixed())
                .collect(Collectors.toList());
        Set<Integer> set = new HashSet<>();
        return fixedList.stream().allMatch(t -> set.add(t));
    }

    static boolean isOnlyCandidate(int candidate, int row, int column, Table table) {
        Table newTable = deepCopy(table);
        newTable.get(row, column).getCandidates().clear();
        return newTable.getRow(row).stream()
                .map(num -> num.getCandidates())
                .flatMap(i -> i.stream())
                .noneMatch(i -> i == candidate) ||

                newTable.getColumn(column).stream()
                        .map(num -> num.getCandidates())
                        .flatMap(i -> i.stream())
                        .noneMatch(i -> i == candidate) ||

                newTable.getBox(getBoxNumberBy(row, column)).stream()
                        .map(num -> num.getCandidates())
                        .flatMap(i -> i.stream())
                        .noneMatch(i -> i == candidate);
    }

    static int getBoxNumberBy(int row, int column) {
        return row / 3 * 3 + column / 3;
    }

}
