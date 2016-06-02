package polytech.tours.di.parallel.tsp.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import polytech.tours.di.parallel.tsp.Algorithm;
import polytech.tours.di.parallel.tsp.Instance;
import polytech.tours.di.parallel.tsp.InstanceReader;
import polytech.tours.di.parallel.tsp.Solution;
import polytech.tours.di.parallel.tsp.TSPCostCalculator;

public class concurentSearchAlgo2 implements Algorithm {

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
		int nbThreads = 8;
		
		int calculationCounter = 0;
		
		//set distance Matrix in TSPCostCalculator
		TSPCostCalculator.setMatrix(instance.getDistanceMatrix());
		
		long startTime=System.currentTimeMillis();
		for(int i=0; i<instance.getN(); i++){
			s.add(i);
		}
		while((System.currentTimeMillis()-startTime)/1_000<=max_cpu){
			CopyOnWriteArrayList<threadSwap2> threadList = new CopyOnWriteArrayList<threadSwap2>();
			CopyOnWriteArrayList<Solution> solList = new CopyOnWriteArrayList<Solution>();
			
			for(int i = 0; i<nbThreads && solList.size()<=nbThreads; i++)
			{
				Collections.shuffle(s,rnd);
				threadSwap2 th = new threadSwap2(s.clone(), startTime+max_cpu*1000);
				new Thread(th).start();;
				threadList.add(th);
			}
			
			for(threadSwap2 th : threadList)
			{
				if(th.isFinished())
				{
					solList.add(th.getSol());
					threadList.remove(th);
					calculationCounter++;
				}
			}
			
			if(best == null && !solList.isEmpty())
				best=solList.get(0);
			
			for(Solution loopSol : solList)
			{
				if(loopSol.getOF() < best.getOF())
					best=loopSol;
				System.out.println(loopSol);
				solList.remove(loopSol);
			}
		}
		System.out.println("================BEST==================");
		System.out.println("Number of Calculation :"+calculationCounter);
		//return the solution
		return best;
	}
}
