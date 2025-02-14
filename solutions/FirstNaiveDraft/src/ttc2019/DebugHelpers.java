package ttc2019;

import ttc2019.metamodels.bdd.Leaf;
import ttc2019.metamodels.bdd.Subtree;
import ttc2019.metamodels.bdd.Tree;
import ttc2019.metamodels.tt.Cell;
import ttc2019.metamodels.tt.Port;
import ttc2019.metamodels.tt.Row;
import ttc2019.metamodels.tt.TruthTable;

/**
 * Provides methods to print the truth table and the output bdd to check for corectness
 */
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

    public static void printTree(Tree tree){
        if (tree instanceof Subtree){
            Subtree subtree = (Subtree) tree;
            System.out.println(subtree.getPort().getName());
            System.out.println("One");
            printTree(subtree.getTreeForOne());
            System.out.println("Zero");
            printTree(subtree.getTreeForZero());
        } else if (tree instanceof Leaf) {
            Leaf leaf = (Leaf) tree;
            leaf.getAssignments().forEach(assignment -> System.out.println(assignment.toString() + assignment.getPort() + " : " +Boolean.toString(assignment.isValue())));
        }
    }
}
