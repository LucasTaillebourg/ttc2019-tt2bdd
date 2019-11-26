package ttc2019;

import ttc2019.metamodels.bdd.*;
import ttc2019.metamodels.bdd.impl.LeafImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeOptimization {
    private static List<Tree>  nodesAtCurrentLvl;
    private static List<Tree> nodesAtParentLvl;

    /**
     * https://www.cs.ox.ac.uk/people/james.worrell/lec5-2015.pdf
     * @param bdd
     * @return
     */
    public static void optimize(BDD bdd){

       leafReduction(bdd);
      // testReduction(bdd);


    }

    /**
     * Remove redundant tests
     * @param bdd
     * @return
     */
    private static void testReduction(BDD bdd){
        List<OutputPort> outputPorts  = bdd.getPorts().parallelStream().filter(p -> p instanceof OutputPort).map(p -> (OutputPort)p).collect(Collectors.toList());
        nodesAtCurrentLvl = new ArrayList<>();
        nodesAtParentLvl = new ArrayList<>();
        outputPorts.parallelStream()
                .forEach(outputPort -> nodesAtCurrentLvl.addAll(outputPort.getAssignments()
                        .parallelStream()
                        .map(Assignment::getOwner).collect(Collectors.toList())
                ));


        while (!nodesAtCurrentLvl.isEmpty()){
            System.out.println(nodesAtCurrentLvl);
            for(Tree currentNode : nodesAtCurrentLvl){
                Subtree currParentOne = currentNode.getOwnerSubtreeForOne();
                Subtree currParentZero = currentNode.getOwnerSubtreeForZero();
                //If zero-branch parent points twice to leaf
                if(currParentOne!= null){
                    if( currParentOne.getTreeForOne().equals(currentNode)){
                        deleteParent(currentNode);
                    }
                    else{
                        nodesAtParentLvl.add(currParentOne);
                    }
                }
                if(currParentZero!= null){
                    if( currParentZero.getTreeForZero().equals(currentNode)){
                        deleteParent(currentNode);
                    }
                    else{
                        nodesAtParentLvl.add(currParentOne);
                    }
                }

            }
            nodesAtParentLvl.removeAll(Collections.singletonList(null));
            nodesAtCurrentLvl.clear();
            nodesAtCurrentLvl.addAll(nodesAtParentLvl);
            nodesAtParentLvl.clear();
        }


    }

    private static  void deleteParent(Tree currentNode){
        Subtree newParentOne = currentNode.getOwnerSubtreeForOne();
        Subtree newParentZero = currentNode.getOwnerSubtreeForZero();
        if (newParentOne != null) {
            newParentOne.setTreeForOne(currentNode);
            nodesAtParentLvl.add(newParentOne);
        }
        if (newParentZero != null){
            newParentZero.setTreeForZero(currentNode);
            nodesAtParentLvl.add(newParentZero);
        }
    }


    /**
     * Remove duplicate leaves
     * @param bdd
     * @return
     */
    private static  BDD leafReduction(BDD bdd){
        Leaf found;
        List<Leaf> uniqueLeaves = new ArrayList<>();
        List<Assignment> assignementToDl = new ArrayList<>();
        for(Port port : bdd.getPorts()){
            if(port instanceof  OutputPort){
              for(Assignment assignment:  ((OutputPort) port).getAssignments()){

                  if (!uniqueLeaves.contains(assignment.getOwner())) {
                      found = getLeafIfContains(uniqueLeaves,assignment.getOwner());
                      if(found !=null){
                          //System.out.println(assignment.toString());
                          if(assignment.getOwner().getOwnerSubtreeForZero()!= null){
                              assignment.getOwner().getOwnerSubtreeForZero().setTreeForZero(found);
                          }
                          if(assignment.getOwner().getOwnerSubtreeForOne()!= null){
                              assignment.getOwner().getOwnerSubtreeForOne().setTreeForOne(found);
                          }
                          //assignment.setOwner(found);
                          assignementToDl.add(assignment);
                      }else{
                          System.out.println(assignment);
                          System.out.println(assignment.getOwner());
                          uniqueLeaves.add(assignment.getOwner());
                      }
                  }
              }
              ((OutputPort) port).getAssignments().removeAll(assignementToDl);
                assignementToDl.clear();
            }
        }
        System.out.println(uniqueLeaves);
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
