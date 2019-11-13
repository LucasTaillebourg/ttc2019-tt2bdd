package ttc2019;

import ttc2019.metamodels.bdd.*;
import ttc2019.metamodels.bdd.impl.LeafImpl;

import java.util.List;
import java.util.stream.Collectors;

public class TreeOptimization {

    /**
     * https://www.cs.ox.ac.uk/people/james.worrell/lec5-2015.pdf
     * @param bdd
     * @return
     */
    public static BDD optimize(BDD bdd){

        bdd = leafReduction(bdd);

        return bdd;
    }

    /**
     * Remove duplicate leaves
     * @param bdd
     * @return
     */
    private static  BDD leafReduction(BDD bdd){
        Leaf lastFalseLeaf = null;
        Leaf lastTrueLeaf = null;
        for(Port port : bdd.getPorts()){
            if(port instanceof  OutputPort){
              for(Assignment assignment:  ((OutputPort) port).getAssignments()){
                  if(assignment.isValue()) {
                      lastTrueLeaf = overrdeLeaf(assignment, lastTrueLeaf, true);
                  }else{
                      lastFalseLeaf = overrdeLeaf(assignment,lastFalseLeaf,false);
                  }
              }
            }
        }
        return bdd;
    }

    private static  Leaf overrdeLeaf(Assignment assignment, Leaf newLeaf,boolean branch){
        if(newLeaf == null){
            return assignment.getOwner();
        }
        Leaf oldLeaf = assignment.getOwner();
        if(branch)
            oldLeaf.getOwnerSubtreeForOne().setTreeForOne(newLeaf) ;
        else
            oldLeaf.getOwnerSubtreeForZero().setTreeForZero(newLeaf); ;
        assignment.setOwner(newLeaf);
        return newLeaf;
    }


}
