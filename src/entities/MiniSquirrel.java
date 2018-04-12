package entities;

import location.XY;

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
}
