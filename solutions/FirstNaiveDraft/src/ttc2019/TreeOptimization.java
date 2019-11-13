package ttc2019;

import ttc2019.metamodels.bdd.*;
import ttc2019.metamodels.bdd.impl.LeafImpl;

import java.util.ArrayList;
import java.util.LinkedList;
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
     * Remove redundant tests
     * @param bdd
     * @return
     */
    private static BDD testReduction(BDD bdd){
        for(Port port : bdd.getPorts()) {
            if (port instanceof OutputPort) {

            }
        }
        return  bdd;
    }

    /**
     * Remove duplicate leaves
     * @param bdd
     * @return
     */
    private static  BDD leafReduction(BDD bdd){
        Leaf found;
        List<Leaf> uniqueLeaves = new ArrayList<>();
        for(Port port : bdd.getPorts()){
            if(port instanceof  OutputPort){
              for(Assignment assignment:  ((OutputPort) port).getAssignments()){
                  if (!uniqueLeaves.contains(assignment.getOwner())) {
                      found = getLeafIfContains(uniqueLeaves,assignment.getOwner());
                      if(found !=null){
                          assignment.getOwner().getOwnerSubtreeForZero().setTreeForZero(found);
                          assignment.getOwner().getOwnerSubtreeForOne().setTreeForOne(found);
                          assignment.setOwner(found);
                      }else{
                          uniqueLeaves.add(assignment.getOwner());
                      }
                  }
              }
            }
        }
        return bdd;
    }

    private static Leaf getLeafIfContains(List<Leaf> leaves, Leaf leaf){
        for(Leaf leafIt : leaves){
            if (areLeafEquals(leafIt,leaf)){
                return leafIt;
            }
        }
        return null;
    }

    private static boolean areLeafEquals(Leaf leaf1, Leaf leaf2){
        boolean found;
        List<Port> falseStack =  leaf1.getAssignments().parallelStream().filter(Assignment::isValue).map(Assignment::getPort).collect(Collectors.toList());
        List<Port> trueStack =  leaf1.getAssignments().parallelStream().filter(as -> !as.isValue()).map(Assignment::getPort).collect(Collectors.toList());
        for(Assignment assign : leaf2.getAssignments()){
            if(assign.isValue()){
                found = trueStack.remove(assign.getPort());
                if(!found) return false;
            }else{
                found = falseStack.remove(assign.getPort());
                if(!found) return false;
            }
        }

        return falseStack.isEmpty() && trueStack.isEmpty();
    }


}
