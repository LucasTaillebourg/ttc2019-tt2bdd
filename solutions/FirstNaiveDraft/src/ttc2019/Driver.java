package ttc2019;

import java.io.File;
import java.io.IOException;


public class Driver {

	public static void main(final String[] args) {
		try {
			initialize();
			load();
			run();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	//private static ResourceSet repository;
	private static String RunIndex;
	private static String Model;
	private static String Tool;

	private static long stopwatch;

	private static Solution solution;
	private static String ModelPath;

	private static Object loadFile(final String path) {
		//load file
		return null;
	}

	static void load() throws IOException {
		stopwatch = System.nanoTime();
		//solution.setTruthTable(xxxxx);

		stopwatch = System.nanoTime() - stopwatch;
		report(BenchmarkPhase.Load);
	}

	static void initialize() throws Exception {
		stopwatch = System.nanoTime();


		Model = System.getenv("Model");
		ModelPath = System.getenv("ModelPath");
		RunIndex = System.getenv("RunIndex");
		Tool = System.getenv("Tool");

		solution = new Solution();
		//solution.load("TT2BDD");

		stopwatch = System.nanoTime() - stopwatch;
		report(BenchmarkPhase.Initialization);
	}

	static void run() throws IOException {
		stopwatch = System.nanoTime();
		solution.run();
		stopwatch = System.nanoTime() - stopwatch;
		report(BenchmarkPhase.Run);
		solution.save();
	}

	static void report(final BenchmarkPhase phase) {
		System.out.println(String.format("%s;%s;%s;%s;Time;%s", Tool, Model, RunIndex, phase.toString(), Long.toString(stopwatch)));

		if ("true".equals(System.getenv("NoGC"))) {
			// nothing to do
		} else {
			Runtime.getRuntime().gc();
			Runtime.getRuntime().gc();
			Runtime.getRuntime().gc();
			Runtime.getRuntime().gc();
			Runtime.getRuntime().gc();
		}

		final long memoryUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println(String.format("%s;%s;%s;%s;Memory;%s", Tool, Model, RunIndex, phase.toString(), Long.toString(memoryUsed)));
	}

	enum BenchmarkPhase {
		Initialization, Load, Run
	}
}
