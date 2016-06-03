package polytech.tours.di.parallel.tsp.example;

import polytech.tours.di.parallel.tsp.Solution;
import polytech.tours.di.parallel.tsp.TSPCostCalculator;

public class threadSwap2 implements Runnable{

	Solution sol;
	private boolean finished;
	private long timeout;
	
	threadSwap2(Solution s, long timeout){
		this.sol = s;
		finished = false;
		this.timeout = timeout;
	}
	
	@Override
	public void run() {
		sol = myAlgorithm(sol);
		finished = true;
	}
	public boolean isFinished(){
		return finished;
	}
	
	public Solution getSol(){
		return sol;
	}
	
	public Solution myAlgorithm(Solution solution){
		int i, j;
		boolean notBestSol = true;
		TSPCostCalculator costCalc = new TSPCostCalculator();
		solution.setOF(costCalc.calcOF(solution));

		while(notBestSol)
		{
			notBestSol = false;
			
			for(i = 0; i < solution.size(); i++)
				for(j = i + 1; j < solution.size(); j++)	//Look for best solution
				{
					double diff = TSPCostCalculator.diffPartialCostCalcSwap(solution, i, j);
					if(diff>0)
					{
						solution.swap(i, j);
						solution.setOF(solution.getOF() - diff);
						notBestSol = true;
					}
					//TODO swap à la fin
					if(System.currentTimeMillis() > timeout)	//Timeout reached
						return solution;
				}
		}
		//System.out.println(solution);
		return solution;
	}
	
	/*int i, j;	//code avec swap à la fin
	boolean notBestSol = true;
	TSPCostCalculator costCalc = new TSPCostCalculator();
	solution.setOF(costCalc.calcOF(solution));
	int MISi=0, MISj=0;
	double MISdiff = -1;

	while(notBestSol)
	{
		notBestSol = false;
		MISdiff = -1;
		for(i = 0; i < solution.size(); i++)
			for(j = i + 1; j < solution.size(); j++)	//Look for best solution
			{
				double diff = TSPCostCalculator.diffPartialCostCalcSwap(solution, i, j);
				if(diff > 0 && diff > MISdiff)
				{
					MISi = i;
					MISj = j;
					MISdiff = diff;
				}
				if(System.currentTimeMillis() > timeout)	//Timeout reached
					return solution;
			}
		if(MISdiff > 0){
			solution.swap(MISi, MISj);
			solution.setOF(solution.getOF() - MISdiff);
			notBestSol = true;
		}
	}
	//System.out.println(solution);
	return solution;*/

}
