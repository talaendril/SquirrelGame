package entities;

import core.EntityContext;
import location.XY;

public class BadBeast extends Character {
	
	public static final int MAXIMUM_BITECOUNT = 6; 
	public static final int MAXIMUM_STEPCOUNT = 3;
	
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
		if(stepCounter == 4) {
			stepCounter = 0;
		}
		return this.stepCounter++;
	}
	
	public int getBiteCounter() {
		return this.biteCounter++;
	}
	
	@Override
	public void nextStep(EntityContext context) {
		context.tryMove(this, XY.getVector(XY.randomNumber()));
	}
}
