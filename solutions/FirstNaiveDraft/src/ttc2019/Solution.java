package ttc2019;


import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import ttc2019.metamodels.bdd.*;
import ttc2019.metamodels.bdd.impl.BDDFactoryImpl;
import ttc2019.metamodels.tt.Cell;
import ttc2019.metamodels.tt.Port;
import ttc2019.metamodels.tt.Row;
import ttc2019.metamodels.tt.TruthTable;
import ttc2019.metamodels.tt.impl.OutputPortImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Naive solution
 *
 */
public class Solution {

	private TruthTable truthTable;
	private BDD binaryDecitionTree;
	private BDDFactory bddFactory;
	private BDD bdd;

	/**
	 * Load Truth table through driver
	 * @param tt the truth table
	 */
	void setTruthTable(final TruthTable tt) {
		this.truthTable = tt;
		DebugHelpers.printTruthTable((truthTable));

	}


	public void run() {
		bddFactory = BDDFactoryImpl.init();
		bdd = bddFactory.createBDD();

		EList<Port> inputPortList = truthTable.getPorts();
		inputPortList.removeIf(port -> (port instanceof OutputPortImpl));

		//Used for knowing the last iteration subtrees calculated
		List<Subtree> lastLevelTree = new ArrayList<>();

		Tree finalTree = null;

		//This is the tree generation, without any optimisation, so all branch are computed.
		for (Port port: inputPortList) {
			if(bdd.getTree() == null){
				//TODO First level of the tree

				// TODO TAKE CARE OF THE CASE OF ONLY ONE ROW
				// TODO so the first element is a leaf and not a subTree

				Subtree subtree = bddFactory.createSubtree();

				//Create the new port as a BDD type port
				InputPort bddPort = createPort(port);

				//Set the tree port
				subtree.setPort(bddPort);

				//It's the first so it's declared as the root tree
				bdd.setTree(subtree);
				//Adding to the last level deph tree for being able to use it at the next iteration.
				lastLevelTree.add(subtree);

				finalTree = subtree;
			} else {
				List<Subtree> nextLastLevelTree = new ArrayList<>();
				for (Subtree subTree: lastLevelTree) {
					//Create the new port as a BDD type port
					InputPort bddPort = createPort(port);

					//Creating left tree
					Subtree subLeftTree = bddFactory.createSubtree();
					subLeftTree.setPort(bddPort);
					subTree.setTreeForZero(subLeftTree);

					//Creating right tree
					Subtree subRightTree = bddFactory.createSubtree();
					subRightTree.setPort(bddPort);
					subTree.setTreeForOne(subRightTree);

					//Adding the the next next level.
					nextLastLevelTree.add(subLeftTree);
					nextLastLevelTree.add(subRightTree);
				}
				//The new level tree for the next turn.
				lastLevelTree = nextLastLevelTree;
			}
		}
		//TODO Calculating the leaf value at the end of the tree, or maybe doing it in the last level iteration of the graph :thinking_face:s

		if( finalTree instanceof Subtree){
			Subtree finalSubTree = (Subtree) finalTree;

			initiateLeaf(finalSubTree, truthTable.getRows());
		}else {
			Leaf leaf = (Leaf) finalTree;

			// TODO Si on n'a qu'un leaf des le depart
		}
		//TODO optimized the tree with the reducing algorithm : https://www.cs.ox.ac.uk/people/james.worrell/lec5-2015.pdf

		DebugHelpers.printTree(finalTree);

		binaryDecitionTree = bdd;
	}

	private void initiateLeaf(Tree tree, EList<Row> rows) {
		if(tree instanceof Subtree){
			Subtree subTree = (Subtree) tree;
			//If it's the last level, checking on forZero would have been the same
			if(subTree.getTreeForOne() == null){
				Leaf leaf = bddFactory.createLeaf();
				InputPort port = subTree.getPort();
				for (Row row : rows) {
					for (Cell cell : row.getCells()) {
						if(cell.getPort().getName().equals(port.getName())){
							for (Cell outputCell: row.getCells()){
								if(outputCell instanceof OutputPort){
									OutputPort leftPort = (OutputPort) outputCell;
									Assignment ass = bddFactory.createAssignment();
									ass.setOwner(leaf);
									ass.setPort(leftPort);
									ass.setValue(cell.isValue());
									leaf.getAssignments().add(ass);
								}
							}
						}
					}
				}
			} else{
				InputPort port = subTree.getPort();
				EList<Row> trueRows = new BasicEList<>();
				EList<Row> falseRows = new BasicEList<>();

				for (Row row : rows) {
					for (Cell cell : row.getCells()) {
						if(cell.getPort().getName().equals(port.getName())){
							if(cell.isValue()){
								trueRows.add(row);
							} else {
								falseRows.add(row);
							}
						}
					}
				}
				initiateLeaf(subTree.getTreeForZero(), trueRows );
				initiateLeaf(subTree.getTreeForOne() , falseRows);
			}

		} else {
				//TODO case only one leaf

		}

	}


	private InputPort createPort(Port port) {
		InputPort bddPort = bddFactory.createInputPort();
		bddPort.setOwner(bdd);
		bddPort.setName(port.getName());
		return bddPort;
	}


	/**
	 * Write BDD into an xmi file
	 * @throws IOException
	 */
	public void save() throws IOException {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("xmi", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		// create a resource
		Resource resource = resSet.createResource(URI
				.createURI("FirstNaiveDraft" + ".xmi"));
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		resource.getContents().add(binaryDecitionTree);

		// now save the content.
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}