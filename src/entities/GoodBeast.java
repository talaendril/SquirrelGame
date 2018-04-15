package entities;

import core.EntityContext;
import location.XY;

public class GoodBeast extends Character {
	
	public static final int MAXIMUM_STEPCOUNT = 3;
	
	private int stepCounter = 0;

	public GoodBeast(int id, XY location) {
		super(id, 200, location);
	}
	
	@Override
	public String toString() {
		return "| GoodBeast" + super.toString() + " Current Stepcount: " + this.stepCounter;
	}
	
	public int getStepCounter() {
		if(this.stepCounter == 4) {
			this.stepCounter = 0;
		}
		return this.stepCounter++;
	}
	
	@Override
	public void nextStep(EntityContext context) {
		context.tryMove(this, XY.getVector(XY.randomNumber()));
	}
}
