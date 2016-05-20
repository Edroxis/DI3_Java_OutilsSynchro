package polytech.tours.di.parallel.tsp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Computes the cost of a TSP solution
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 *
 */
public class TSPCostCalculator{

	private ArrayList<Integer> s;
	private static double[][] distMatrix;

	public static void setMatrix(Instance instance){
		distMatrix=instance.getDistanceMatrix();
	}

	public static void setMatrix(double[][] matrix){
		distMatrix=matrix;
	}

	/**
	 * Computes the objective function of a TSP tour
	 * @param instance the instance data
	 * @param s the solution
	 * @return the objective function of solution <code>s</code>
	 */
	public double calcOF(Solution s){
		this.s=s;
		return calc();
	}
	/**
	 * static access to the calculator
	 * @param matrix the distance matrix
	 * @param s the TSP solution (permutation)
	 * @return the cost of <code>s</code>
	 */
	public double calcOF(ArrayList<Integer> s){
		this.s=s;
		return calc();
	}

	public boolean interestingSwap(Solution s, int i, int j){
		double partialCostDiff = 0;
		int downI = i-1, upI = i+1, downJ = j-1, upJ = j+1;

		if(downI<0)
			downI = s.size()-1;

		if(upJ == s.size())
			upJ = 0;

		if(j == i+1)
			partialCostDiff = distMatrix[s.get(downI)][s.get(i)] + distMatrix[s.get(j)][s.get(upJ)] - (distMatrix[s.get(downI)][s.get(j)] + distMatrix[s.get(i)][s.get(upJ)]);
		else
			partialCostDiff = distMatrix[s.get(downI)][s.get(i)] + distMatrix[s.get(i)][s.get(upI)] + distMatrix[s.get(downJ)][s.get(j)] + distMatrix[s.get(j)][s.get(upJ)] -
				( distMatrix[s.get(downI)][s.get(j)] + distMatrix[s.get(j)][s.get(upI)] + distMatrix[s.get(downJ)][s.get(i)] + distMatrix[s.get(i)][s.get(upJ)]);

		return  partialCostDiff < 0;
	}

	/**
	 * internal implementation of the calculator
	 * @return the cost of a TSP solution
	 */
	private double calc(){
		double cost=0;
		for(int i=1;i<s.size();i++){
			cost=cost+distMatrix[s.get(i-1)][s.get(i)];
		}
		cost=cost+distMatrix[s.get(s.size()-1)][s.get(0)];
		return cost;

 					
		
//		final double cost=0;
//		int cpus = Runtime.getRuntime().availableProcessors();
//		ExecutorService service = Executors.newFixedThreadPool(cpus);
//
//
//		List<Future<Double>> tasks = new ArrayList<>();
//		int blockSize = (s.size() + cpus - 1) / cpus;
//		for (int i=0; i < s.size()/blockSize; i++) {
//			int start = blockSize * i +1;
//			int end = Math.min(blockSize * (i+1), s.size());
//			tasks.add(service.submit(new Callable<Double>() {
//				public Double call() {
//					double cost= 0;
//					for(int j=start;j<end;j++)
//						cost=cost+distMatrix[s.get(j-1)][s.get(j)];
//					return cost;
//				}
//			}));
//		}
//		double sum = 0;
//		for(Future<Double> task: tasks)
//			try {
//				sum += task.get();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				e.printStackTrace();
//			}
//		service.shutdownNow();
//		return sum;
			
	}

}
