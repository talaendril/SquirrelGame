package entities;

import core.EntityContext;
import location.XY;
import ui.commandhandle.MoveCommand;

public class HandOperatedMasterSquirrel extends MasterSquirrel {

	public HandOperatedMasterSquirrel(int id, XY location) {
		super(id, location);
	}
	
	@Override
	public String toString() {
		return "OPERATED " + super.toString();
	}
	
	@Override
	public void nextStep(EntityContext context, MoveCommand command) {
		super.nextStep(context, command);
	}
}