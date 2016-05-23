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

		//read maximum CPU time
		long max_cpu=Long.valueOf(config.getProperty("maxcpu"));
		
		//build a random solution
		Random rnd=new Random(Long.valueOf(config.getProperty("seed")));
		Solution s=new Solution();
		Solution best=null;
		long startTime=System.currentTimeMillis();
		
		//Set a calculator
		TSPCostCalculator costCalc = new TSPCostCalculator(instance);
		
		for(int i=0; i<instance.getN(); i++){
			s.add(i);
		}
		while((System.currentTimeMillis()-startTime)/1_000<=max_cpu){	
			Collections.shuffle(s,rnd);
			//set the objective function of the solution
			s.setOF(costCalc.calcOF(s));
			
			System.out.println("before:"+s.getOF());
			int first = 5;
			int last = 15;
			double diff = TSPCostCalculator.diffPartialCostCalcSwap(s, first, last);
			System.out.println("\ncalculated diff:"+diff);
			System.out.println("\nestimated future cost:"+ (s.getOF()-diff));
			s.swap(first, last);
			s.setOF(costCalc.calcOF(s));
			System.out.println("\nafter:"+s.getOF());
			
			//System.out.println(s);
			if(best==null)
				best=s.clone();
			else if(s.getOF()<best.getOF())
				best=s.clone();
		}
		//return the solution
		
		System.out.println("================BEST==================\n");
		return best;
	}

}
