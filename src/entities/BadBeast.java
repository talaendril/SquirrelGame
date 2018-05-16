package entities;

import core.EntityContext;
import location.XY;
import location.XYSupport;
import ui.commandhandle.MoveCommand;

public class BadBeast extends Character {
	
	public static final int MAXIMUM_BITECOUNT = 6; 
	public static final int MAXIMUM_STEPCOUNT = 3;
	
	private static final int startEnergy = -150;
	
	private int stepCounter = 0;
	private int biteCounter = 0;

	public BadBeast(int id, XY location) {
		super(id, startEnergy, location);
	}
	
	@Override
	public void resetEnergy() {
		if(this.getEnergy() != startEnergy) {
			int diff = startEnergy - this.getEnergy();
			this.updateEnergy(diff);
		}
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
		context.tryMove(this, XYSupport.getVector(command));
	}
}
