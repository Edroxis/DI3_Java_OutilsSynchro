package polytech.tours.di.parallel.tsp.example;

import java.util.concurrent.Callable;

import polytech.tours.di.parallel.tsp.Solution;
import polytech.tours.di.parallel.tsp.TSPCostCalculator;

public class threadSwap implements Callable<Solution> {

	private Solution solution;
	
	public threadSwap(Solution sol){
		this.solution = sol;
	}
	
	@Override
	public Solution call() throws Exception {
		
		solution = myAlgorithm(solution);
		
		return solution;
	}
	
	public Solution myAlgorithm(Solution solution){
		int i, j;
		boolean notBestSol = true;
		Solution testedSol;
		TSPCostCalculator costCalc = new TSPCostCalculator();
		solution.setOF(costCalc.calcOF(solution));
		//SetForSwap set = new SetForSwap(-1,-1, 0);

		while(notBestSol)
		{
			notBestSol = false;
			
			for(i = 0; i < solution.size(); i++)
				for(j = i + 1; j < solution.size(); j++)	//Look for best solution
				{
					double diff = costCalc.interestingSwap(solution, i, j);
					if(diff>0)
					{
						solution.swap(i, j);
						solution.setOF(solution.getOF() - diff);
						notBestSol = true;
					}
					//TODO swap à la fin
				}
		}
		//System.out.println(solution);
		return solution;
	}

}
