package ttc2019;


import org.eclipse.m2m.atl.emftvm.EmftvmFactory;
import org.eclipse.m2m.atl.emftvm.ExecEnv;
import org.eclipse.m2m.atl.emftvm.Metamodel;
import org.eclipse.m2m.atl.emftvm.Model;
import org.eclipse.m2m.atl.emftvm.util.ClassModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.ModuleResolver;
import ttc2019.metamodels.bdd.BDD;
import ttc2019.metamodels.bdd.BDDPackage;
import ttc2019.metamodels.tt.Cell;
import ttc2019.metamodels.tt.Row;
import ttc2019.metamodels.tt.TTPackage;
import ttc2019.metamodels.tt.TruthTable;

import java.io.IOException;

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
	//convert
	}

	public void save() throws IOException {
	//write result
	}


}