package entities;

import location.XY;

public class BadBeast extends Entity {

	public BadBeast(int id, XY location) {
		super(id, -150, location);
	}
	
	@Override
	public String toString() {
		return "| BadBeast" + super.toString();
	}
}
