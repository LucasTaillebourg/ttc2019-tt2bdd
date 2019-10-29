package ttc2019;

import ttc2019.metamodels.tt.Cell;
import ttc2019.metamodels.tt.Port;
import ttc2019.metamodels.tt.Row;
import ttc2019.metamodels.tt.TruthTable;

public  class DebugHelpers {
    public static void printTruthTable(TruthTable tt){
        for(Port label: tt.getPorts()){
            System.out.print(label.getName());
            System.out.print("  |  ");

        }
        System.out.println("");
        for(Row row : tt.getRows()){
            for(Cell cell : row.getCells()){
                System.out.print(cell.isValue());
                System.out.print("  |  ");
            }
            System.out.println("");
        }
    }
}
