package entities;

import location.XY;

public class GoodBeast extends Entity {

	public GoodBeast(int id, XY location) {
		super(id, 200, location);
	}
	
	@Override
	public String toString() {
		return "| GoodBeast" + super.toString();
	}
}
