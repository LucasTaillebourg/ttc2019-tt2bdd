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
		List<Subtree> lastLevelTree = new ArrayList<>();

		//This is the tree generation, without any optimisation, so all branch are computed.
		for (Port port: inputPortList) {
			if(bdd.getTree() == null){
				//TODO First level of the tree

				// TODO TAKE CARE OF THE CASE OF ONLY ONE ROW
				// TODO so the first element is a leaf and not a subTree

				Subtree subtree = bddFactory.createSubtree();

				//Create the new port as a BDD type port
				ttc2019.metamodels.bdd.InputPort bddPort = bddFactory.createInputPort();
				bddPort.setOwner(bdd);
				bddPort.setName(port.getName());

				//Set the tree port
				subtree.setPort(bddPort);

				//It's the first so it's declared as the root tree
				bdd.setTree(subtree);

				//We created one level of the tree, the first
				actualDepth++;
			} else if(actualDepth == depthOfTheTree){
				// TODO Last level of the tree
			} else {
				// TODO general case, inside the tree when we create subTree
			}
		}

		//TODO optimized the tree with the reducing algorithm : https://www.cs.ox.ac.uk/people/james.worrell/lec5-2015.pdf

	}

	public void save() throws IOException {
	//write result
	}


}