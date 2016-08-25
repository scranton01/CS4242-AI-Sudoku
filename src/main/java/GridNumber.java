import lombok.Data;

import java.util.ArrayList;
import java.util.List;


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

    public String toString(){
        if(fixed == 0){
            return "[ ]";
        }
        return "[" + fixed + "]";
    }
}
