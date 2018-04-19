package entities;

import core.EntityContext;
import location.XY;
import ui.MoveCommand;

public class BadPlant extends Entity {

	public BadPlant(int id, XY location) {
		super(id, -100, location);
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
