package polytech.tours.di.parallel.tsp.example;

import java.util.Collections;
import java.util.Properties;
import java.util.Random;

import polytech.tours.di.parallel.tsp.Algorithm;
import polytech.tours.di.parallel.tsp.Instance;
import polytech.tours.di.parallel.tsp.InstanceReader;
import polytech.tours.di.parallel.tsp.Solution;
import polytech.tours.di.parallel.tsp.TSPCostCalculator;

/**
 * Implements an example in which we read an instance from a file and print out some of the distances in the distance matrix.
 * Then we generate a random solution and computer its objective function. Finally, we print the solution to the output console.
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 *
 */
public class ExampleAlgorithm implements Algorithm {

	@Override
	public Solution run(Properties config) {

		//read instance
		InstanceReader ir=new InstanceReader();
		ir.buildInstance(config.getProperty("instance"));
		//get the instance 
		Instance instance=ir.getInstance();
		//print some distances
		System.out.println("d(1,2)="+instance.getDistance(1, 2));
		System.out.println("d(10,19)="+instance.getDistance(10, 19));
		//read maximum CPU time
		long max_cpu=Long.valueOf(config.getProperty("maxcpu"));
		//build a random solution
		Random rnd=new Random(Long.valueOf(config.getProperty("seed")));
		Solution s=new Solution();
		Solution best=null;
		
		//set distance Matrix in TSPCostCalculator
		TSPCostCalculator.setMatrix(instance.getDistanceMatrix());
		TSPCostCalculator costCalc = new TSPCostCalculator();
		
		long startTime=System.currentTimeMillis();
		for(int i=0; i<instance.getN(); i++){
			s.add(i);
		}
		while((System.currentTimeMillis()-startTime)/1_000<=max_cpu){	
			Collections.shuffle(s,rnd);
			
			s = swapAlgorithm(s);
			
			//set the objective function of the solution
			s.setOF(costCalc.calcOF(s));
			System.out.println(s);
			if(best==null)
				best=s.clone();
			else if(s.getOF()<best.getOF())
				best=s.clone();
		}
		//return the solution
		return best;
	}
	
	public Solution swapAlgorithm(Solution solution){
		int i, j;
		boolean notBestSol = true;
		Solution testedSol;
		TSPCostCalculator costCalc = new TSPCostCalculator();

		while(notBestSol)
		{
			notBestSol = false;
			for(i = 0; i < solution.size(); i++)
				for(j = i + 1; j < solution.size(); j++)	//Look for best solution
				{
					testedSol = solution.clone();
					testedSol.swap(i, j);
					testedSol.setOF(costCalc.calcOF(testedSol));
					if(testedSol.getOF() < solution.getOF())
					{
						solution = testedSol;
						notBestSol = true;
					}
				}
			for(i = 0; i < solution.size(); i++)
				for(j = i + 1; j < solution.size(); j++)	//Look for best solution
				{
					testedSol = solution.clone();
					testedSol.relocate(i, j);
					testedSol.setOF(costCalc.calcOF(testedSol));
					if(testedSol.getOF() < solution.getOF())
					{
						solution = testedSol;
						notBestSol = true;
					}
				}
		}
		
		return solution;
	}
	
	public Solution relocateAlgorithm(Solution solution){
		int i, j;
		boolean notBestSol = true;
		Solution testedSol;
		TSPCostCalculator costCalc = new TSPCostCalculator();

		while(notBestSol)
		{
			notBestSol = false;
			
		}
		return solution;
	}

}
