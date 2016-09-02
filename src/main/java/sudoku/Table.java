/**
 * CS4242 Artificial Intelligence
 *
 * @Date 8/31/2016
 * @Author Jun Nguyen
 */
package sudoku;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static sudoku.Utility.*;

@Data
public class Table {
    List<List<GridNumber>> table;

    Table() {
        table = new ArrayList<>();
    }

    public static Table deepCopy(Table original) {
        Table table = new Table();
        for (int rowPos = 0; rowPos < 9; rowPos++) {
            List<GridNumber> row = new ArrayList<>();
            for (int columnPos = 0; columnPos < 9; columnPos++) {
                List<Integer> candidates = new ArrayList<>();
                for (int i = 0; i < original.get(rowPos, columnPos).getCandidates().size(); i++) {
                    candidates.add(original.get(rowPos, columnPos).getCandidates().get(i));
                }
                GridNumber gridNumber = new GridNumber(original.get(rowPos, columnPos).getFixed(), rowPos, columnPos);
                gridNumber.setCandidates(candidates);
                row.add(gridNumber);
            }
            table.add(row);
        }
        return table;
    }

    String numToString(int i, int j) {
        return table.get(i).get(j).toString();
    }

    GridNumber get(int row, int column) {
        return table.get(row).get(column);
    }

    List<GridNumber> getRow(int row) {
        return table.get(row);
    }

    List<GridNumber> getColumn(int j) {
        return table.stream()
                .map(row -> row.get(j))
                .collect(Collectors.toList());
    }

    List<GridNumber> getBox(int box) {
        switch (box) {
            case 0:
                return table.stream().flatMap(num -> num.stream()
                        .filter(i -> 0 <= i.getRowPos() && i.getRowPos() <= 2 && 0 <= i.getColumnPos() && i.getColumnPos() <= 2))
                        .collect(Collectors.toList());
            case 1:
                return table.stream().flatMap(num -> num.stream()
                        .filter(i -> 0 <= i.getRowPos() && i.getRowPos() <= 2 && 3 <= i.getColumnPos() && i.getColumnPos() <= 5))
                        .collect(Collectors.toList());
            case 2:
                return table.stream().flatMap(num -> num.stream()
                        .filter(i -> 0 <= i.getRowPos() && i.getRowPos() <= 2 && 6 <= i.getColumnPos() && i.getColumnPos() <= 8))
                        .collect(Collectors.toList());
            case 3:
                return table.stream().flatMap(num -> num.stream()
                        .filter(i -> 3 <= i.getRowPos() && i.getRowPos() <= 5 && 0 <= i.getColumnPos() && i.getColumnPos() <= 2))
                        .collect(Collectors.toList());
            case 4:
                return table.stream().flatMap(num -> num.stream()
                        .filter(i -> 3 <= i.getRowPos() && i.getRowPos() <= 5 && 3 <= i.getColumnPos() && i.getColumnPos() <= 5))
                        .collect(Collectors.toList());
            case 5:
                return table.stream().flatMap(num -> num.stream()
                        .filter(i -> 3 <= i.getRowPos() && i.getRowPos() <= 5 && 6 <= i.getColumnPos() && i.getColumnPos() <= 8))
                        .collect(Collectors.toList());
            case 6:
                return table.stream().flatMap(num -> num.stream()
                        .filter(i -> 6 <= i.getRowPos() && i.getRowPos() <= 8 && 0 <= i.getColumnPos() && i.getColumnPos() <= 2))
                        .collect(Collectors.toList());
            case 7:
                return table.stream().flatMap(num -> num.stream()
                        .filter(i -> 6 <= i.getRowPos() && i.getRowPos() <= 8 && 3 <= i.getColumnPos() && i.getColumnPos() <= 5))
                        .collect(Collectors.toList());
            case 8:
                return table.stream().flatMap(num -> num.stream()
                        .filter(i -> 6 <= i.getRowPos() && i.getRowPos() <= 8 && 6 <= i.getColumnPos() && i.getColumnPos() <= 8))
                        .collect(Collectors.toList());
        }
        return new ArrayList<>();
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
        System.out.println("===============================");
    }

    void printRow(int i) {
        System.out.println(numToString(i, 0) + numToString(i, 1) + numToString(i, 2) + "  " +
                numToString(i, 3) + numToString(i, 4) + numToString(i, 5) + "  " +
                numToString(i, 6) + numToString(i, 7) + numToString(i, 8));
    }

    boolean isValid() {
        return IntStream.range(0, 9).allMatch(row -> containsUnique(getRow(row))) &&
                IntStream.range(0, 9).allMatch(column -> containsUnique(getColumn(column))) &&
                IntStream.range(0, 9).allMatch(box -> containsUnique(getBox(box)));
    }

    boolean isSolved() {
        return table.stream()
                .flatMap(List::stream)
                .map(GridNumber::getFixed)
                .noneMatch(i -> i == 0) &&
                isValid();
    }


    void solvePuzzle() {
        AtomicInteger candidate = new AtomicInteger();
        int initialSize;
        AtomicInteger inserted = new AtomicInteger();

        while (!isSolved()) {
            initialSize = findFixedSize();
            candidate.set(1);
            while (candidate.get() <= 9) {
                for (int row = 0; row < 9; row++) {
                    for (int column = 0; column < 9; column++) {
                        if (get(row, column).getFixed() == 0 &&
                                getRow(row).stream().map(GridNumber::getFixed).noneMatch(fixed -> fixed == candidate.get()) &&
                                getColumn(column).stream().map(GridNumber::getFixed).noneMatch(fixed -> fixed == candidate.get()) &&
                                getBox(getBoxNumberBy(row, column)).stream().map(GridNumber::getFixed).noneMatch(fixed -> fixed == candidate.get())) {
                            get(row, column).candidates.add(candidate.get());
                        }
                    }
                }
                candidate.getAndIncrement();
            }
//           added from ver 1.2
            updateCandidates();

            candidate.set(1);
            A:
            while (candidate.get() <= 9) {
                for (int row = 0; row < 9; row++) {
                    for (int column = 0; column < 9; column++) {
                        if (get(row, column).getFixed() == 0 &&
                                get(row, column).getCandidates().contains(candidate.get()) &&
                                get(row, column).getCandidates().size() == 1) {
                            get(row, column).setFixed(candidate.get());
                            inserted.incrementAndGet();
                            printTable();
                            break A;
                        }
                        if (get(row, column).getFixed() == 0 &&
                                get(row, column).getCandidates().contains(candidate.get()) &&
                                isOnlyCandidate(candidate.get(), row, column, this)) {
                            get(row, column).setFixed(candidate.get());
                            inserted.incrementAndGet();
                            printTable();
                            break A;
                        }
                    }
                }
                candidate.getAndIncrement();
            }
            if (initialSize == findFixedSize()) {
                System.out.println("Table is unsolvable");
                System.out.println(inserted.get() + " numbers has been inserted.");
                return;
            }
            deleteCandidates();
        }
        System.out.println("Table is solved");
        System.out.println(inserted.get() + " numbers has been inserted.");
    }

    void deleteCandidates() {
        table.stream().flatMap(List::stream).forEach(i -> i.candidates.clear());
    }

    int findFixedSize() {
        return table.stream()
                .flatMap(List::stream)
                .map(GridNumber::getFixed)
                .filter(i -> i != 0)
                .collect(Collectors.toList())
                .size();
    }

    void updateCandidates() {
        int initialCandidateSize;
        int currentCandidateSize;
        do {
            initialCandidateSize = findCandidateSize();
            AtomicInteger candidate = new AtomicInteger(1);
            while (candidate.get() <= 9) {
                for (int row = 0; row < 9; row++) {
                    for (int column = 0; column < 9; column++) {
                        if (get(row, column).getFixed() == 0) {
                            deleteCandidates(candidate.get(), row, column);
                        }
                    }
                }
                candidate.getAndIncrement();
            }
            currentCandidateSize = findCandidateSize();
        } while (initialCandidateSize > currentCandidateSize);
    }

    int findCandidateSize() {
        return table.stream()
                .flatMap(List::stream)
                .map(GridNumber::getCandidates)
                .flatMap(List::stream)
                .collect(Collectors.toList()).size();
    }

    void deleteCandidates(int candidate, int rowPos, int columnPos) {
        List<GridNumber> row = getRow(rowPos);
        List<GridNumber> column = getColumn(columnPos);
        int boxNumber = getBoxNumberBy(rowPos, columnPos);
        List<GridNumber> box = getBox(boxNumber);

        List<Integer> rowList1;
        List<Integer> rowList2;
        List<Integer> columnList1;
        List<Integer> columnList2;
        List<Integer> boxList;

        switch (boxNumber) {
            case 0:
                rowList1 = row.stream()
                        .filter(num -> num.getColumnPos() < 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                rowList2 = row.stream()
                        .filter(num -> num.getColumnPos() >= 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getRowPos() != rowPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(rowList1, candidate) >= 2 &&
                        !rowList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getRow(rowPos).stream()
                            .filter(num -> num.getColumnPos() >= 3 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }

                columnList1 = column.stream()
                        .filter(num -> num.getRowPos() < 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                columnList2 = column.stream()
                        .filter(num -> num.getRowPos() >= 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getColumnPos() != columnPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(columnList1, candidate) >= 2 &&
                        !columnList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getColumn(columnPos).stream()
                            .filter(num -> num.getRowPos() >= 3 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }
                break;
            case 1:
                rowList1 = row.stream()
                        .filter(num -> num.getColumnPos() >= 3 && num.getColumnPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                rowList2 = row.stream()
                        .filter(num -> num.getColumnPos() < 3 && num.getColumnPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getRowPos() != rowPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(rowList1, candidate) >= 2 &&
                        !rowList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getRow(rowPos).stream()
                            .filter(num -> num.getColumnPos() < 3 && num.getColumnPos() >= 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }

                columnList1 = column.stream()
                        .filter(num -> num.getRowPos() < 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                columnList2 = column.stream()
                        .filter(num -> num.getRowPos() >= 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getColumnPos() != columnPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(columnList1, candidate) >= 2 &&
                        !columnList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getColumn(columnPos).stream()
                            .filter(num -> num.getRowPos() >= 3 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }
                break;
            case 2:
                rowList1 = row.stream()
                        .filter(num -> num.getColumnPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                rowList2 = row.stream()
                        .filter(num -> num.getColumnPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getRowPos() != rowPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(rowList1, candidate) >= 2 &&
                        !rowList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getRow(rowPos).stream()
                            .filter(num -> num.getColumnPos() < 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }

                columnList1 = column.stream()
                        .filter(num -> num.getRowPos() < 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                columnList2 = column.stream()
                        .filter(num -> num.getRowPos() >= 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getColumnPos() != columnPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(columnList1, candidate) >= 2 &&
                        !columnList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getColumn(columnPos).stream()
                            .filter(num -> num.getRowPos() >= 3 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }
                break;
            case 3:
                rowList1 = row.stream()
                        .filter(num -> num.getColumnPos() < 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                rowList2 = row.stream()
                        .filter(num -> num.getColumnPos() >= 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getRowPos() != rowPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(rowList1, candidate) >= 2 &&
                        !rowList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getRow(rowPos).stream()
                            .filter(num -> num.getColumnPos() >= 3 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }

                columnList1 = column.stream()
                        .filter(num -> num.getRowPos() >= 3 && num.getRowPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                columnList2 = column.stream()
                        .filter(num -> num.getRowPos() < 3 && num.getRowPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getColumnPos() != columnPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(columnList1, candidate) >= 2 &&
                        !columnList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getColumn(columnPos).stream()
                            .filter(num -> num.getRowPos() < 3 && num.getRowPos() >= 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }
                break;
            case 4:
                rowList1 = row.stream()
                        .filter(num -> num.getColumnPos() >= 3 && num.getColumnPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                rowList2 = row.stream()
                        .filter(num -> num.getColumnPos() < 3 && num.getColumnPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getRowPos() != rowPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(rowList1, candidate) >= 2 &&
                        !rowList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getRow(rowPos).stream()
                            .filter(num -> num.getColumnPos() < 3 && num.getColumnPos() >= 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }

                columnList1 = column.stream()
                        .filter(num -> num.getRowPos() >= 3 && num.getRowPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                columnList2 = column.stream()
                        .filter(num -> num.getRowPos() < 3 && num.getRowPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getColumnPos() != columnPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(columnList1, candidate) >= 2 &&
                        !columnList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getColumn(columnPos).stream()
                            .filter(num -> num.getRowPos() < 3 && num.getRowPos() >= 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }
                break;
            case 5:
                rowList1 = row.stream()
                        .filter(num -> num.getColumnPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                rowList2 = row.stream()
                        .filter(num -> num.getColumnPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getRowPos() != rowPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(rowList1, candidate) >= 2 &&
                        !rowList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getRow(rowPos).stream()
                            .filter(num -> num.getColumnPos() < 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }

                columnList1 = column.stream()
                        .filter(num -> num.getRowPos() >= 3 && num.getRowPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                columnList2 = column.stream()
                        .filter(num -> num.getRowPos() < 3 && num.getRowPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getColumnPos() != columnPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(columnList1, candidate) >= 2 &&
                        !columnList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getColumn(columnPos).stream()
                            .filter(num -> num.getRowPos() < 3 && num.getRowPos() >= 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }
                break;
            case 6:
                rowList1 = row.stream()
                        .filter(num -> num.getColumnPos() < 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                rowList2 = row.stream()
                        .filter(num -> num.getColumnPos() >= 3)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getRowPos() != rowPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(rowList1, candidate) >= 2 &&
                        !rowList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getRow(rowPos).stream()
                            .filter(num -> num.getColumnPos() >= 3 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }

                columnList1 = column.stream()
                        .filter(num -> num.getRowPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                columnList2 = column.stream()
                        .filter(num -> num.getRowPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getColumnPos() != columnPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(columnList1, candidate) >= 2 &&
                        !columnList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getColumn(columnPos).stream()
                            .filter(num -> num.getRowPos() < 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }
                break;
            case 7:
                rowList1 = row.stream()
                        .filter(num -> num.getColumnPos() >= 3 && num.getColumnPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                rowList2 = row.stream()
                        .filter(num -> num.getColumnPos() < 3 && num.getColumnPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getRowPos() != rowPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(rowList1, candidate) >= 2 &&
                        !rowList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getRow(rowPos).stream()
                            .filter(num -> num.getColumnPos() < 3 && num.getColumnPos() >= 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }

                columnList1 = column.stream()
                        .filter(num -> num.getRowPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                columnList2 = column.stream()
                        .filter(num -> num.getRowPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getColumnPos() != columnPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(columnList1, candidate) >= 2 &&
                        !columnList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getColumn(columnPos).stream()
                            .filter(num -> num.getRowPos() < 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }
                break;
            case 8:
                rowList1 = row.stream()
                        .filter(num -> num.getColumnPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                rowList2 = row.stream()
                        .filter(num -> num.getColumnPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getRowPos() != rowPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(rowList1, candidate) >= 2 &&
                        !rowList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getRow(rowPos).stream()
                            .filter(num -> num.getColumnPos() < 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }

                columnList1 = column.stream()
                        .filter(num -> num.getRowPos() >= 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                columnList2 = column.stream()
                        .filter(num -> num.getRowPos() < 6)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                boxList = box.stream()
                        .filter(num -> num.getColumnPos() != columnPos)
                        .map(GridNumber::getCandidates)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                if (Collections.frequency(columnList1, candidate) >= 2 &&
                        !columnList2.contains(candidate) &&
                        !boxList.contains(candidate)) {
                    this.getColumn(columnPos).stream()
                            .filter(num -> num.getRowPos() < 6 && num.getFixed() == 0)
                            .forEach(num -> num.deleteFromCandidates(candidate));
                }
                break;
        }
    }
}