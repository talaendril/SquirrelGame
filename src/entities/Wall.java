package entities;

import location.XY;

public class Wall extends Entity {
	
	public Wall(int id, XY location) {
		super(id, -10, location);
	}
	
	@Override
	public String toString() {
		return "| Wall" + super.toString();
	}
	
	@Override
	public void nextStep() {
		//nothing to do
	}
}
