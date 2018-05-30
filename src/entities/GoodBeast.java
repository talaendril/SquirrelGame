package entities;

import core.EntityContext;
import location.XY;
import location.XYSupport;
import ui.commandhandle.MoveCommand;

public class GoodBeast extends Character {
	
	public static final int MAXIMUM_STEPCOUNT = 4;
	
	private static final int startEnergy = 200;
	private int stepCounter = 1;

	public GoodBeast(int id, XY location) {
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
		return "| GoodBeast" + super.toString() + " Current Stepcount: " + this.stepCounter;
	}

	public int getStepCount() {
	    return this.stepCounter;
    }

    public void incrementStepCount() {
	    this.stepCounter++;
    }

    public void resetStepCount() {
	    this.stepCounter = 0;
    }
	
	@Override
	public void nextStep(EntityContext context, MoveCommand command) {
		context.tryMove(this, XYSupport.getVector(command));
	}
}
