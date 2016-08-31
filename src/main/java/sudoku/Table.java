/**
 * CS4242 Artificial Intelligence
 * @Date 8/31/2016
 * @Author Jun Nguyen
 */
package sudoku;

import lombok.Data;

import java.util.ArrayList;
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
    public static Table deepCopy(Table original){
        Table table = new Table();
        for(int rowPos=0 ;rowPos<9;rowPos++){
            List<GridNumber> row = new ArrayList<>();
            for(int columnPos=0;columnPos<9;columnPos++){
                List<Integer> candidates = new ArrayList<>();
                for(int i =0; i<original.get(rowPos,columnPos).getCandidates().size();i++){
                    candidates.add(original.get(rowPos,columnPos).getCandidates().get(i));
                }
                GridNumber gridNumber = new GridNumber(original.get(rowPos,columnPos).getFixed(),rowPos,columnPos);
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

    boolean isCorrect() {
        return IntStream.range(0, 9).allMatch(row -> containsUnique(getRow(row))) &&
                IntStream.range(0, 9).allMatch(column -> containsUnique(getColumn(column))) &&
                IntStream.range(0, 9).allMatch(box -> containsUnique(getBox(box)));
    }

    boolean isSolved() {
        return table.stream()
                .flatMap(num -> num.stream())
                .map(GridNumber::getFixed)
                .noneMatch(i -> i == 0) &&
                isCorrect();
    }


    void solvePuzzle() {
        AtomicInteger candidate = new AtomicInteger();
        int initialSize;

        while (!isSolved()) {
            initialSize = findFixedSize();
            candidate.set(1);
            while (candidate.get() <= 9) {
                for (int row = 0; row < 9; row++) {
                    for (int column = 0; column < 9; column++) {
                        if (get(row, column).getFixed() == 0 &&
                                getRow(row).stream().map(num -> num.getFixed()).noneMatch(fixed -> fixed == candidate.get()) &&
                                getColumn(column).stream().map(num -> num.getFixed()).noneMatch(fixed -> fixed == candidate.get()) &&
                                getBox(getBoxNumberBy(row, column)).stream().map(num -> num.getFixed()).noneMatch(fixed -> fixed == candidate.get())) {
                            get(row, column).candidates.add(candidate.get());
                        }
                    }
                }
                candidate.getAndIncrement();
            }

            candidate.set(1);
            A: while (candidate.get() <= 9) {
                for (int row = 0; row < 9; row++) {
                    for (int column = 0; column < 9; column++) {
                        if (get(row, column).getFixed() == 0 &&
                                get(row, column).getCandidates().contains(candidate.get()) &&
                                isOnlyCandidate(candidate.get(), row, column, this)){
                            get(row, column).setFixed(candidate.get());
                            printTable();
                            break A;
                        }
                    }
                }
                candidate.getAndIncrement();
            }
            deleteCandidates();
            if(initialSize==findFixedSize()){
                System.out.println("Table is unsolvable");
                return;
            }
        }
        System.out.println("Table is solved");
    }

    void deleteCandidates() {
        table.stream().flatMap(num -> num.stream()).forEach(i -> i.candidates.clear());
    }

    int findFixedSize(){
        return table.stream()
                .flatMap(num -> num.stream())
                .map(GridNumber::getFixed)
                .filter(i -> i!=0)
                .collect(Collectors.toList())
                .size();
    }
}
