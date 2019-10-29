package ttc2019;


import org.eclipse.emf.common.util.EList;
import org.eclipse.m2m.atl.emftvm.EmftvmFactory;
import org.eclipse.m2m.atl.emftvm.ExecEnv;
import org.eclipse.m2m.atl.emftvm.Metamodel;
import org.eclipse.m2m.atl.emftvm.Model;
import org.eclipse.m2m.atl.emftvm.util.ClassModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.ModuleResolver;
import ttc2019.metamodels.bdd.*;
import ttc2019.metamodels.bdd.impl.BDDFactoryImpl;
import ttc2019.metamodels.tt.Port;
import ttc2019.metamodels.tt.TTPackage;
import ttc2019.metamodels.tt.TruthTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solution {

	private ExecEnv env;
	private TruthTable truthTable;

	void setTruthTable(final TruthTable tt) {
		this.truthTable = tt;
		System.out.println("COUCOU");
		System.out.println(tt);
		final Model inModel = EmftvmFactory.eINSTANCE.createModel();
		inModel.setResource(truthTable.eResource());
		env.registerInputModel("IN", inModel);
	}

	public void load(String moduleName) {
		env = EmftvmFactory.eINSTANCE.createExecEnv();

		final Metamodel ttMetamodel = EmftvmFactory.eINSTANCE.createMetamodel();
		ttMetamodel.setResource(TTPackage.eINSTANCE.getCell().eResource());
		env.registerMetaModel("TT", ttMetamodel);

		final Metamodel bddMetamodel = EmftvmFactory.eINSTANCE.createMetamodel();
		bddMetamodel.setResource(BDDPackage.eINSTANCE.getTree().eResource());
		env.registerMetaModel("BDD", bddMetamodel);

		// loading module
		final ModuleResolver mr = new ClassModuleResolver(Solution.class);
		env.loadModule(mr, moduleName);
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