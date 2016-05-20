package polytech.tours.di.parallel.tsp;

import java.util.ArrayList;

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
		
		partialCostDiff += distMatrix[s.get(downI)][s.get(i)] + distMatrix[s.get(i)][s.get(upI)] + distMatrix[s.get(downJ)][s.get(j)] + distMatrix[s.get(j)][s.get(upJ)] -
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
	}

}
