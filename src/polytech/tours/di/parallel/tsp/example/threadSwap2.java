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
		Solution testedSol;
		TSPCostCalculator costCalc = new TSPCostCalculator();
		solution.setOF(costCalc.calcOF(solution));

		while(notBestSol)
		{
			notBestSol = false;
			
			for(i = 0; i < solution.size(); i++)
				for(j = i + 1; j < solution.size(); j++)	//Look for best solution
				{
					/*testedSol = solution.clone();
					testedSol.swap(i, j);
					testedSol.setOF(costCalc.calcOF(testedSol));
					if(testedSol.getOF() < solution.getOF())
					{
						solution = testedSol;
						notBestSol = true;
					}*/
					if(costCalc.interestingSwap(solution, i, j)>0)
					{
						solution.swap(i, j);
						solution.setOF(costCalc.calcOF(solution));
						notBestSol = true;
					}
					//TODO swap à la fin
					/*if(System.currentTimeMillis() > timeout)
						return solution;*/
				}
		}
		//System.out.println(solution);
		return solution;
	}

}
