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
	
	@Override
	public void nextStep(EntityContext context, MoveCommand command) {
		if(this.getStunnedAndDecrement()) {
			return;
		}
		context.tryMove(this, XY.getVector(command));
	}
}
