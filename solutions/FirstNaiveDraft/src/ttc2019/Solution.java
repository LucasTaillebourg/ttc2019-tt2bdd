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

	/**
	 * The input truthtable.
	 */
	private TruthTable truthTable;
	/**
	 * The output tree.
	 */
	private BDD binaryDecitionTree;
	/**
	 * The factory for creating bdd items.
	 */
	private BDDFactory bddFactory;

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
		binaryDecitionTree = bddFactory.createBDD();

		// Creating input port list for always referencing the same port in the code
		EList<InputPort> inputPortList = new BasicEList<>();
		truthTable.getPorts().forEach(port -> {
			if (port instanceof ttc2019.metamodels.tt.InputPort){
				InputPort inputPort = bddFactory.createInputPort();
				inputPort.setOwner(binaryDecitionTree);
				inputPort.setName(port.getName());
				inputPortList.add(inputPort);
			}
		});

		// Creating output port list for always referencing the same port in the code
		EList<OutputPort> outPortList = new BasicEList<>();
		truthTable.getPorts().forEach(port -> {
			if (port instanceof OutputPortImpl){
				OutputPort outputPort = bddFactory.createOutputPort();
				outputPort.setOwner(binaryDecitionTree);
				outputPort.setName(port.getName());
				outPortList.add(outputPort);
			}
		});

		//Creating the tree
		Tree tree = createTree(inputPortList);;

		//Assign values to the Leafs
		instanciateLeaf(tree, truthTable.getRows(), outPortList);

		// Optimizing the tree, disabled because the validator can't validate an optimized tree.
		//bdd = TreeOptimization.optimize(bdd);
	}

	/**
	 * Create the tree and all the subTree, leaf will be initialize later. It's just the container.
	 * @param inputPortList All the input port for creating a level for each of those.
	 * @return A tree ready to be filled with values
	 */
	private Tree createTree(EList<InputPort> inputPortList) {
		Tree finalTree = null;

		//Used for knowing the last iteration subtrees calculated
		List<Subtree> lastLevelTree = new ArrayList<>();

		//This is the tree generation, without any optimisation, so all branch are computed.
		for (InputPort port: inputPortList) {
			//First iteration level. We set this tree as a root tree.
			if(binaryDecitionTree.getTree() == null){

				Subtree subtree = bddFactory.createSubtree();

				subtree.setPort(port);

				binaryDecitionTree.setTree(subtree);

				//Adding to the last level depth tree for being able to use it at the next iteration.
				lastLevelTree.add(subtree);

				finalTree = subtree;
			} else {
				//For each other tree level;
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

	/**
	 * Initialize all the leaf of the tree.
	 * It's a recursive method. Each time we filter the row list and going down just with the interesting rows.
	 * At the last level, only pertinent rows will be left for initializing the tree.
	 * @param tree The tree with the leaf to initiate
	 * @param rows The list of rows containing the pertinent ones for this level of iteration
	 * @param outPortList The list of output port for linking the leaf ot those port.
	 */
	private void instanciateLeaf(Tree tree, EList<Row> rows, EList<OutputPort> outPortList) {
		Subtree subTree = (Subtree) tree;

		//If it's the last level.  NDLR : checking on forZero would have been the same
		if(subTree.getTreeForOne() == null){
			//Instanciate each leaf with the correct port.
			//The port will tell us in which cell we have to look for knowing in which list we have to add the row.
			rows.forEach(row -> row.getCells()
					.stream()
					.filter(cell -> cell.getPort().getName().equals(subTree.getPort().getName()))
					.forEach( cell -> createLeaf(row, cell, subTree, outPortList)));

		} else{
			//Creating the row list for the both branch of the tree and then making a recursive call to the next tree level.
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

	/**
	 * Assign a rows in the pertinent list depending of the value of the cell.
	 * @param trueRows The list of rows concerning the true path
	 * @param falseRows The lsit of rows concerning the false path
	 * @param row The row to add
	 * @param cell The cell related to the good port for knowing in which list we have to add the row
	 */
	private void assignInRows(EList<Row> trueRows, EList<Row> falseRows, Row row, Cell cell) {
		if(cell.isValue()) {
			trueRows.add(row);
		}else{
			falseRows.add(row);
		}
	}

	/**
	 * Instantiate a leaf. That's the endpoint of the leaf initialization recursion
	 * @param row The row of the truthtable related to this leaf
	 * @param cell The cell of the parent port. Used for knowing if it's a left leaf or a right leaf.
	 * @param subTree The parent subtree. Used for putting the leaf under
	 * @param outPortList The output port list, used for linking the leaf output port to the right one.
	 */
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