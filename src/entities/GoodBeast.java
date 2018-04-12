package entities;

import location.XY;

public class GoodBeast extends Character {
	
	private int stepCounter = 0;

	public GoodBeast(int id, XY location) {
		super(id, 200, location);
	}
	
	@Override
	public String toString() {
		return "| GoodBeast" + super.toString();
	}
	
	public int getStepCounter() {
		return this.stepCounter;
	}
	
	public void incrementStepCounter() {
		if(stepCounter == 4) {
			stepCounter = 0;
		} else {
			stepCounter++;
		}
	}
}
