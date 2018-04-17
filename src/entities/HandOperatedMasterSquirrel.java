package entities;

import core.EntityContext;
import location.XY;
import ui.MoveCommand;

public class HandOperatedMasterSquirrel extends MasterSquirrel {

	public HandOperatedMasterSquirrel(int id, XY location) {
		super(id, location);
	}
	
	@Override
	public String toString() {
		return "OPERATED " + super.toString();
	}
	
	public void nextStepHOMS(EntityContext context, MoveCommand command) {
		context.tryMove(this, XY.getVector(command));
	}
	
	@Override
	public void nextStep(EntityContext context) {
		//nothing to do here, look at nextStepHOMS
	}
	
	public void nextStep(EntityContext context, MoveCommand command) {
		context.tryMove(this, XY.getVector(command));
	}
}
