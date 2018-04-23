package entities;

import core.EntityContext;
import location.XY;
import ui.CommandHandle.MoveCommand;

public class GoodPlant extends Entity {

	public GoodPlant(int id, XY location) {
		super(id, 100, location);
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
