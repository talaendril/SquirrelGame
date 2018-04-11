package entities;

import location.XY;

public class GoodPlant extends Entity {

	public GoodPlant(int id, XY location) {
		super(id, 100, location);
	}
	
	@Override
	public String toString() {
		return "| GoodPlant" + super.toString();
	}
	
	@Override
	public void nextStep() {
		//nothing to do
	}
}
