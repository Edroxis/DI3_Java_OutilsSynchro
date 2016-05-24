package polytech.tours.di.parallel.tsp.example;

import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import polytech.tours.di.parallel.tsp.Algorithm;
import polytech.tours.di.parallel.tsp.Instance;
import polytech.tours.di.parallel.tsp.InstanceReader;
import polytech.tours.di.parallel.tsp.Solution;
import polytech.tours.di.parallel.tsp.TSPCostCalculator;

/**
 * Implements an example in which we read an instance from a file and print out
 * some of the distances in the distance matrix. Then we generate a random
 * solution and computer its objective function. Finally, we print the solution
 * to the output console.
 * 
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 *
 */
public class ExampleAlgorithm implements Algorithm {

	@Override
	public Solution run(Properties config) {

		// read instance
		InstanceReader ir = new InstanceReader();
		ir.buildInstance(config.getProperty("instance"));

		// get the instance
		Instance instance = ir.getInstance();

		// read maximum CPU time
		long max_cpu = Long.valueOf(config.getProperty("maxcpu"));
		
		// number of threads
		int nbThreads = 4;
		
		int nbTested = 0;

		// build a random solution
		Random rnd = new Random(Long.valueOf(config.getProperty("seed")));
		Solution s = new Solution();
		Solution best = null;
		long startTime = System.currentTimeMillis();

		// Set a calculator
		TSPCostCalculator costCalc = new TSPCostCalculator(instance);
		
		// Set bestSwap Object
		BestSwap bestSwap = null;
		
		// Declaration task list
		Runnable[] tasks = new Runnable[instance.getN()];
		
		ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

		for (int i = 0; i < instance.getN(); i++) {
			s.add(i);
		}
		while ((System.currentTimeMillis() - startTime) / 1_000 <= max_cpu) {
			Collections.shuffle(s, rnd);
			// set the objective function of the solution
			s.setOF(costCalc.calcOF(s));
			
			if(bestSwap == null)
				bestSwap = new BestSwap(s, System.currentTimeMillis()+max_cpu*1000);

			while(bestSwap.isModified())
			{
				bestSwap.initIsModified();
				for(int i = 0; i < s.size()-1; i++)
				{
					tasks[i] = new FindBestSwap(bestSwap, s.clone(), i);
					executor.submit(tasks[i]);
				}
				try {
					executor.awaitTermination(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			best = bestSwap.getSol();
			nbTested++;
			
			/*int first = 0;
			int last = 28;
			System.out.println("avant: "+s.getOF());
			double diff = TSPCostCalculator.diffPartialCostCalcSwap(s, first, last);
			System.out.println("calculated Diff: " + diff);
			System.out.println("estimated cost: " + (s.getOF()-diff));
			s.swap(first, last);
			s.setOF(costCalc.calcOF(s));
			System.out.println("après: "+ s.getOF());*/
			
			System.out.println(best);
			if (best == null)
				best = s.clone();
			else if (s.getOF() < best.getOF())
				best = s.clone();
		}
		// return the solution

		System.out.println("================BEST==================\n");
		System.out.println("Number of tested solutions: " + nbTested);
		return best;
	}

}
