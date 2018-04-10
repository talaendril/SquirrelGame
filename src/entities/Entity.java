package entities;

import location.XY;

public abstract class Entity {

	private int id;
	private int energy;
	private XY location;
	
	public Entity(int id, int energy, XY xy) {
		this.id = id;
		this.energy = energy;
		this.location = xy;
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getEnergy() {
		return this.energy;
	}
	
	public XY getLocation() {
		return this.location;
	}
}
