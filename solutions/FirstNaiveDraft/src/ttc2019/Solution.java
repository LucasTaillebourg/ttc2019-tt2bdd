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

		// Creating input port list for always referencing the same port in the code
		EList<InputPort> inputPortList = new BasicEList<>();
		truthTable.getPorts().forEach(port -> {
			if (port instanceof ttc2019.metamodels.tt.InputPort){
				InputPort inputPort = bddFactory.createInputPort();
				inputPort.setOwner(bdd);
				inputPort.setName(port.getName());
				inputPortList.add(inputPort);
			}
		});

		// Creating output port list for always referencing the same port in the code
		EList<OutputPort> outPortList = new BasicEList<>();
		truthTable.getPorts().forEach(port -> {
			if (port instanceof OutputPortImpl){
				OutputPort outputPort = bddFactory.createOutputPort();
				outputPort.setOwner(bdd);
				outputPort.setName(port.getName());
				outPortList.add(outputPort);
			}
		});

		//Creating the tree
		Tree tree = createTree(inputPortList);;

		//Assign values to the Leafs
		instanciateLeaf(tree, truthTable.getRows(), outPortList);

		//bdd = TreeOptimization.optimize(bdd);

		DebugHelpers.printTree(tree);

		//TODO optimized the tree with the reducing algorithm : https://www.cs.ox.ac.uk/people/james.worrell/lec5-2015.pdf

		binaryDecitionTree = bdd;
	}

	private Tree createTree(EList<InputPort> inputPortList) {
		Tree finalTree = null;

		//Used for knowing the last iteration subtrees calculated
		List<Subtree> lastLevelTree = new ArrayList<>();

		//This is the tree generation, without any optimisation, so all branch are computed.
		for (InputPort port: inputPortList) {
			if(bdd.getTree() == null){

				Subtree subtree = bddFactory.createSubtree();

				//Set the tree port
				subtree.setPort(port);

				//It's the first so it's declared as the root tree
				bdd.setTree(subtree);
				//Adding to the last level deph tree for being able to use it at the next iteration.
				lastLevelTree.add(subtree);

				finalTree = subtree;
			} else {
				//Initiating all the subtree of the last level
				List<Subtree> nextLastLevelTree = new ArrayList<>();
				for (Subtree subTree: lastLevelTree) {

					//Creating left tree
					Subtree subLeftTree = bddFactory.createSubtree();
					subLeftTree.setPort(port);
					subTree.setTreeForOne(subLeftTree);

					//Creating right tree
					Subtree subRightTree = bddFactory.createSubtree();
					subRightTree.setPort(port);
					subTree.setTreeForZero(subRightTree);

					//Adding to the next next level.
					nextLastLevelTree.add(subLeftTree);
					nextLastLevelTree.add(subRightTree);
				}
				//The new level tree for the next turn.
				lastLevelTree = nextLastLevelTree;
			}
		}
		return finalTree;
	}

	private void instanciateLeaf(Tree tree, EList<Row> rows, EList<OutputPort> outPortList) {
		Subtree subTree = (Subtree) tree;

		//If it's the last level.  NDLR : checking on forZero would have been the same
		if(subTree.getTreeForOne() == null){

			rows.forEach(row -> row.getCells()
					.stream()
					.filter(cell -> cell.getPort().getName().equals(subTree.getPort().getName()))
					.forEach( cell -> createLeaf(row, cell, subTree, outPortList)));

		} else{
			EList<Row> trueRows = new BasicEList<>();
			EList<Row> falseRows = new BasicEList<>();

			rows.forEach(row -> row.getCells()
					.stream()
					.filter(cell -> cell.getPort().getName().equals(subTree.getPort().getName()))
					.forEach( cell -> assignInRows(trueRows, falseRows, row, cell)));

			instanciateLeaf(subTree.getTreeForZero(), falseRows, outPortList);
			instanciateLeaf(subTree.getTreeForOne() , trueRows, outPortList);
		}

	}

	private void assignInRows(EList<Row> trueRows, EList<Row> falseRows, Row row, Cell cell) {
		if(cell.isValue()) {
			trueRows.add(row);
		}else{
			falseRows.add(row);
		}
	}

	private void createLeaf(Row row, Cell cell, Subtree subTree, EList<OutputPort> outPortList) {
		Leaf leaf = bddFactory.createLeaf();
		for (Cell outputCell: row.getCells()){
			if(outputCell.getPort() instanceof OutputPortImpl){
				Assignment ass = bddFactory.createAssignment();
				ass.setOwner(leaf);
				ass.setPort(outPortList.stream()
								.filter(outputPort -> outputPort.getName().equals(outputCell.getPort().getName()))
								.findFirst()
								.get());
				ass.setValue(outputCell.isValue());
				leaf.getAssignments().add(ass);
				if(cell.isValue()){
					subTree.setTreeForOne(leaf);
				}else{
					subTree.setTreeForZero(leaf);
				}
			}
		}
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
		Resource resource = resSet.createResource(URI.createURI("FirstNaiveDraft" + ".xmi"));
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		resource.getContents().add(binaryDecitionTree);

		// now save the content.
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}