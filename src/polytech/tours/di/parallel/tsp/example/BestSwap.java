package polytech.tours.di.parallel.tsp.example;

import polytech.tours.di.parallel.tsp.Solution;

public class BestSwap {

	private Solution solution;
	private long endExecution; // timeout
	private boolean isModified;

	BestSwap(Solution sol, long timeout) {
		this.solution = sol;
		this.endExecution = timeout;
		isModified = true;
	}

	public synchronized void checkBetterSolution(Solution sol) {
		if (sol.getOF() < solution.getOF()) {
			solution = sol;
			isModified = true;
		}
	}

	public void initIsModified() {
		isModified = false;
	}
	
	public boolean isModified(){
		return isModified;
	}

	public Solution getSol() {
		return solution;
	}

	public long getTimeout() {
		return endExecution;
	}
}
