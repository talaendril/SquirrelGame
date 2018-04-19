package entities;

import core.EntityContext;
import location.XY;
import ui.MoveCommand;

public class MiniSquirrel extends Squirrel {
	
	public static final int ENERGY_GAIN_NOT_MASTER = 150;
	
	private MasterSquirrel master = null;

	public MiniSquirrel(int id, int energy, XY location, MasterSquirrel master) {
		super(id, energy, location);
		this.master = master;
	}
	
	public MasterSquirrel getMaster() {
		return this.master;
	}
	
	@Override
	public String toString() {
		return "| MiniSquirrel" + super.toString();
	}
	
	@Override
	public void nextStep(EntityContext context, MoveCommand command) {
		if(this.getStunnedAndDecrement()) {
			return;
		}
		context.tryMove(this, XY.getVector(command));
	}
}
