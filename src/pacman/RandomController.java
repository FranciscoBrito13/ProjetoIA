package pacman;

import utils.GameController;

public class RandomController implements GameController {

	@Override
	public int nextMove(int[] currentState) {
		return (int) (Math.random()*5);
	}

	@Override
	public double getCachedFitness() {
		return 0;
	}

}
