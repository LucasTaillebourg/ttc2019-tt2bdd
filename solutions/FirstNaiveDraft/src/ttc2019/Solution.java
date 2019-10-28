package ttc2019;


import org.eclipse.m2m.atl.emftvm.EmftvmFactory;
import org.eclipse.m2m.atl.emftvm.ExecEnv;
import org.eclipse.m2m.atl.emftvm.Metamodel;
import org.eclipse.m2m.atl.emftvm.Model;
import org.eclipse.m2m.atl.emftvm.util.ClassModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.ModuleResolver;
import ttc2019.metamodels.bdd.BDDPackage;
import ttc2019.metamodels.tt.TTPackage;
import ttc2019.metamodels.tt.TruthTable;

import java.io.IOException;


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
	//convert
	}

	public void save() throws IOException {
	//write result
	}


}