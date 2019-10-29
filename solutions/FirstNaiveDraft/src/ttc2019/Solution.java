package ttc2019;


import org.eclipse.emf.common.util.EList;
import ttc2019.metamodels.bdd.*;
import ttc2019.metamodels.bdd.impl.BDDFactoryImpl;
import ttc2019.metamodels.tt.Port;
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
		inputPortList.removeIf(port -> (port instanceof OutputPortImpl));

		inputPortList.forEach(port -> {
			System.out.println(port);
		});

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
				InputPort bddPort = createPort(bddFactory, bdd, port);

				//Set the tree port
				subtree.setPort(bddPort);

				//It's the first so it's declared as the root tree
				bdd.setTree(subtree);

				//We created one level of the tree, the first
				actualDepth++;
				//Adding to the last level deph tree for being able to use it at the next iteration.
				lastLevelTree.add(subtree);
			} else if(actualDepth == depthOfTheTree - 1 ){
				System.out.println("Last level : " + actualDepth);
				System.out.println("Tree depth: " + depthOfTheTree);
				for (Subtree subTree: lastLevelTree) {
					//TODO THE LEAF THING WITH THE ASSIGNMENT LIST ET TOUT ET TOUT MAIS ON ARRIVE AU BOUT WOUHOU
					Leaf leaf = bddFactory.createLeaf();
					leaf.getAssignments()
				}
				// TODO Last level of the tree
			} else {
				List<Subtree> nextLastLevelTree = new ArrayList<>();
				for (Subtree subTree: lastLevelTree) {
					System.out.println(actualDepth);
					//Create the new port as a BDD type port
					InputPort bddPort = createPort(bddFactory, bdd, port);

					InputPort bddPort = createPort(bddFactory, bdd, port);

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
		binaryDecitionTree = bdd;
	}


	private InputPort createPort(BDDFactory bddFactory, BDD bdd, Port port) {
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