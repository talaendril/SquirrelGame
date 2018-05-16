package entities;

import core.EntityContext;
import location.XY;
import ui.commandhandle.MoveCommand;

public class GoodPlant extends Entity {
	
	private static final int startEnergy = 100;

	public GoodPlant(int id, XY location) {
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
		return "| GoodPlant" + super.toString();
	}
	
	@Override
	public void nextStep(EntityContext context, MoveCommand command) {
		//nothing to do
	}
}
