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
		
		int calculationCounter = 0;
		
		//set distance Matrix in TSPCostCalculator
		TSPCostCalculator.setMatrix(instance.getDistanceMatrix());
		
		long startTime=System.currentTimeMillis();
		for(int i=0; i<instance.getN(); i++){
			s.add(i);
		}
		while((System.currentTimeMillis()-startTime)/1_000<=max_cpu){	
			ArrayList<Future<Solution>> solList = new ArrayList<Future<Solution>>();
			ArrayList<threadSwap> threadList = new ArrayList<threadSwap>();
			ArrayList<TSPCostCalculator> costCalcList = new ArrayList<TSPCostCalculator>();
			ExecutorService execute = Executors.newSingleThreadExecutor();
			
			for(int i = 0; i<8; i++)
			{
				Collections.shuffle(s,rnd);
				threadList.add(new threadSwap(s.clone()));
				solList.add(execute.submit(threadList.get(i)));	
				costCalcList.add(new TSPCostCalculator());
				calculationCounter++;
			}
			
			for(int i = 0; i<8; i++)
			{
				Solution temp = null;
				try {
					temp = solList.get(i).get(max_cpu - (System.currentTimeMillis()-startTime)/1_000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}catch (TimeoutException e) {
					System.out.println("Not enough time to finish this one!");
				}
				//temp.setOF(costCalcList.get(i).calcOF(temp));
				//System.out.println(temp);

			}
			
			//set the objective function of the solution
			
			try {
				best=solList.get(0).get();
			} catch (InterruptedException | ExecutionException e1) {
				e1.printStackTrace();
			}
			
			for(int i = 0; i<7; i++)
			{
				try {
					if(solList.get(i).get().getOF() < best.getOF())
						best=solList.get(i).get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			execute.shutdownNow();
		}
		System.out.println("Number of Calculation :"+calculationCounter);
		//return the solution
		return best;
	}

}
