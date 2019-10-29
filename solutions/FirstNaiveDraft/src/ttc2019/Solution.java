package ttc2019;


import org.eclipse.emf.common.util.EList;
import ttc2019.metamodels.bdd.BDD;
import ttc2019.metamodels.bdd.BDDFactory;
import ttc2019.metamodels.bdd.OutputPort;
import ttc2019.metamodels.bdd.Subtree;
import ttc2019.metamodels.bdd.impl.BDDFactoryImpl;
import ttc2019.metamodels.tt.Port;
import ttc2019.metamodels.tt.TruthTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Naive solution
 *
 */
public class Solution {

	private TruthTable truthTable;
	private BDD BinaryDEcitionTree;


	/**
	 * Load Truth table through driver
	 * @param tt
	 */
	void setTruthTable(final TruthTable tt) {
		this.truthTable = tt;
		DebugHelpers.printTruthTable((truthTable));

	}


	public void run() {
		BDDFactory bddFactory = BDDFactoryImpl.init();
		BDD bdd = bddFactory.createBDD();

		EList<Port> inputPortList = truthTable.getPorts();
		inputPortList.removeIf(port -> (port instanceof OutputPort));

		int depthOfTheTree = inputPortList.size();
		int actualDepth = 0;

		//Used for knowing the last iteration subtrees calculated
		List<Subtree> lastLevelTree = new ArrayList<>();

		//This is the tree generation, without any optimisation, so all branch are computed.
		for (Port port: inputPortList) {
			if(bdd.getTree() == null){
				//TODO First level of the tree

				// TODO TAKE CARE OF THE CASE OF ONLY ONE ROW
				// TODO so the first element is a leaf and not a subTree

				Subtree subtree = bddFactory.createSubtree();

				//Create the new port as a BDD type port
				ttc2019.metamodels.bdd.InputPort bddPort = createPort(bddFactory, bdd, port);

				//Set the tree port
				subtree.setPort(bddPort);

				//It's the first so it's declared as the root tree
				bdd.setTree(subtree);

				//We created one level of the tree, the first
				actualDepth++;
				//Adding to the last level deph tree for being able to use it at the next iteration.
				lastLevelTree.add(subtree);
			} else if(actualDepth == depthOfTheTree - 1 ){
				// TODO Last level of the tree
			} else {
				List<Subtree> nextLastLevelTree = new ArrayList<>();
				for (Subtree subTree: lastLevelTree) {
					//Create the new port as a BDD type port
					ttc2019.metamodels.bdd.InputPort bddPort = createPort(bddFactory, bdd, port);

					//Creating left tree
					Subtree subLeftTree = bddFactory.createSubtree();
					subLeftTree.setPort(bddPort);
					subTree.setTreeForZero(subLeftTree);

					//Creating right tree
					Subtree subRightTree = bddFactory.createSubtree();
					subRightTree.setPort(bddPort);
					subTree.setTreeForZero(subRightTree);

					//Adding the the next next level.
					nextLastLevelTree.add(subLeftTree);
					nextLastLevelTree.add(subRightTree);
				}
				//The new level tree for the next turn.
				lastLevelTree = nextLastLevelTree;

				//Another depth level created
				actualDepth++;
			}
		}

		//TODO Calculating the leaf value at the end of the tree, or maybe doing it in the last level iteration of the graph :thinking_face:s

		//TODO optimized the tree with the reducing algorithm : https://www.cs.ox.ac.uk/people/james.worrell/lec5-2015.pdf

	}

	private InputPort createPort(BDDFactory bddFactory, BDD bdd, Port port) {
		ttc2019.metamodels.bdd.InputPort bddPort = bddFactory.createInputPort();
		bddPort.setOwner(bdd);
		bddPort.setName(port.getName());
		return bddPort;
	}

	public void save() throws IOException {
	//write result
	}


}