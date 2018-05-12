package entities;

import location.XY;

public class HandOperatedMasterSquirrel extends MasterSquirrel {

	public HandOperatedMasterSquirrel(int id, XY location) {
		super(id, location);
	}
	
	@Override
	public String toString() {
		return "OPERATED " + super.toString();
	}
}