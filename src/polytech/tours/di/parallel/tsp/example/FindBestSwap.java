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

		for (int i = startingIteration + 1; i < sol.size() - 1; i++) {	//find most interesting swap (MIS)
			tempCost = TSPCostCalculator.diffPartialCostCalcSwap(sol, startingIteration, i);
			if (tempCost > 0 && tempCost > MIScost) {	//if tested swap more interesting and better than previous one
				MISexist = true;
				MIScost = tempCost;
				mostInterestingSwap = i;
			}
			
			if(System.currentTimeMillis() > best.getTimeout())	//if timeout reached
				break;
		}

		if (MISexist) {	//if a better solution has been found
			sol.swap(startingIteration, mostInterestingSwap);
			sol.setOF(sol.getOF() - MIScost);
			best.checkBetterSolution(sol);
		}
	}

}
