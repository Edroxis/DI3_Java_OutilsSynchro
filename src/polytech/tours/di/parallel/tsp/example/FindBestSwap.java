package polytech.tours.di.parallel.tsp.example;

import polytech.tours.di.parallel.tsp.Solution;
import polytech.tours.di.parallel.tsp.TSPCostCalculator;

public class FindBestSwap implements Runnable {

	int startingIteration;
	Solution sol;
	BestSwap best;
	int mostInterestingSwap;
	double MIScost, tempCost;
	boolean MISexist;

	FindBestSwap(BestSwap best, Solution sol, int i) {
		startingIteration = i;
		this.sol = sol;
		this.best = best;
	}

	@Override
	public void run() {
		MISexist = false;
		MIScost = 0;
		mostInterestingSwap = -1;

		for (int i = startingIteration + 1; i < sol.size() - 1; i++) {
			tempCost = TSPCostCalculator.diffPartialCostCalcSwap(sol, startingIteration, i);
			if (tempCost > 0 && tempCost > MIScost) {
				MISexist = true;
				MIScost = tempCost;
				mostInterestingSwap = i;
			}
		}

		if (MISexist) {
			sol.swap(startingIteration, mostInterestingSwap);
			sol.setOF(sol.getOF() - MIScost);
			best.checkBetterSolution(sol);
		}
		
	}

}
