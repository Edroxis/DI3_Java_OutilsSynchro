package polytech.tours.di.parallel.tsp;

import java.util.ArrayList;

/**
 * Computes the cost of a TSP solution
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 *
 */
public class TSPCostCalculator{
	
	private ArrayList<Integer> solution;
	private static double[][] distMatrix;
	
	public TSPCostCalculator(Instance instance){
		distMatrix=instance.getDistanceMatrix();
	}
	
	public TSPCostCalculator(double[][] matrix){
		distMatrix=matrix;
	}
	
	public TSPCostCalculator(){}

	public static void setMatrix(double[][] distanceMatrix) {
		distMatrix = distanceMatrix;
	}
	
	/**
	 * Computes the objective function of a TSP tour
	 * @param instance the instance data
	 * @param s the solution
	 * @return the objective function of solution <code>s</code>
	 */
	public double calcOF(Solution s){
		solution = s;
		return calc();
	}
	/**
	 * static access to the calculator
	 * @param matrix the distance matrix
	 * @param s the TSP solution (permutation)
	 * @return the cost of <code>s</code>
	 */
	public double calcOF(ArrayList<Integer> s){
		solution = s;
		return calc();
	}

	private static double getDist(int i, int j) {
		return distMatrix[i][j];
	}

	/**
	 * internal implementation of the calculator
	 * @return the cost of a TSP solution
	 */
	private double calc(){
		double cost=0;
		for(int i=1;i<solution.size();i++){
			cost=cost+distMatrix[solution.get(i-1)][solution.get(i)];
		}
		cost=cost+distMatrix[solution.get(solution.size()-1)][solution.get(0)];
		return cost;
	}
	
	public static double diffPartialCostCalcSwap(Solution sol, int i, int j){
		if(i==0 && j==sol.size()-1)
		{
			int tmp = j;
			j = i;
			i = tmp;
		}
		
		int pI = i-1, sI = i+1, pJ = j-1, sJ = j+1;
		double partialCost = 0;
		
		if(pI == -1)
			pI = sol.size()-1;
		if(pJ == -1)
			pJ = sol.size()-1;
		if(sJ == sol.size())
			sJ = 0;
		if(sI == sol.size())
			sI = 0;
		
		partialCost += getDist(sol.get(pI),sol.get(i));
		if(pI != j && sI != j){
			partialCost += getDist(sol.get(i), sol.get(sI));
			partialCost += getDist(sol.get(pJ), sol.get(j));
			
			partialCost -= getDist(sol.get(j), sol.get(sI)); 
			partialCost -= getDist(sol.get(pJ), sol.get(i));
		}
		partialCost += getDist(sol.get(j), sol.get(sJ));
		
		partialCost -= getDist(sol.get(pI),sol.get(j));
		partialCost -= getDist(sol.get(i), sol.get(sJ));
		
		return partialCost;
	}

}
