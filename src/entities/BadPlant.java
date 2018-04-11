package entities;

import location.XY;

public class BadPlant extends Entity {

	public BadPlant(int id, XY location) {
		super(id, -100, location);
	}
	
	@Override
	public String toString() {
		return "| BadPlant" + super.toString();
	}
	
	@Override
	public void nextStep() {
		//nothing to do
	}
}
