package entities;

import core.EntityContext;
import location.XY;
import ui.commandhandle.MoveCommand;

public class BadPlant extends Entity {
	
	private final static int startEnergy = -100;
	
	public BadPlant(int id, XY location) {
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
		return "| BadPlant" + super.toString();
	}
	
	@Override
	public void nextStep(EntityContext context, MoveCommand command) {
		//nothing to do
	}
}
