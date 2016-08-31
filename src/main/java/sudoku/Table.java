package sudoku;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static sudoku.Utility.containsUnique;
import static sudoku.Utility.getBoxNumberBy;

@Data
public class Table {
    List<List<GridNumber>> table;

    Table() {
        table = new ArrayList<>();
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

//    boolean isOnlyCandidate(int var, int row, int column){
//        get(row, column);
//    }

    void solvePuzzle() {
        for (int i = 0; i < 9; i++) {
            for (int row = 0; row < 9; row++) {
                for (int column = 0; column < 9; column++) {
                    if (getRow(row).stream().map(num -> num.getFixed()).noneMatch(fixed -> fixed == i) &&
                            getColumn(column).stream().map(num -> num.getFixed()).noneMatch(fixed -> fixed == i) &&
                            getBox(getBoxNumberBy(row,column)).stream().map(num -> num.getFixed()).noneMatch(fixed -> fixed == i)) {
                        System.out.println("");
                    }
                }
            }
        }
//    while(!isSolved()){
//
//    }
    }
}
