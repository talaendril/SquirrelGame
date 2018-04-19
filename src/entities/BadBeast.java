package entities;

import core.EntityContext;
import location.XY;
import ui.MoveCommand;

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
		return "| BadBeast" + super.toString() + " Current Stepcount: " + this.stepCounter;
	}
	
	public int getBiteCounterAndIncrement() {
		return this.biteCounter++;
	}
	
	public int getStepCounterAndIncrement() {
		if(this.stepCounter == 4) {
			this.stepCounter = 0;
		}
		return this.stepCounter++;
	}
	
	@Override
	public void nextStep(EntityContext context, MoveCommand command) {
		context.tryMove(this, XY.getVector(command));
	}
}
