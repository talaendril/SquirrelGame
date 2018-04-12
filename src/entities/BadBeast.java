package entities;

import location.XY;

public class BadBeast extends Character {
	
	public static final int MAXIMUM_BITECOUNT = 7;
	
	private int stepCounter = 0;
	private int biteCounter = 0;

	public BadBeast(int id, XY location) {
		super(id, -150, location);
	}
	
	@Override
	public String toString() {
		return "| BadBeast" + super.toString();
	}
	
	public int getStepCounter() {
		return this.stepCounter;
	}
	
	public int getBiteCounter() {
		return this.biteCounter;
	}
	
	public void incrementBiteCounter() {
		this.biteCounter++;
	}
	
	public void incrementStepCounter() {
		if(stepCounter == 4) {
			stepCounter = 0;
		} else {
			stepCounter++;
		}
	}
}
