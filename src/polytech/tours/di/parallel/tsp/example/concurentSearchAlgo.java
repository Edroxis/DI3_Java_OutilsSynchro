package polytech.tours.di.parallel.tsp.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import polytech.tours.di.parallel.tsp.Algorithm;
import polytech.tours.di.parallel.tsp.Instance;
import polytech.tours.di.parallel.tsp.InstanceReader;
import polytech.tours.di.parallel.tsp.Solution;
import polytech.tours.di.parallel.tsp.TSPCostCalculator;

public class concurentSearchAlgo implements Algorithm {

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
		//set nb of threads
		int nbThreads = 64;
		
		int calculationCounter = 0;
		
		//set distance Matrix in TSPCostCalculator
		TSPCostCalculator.setMatrix(instance.getDistanceMatrix());
		
		long startTime=System.currentTimeMillis();
		for(int i=0; i<instance.getN(); i++){
			s.add(i);
		}
		while((System.currentTimeMillis()-startTime)/1_000<=max_cpu){	
			ArrayList<Future<Solution>> futureSolList = new ArrayList<Future<Solution>>();
			ArrayList<threadSwap> threadList = new ArrayList<threadSwap>();
			ArrayList<Solution> solList = new ArrayList<Solution>();
			ExecutorService execute = Executors.newSingleThreadExecutor();
			
			for(int i = 0; i<nbThreads; i++)
			{
				Collections.shuffle(s,rnd);
				threadList.add(new threadSwap(s.clone()));
				futureSolList.add(execute.submit(threadList.get(i)));	
			}
			
			for(int i = 0; i<nbThreads; i++)
			{
				Solution temp = null;
				try {
					temp = futureSolList.get(i).get(max_cpu - (System.currentTimeMillis()-startTime)/1_000, TimeUnit.SECONDS);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}catch (TimeoutException e) {
					System.out.println("Not enough time to finish this one!");
				}
				if(temp!=null)
				{
					solList.add(temp);
					System.out.println(temp);
					calculationCounter++;
				}
			}
			
			if(best == null)
				best=solList.get(0);
			
			for(Solution sol : solList)
				if(sol.getOF() < best.getOF())
					best=sol;
			
			execute.shutdownNow();
		}
		System.out.println("Number of Calculation :"+calculationCounter);
		//return the solution
		return best;
	}

}
