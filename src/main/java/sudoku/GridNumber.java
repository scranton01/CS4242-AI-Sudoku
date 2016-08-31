package sudoku;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
/**
 * CS4242 Artificial Inteligence
 * @Date 8/31/2016
 * @Author Jun Nguyen
 */

@Data
public class GridNumber {
    int fixed;
    List<Integer> candidates;
    int rowPos;
    int columnPos;


    GridNumber(int value, int row, int column){
        this.fixed = value;
        this.candidates = new ArrayList<>();
        this.rowPos = row;
        this.columnPos = column;
    }

    @Override
    public String toString(){
        if(fixed == 0){
            return "[ ]";
        }
        return "[" + fixed + "]";
    }
}
