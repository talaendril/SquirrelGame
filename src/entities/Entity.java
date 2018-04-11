package entities;

import location.XY;

public abstract class Entity {

	private int id;
	private int energy;
	private XY location;
	private XY nextLocation;
	
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
	
	public XY getNextLocation() {
		return this.nextLocation;
	}
	
	@Override
	public String toString() {
		return " ID: " + this.id + " | Energy: " + this.energy + " | Location: " + this.location.toString();
	}
	
	public void setLocation(XY location) {
		if(location != null) {
			this.location = location;
		}
	}
	
	public void setNextLocation(XY location) {
		if(location != null) {
			this.nextLocation = location;
		}
	}
	
	public void updateEnergy(int delta) {
		if(this instanceof Squirrel) {
			this.energy -= delta;
		}
	}
	
	public XY tryNextStep() {
		return this.nextLocation = new XY(this.location, XY.getVector(XY.randomNumber()));
	}
	
	public void nextStep() {	//might need to be abstract
		this.location = this.nextLocation;
	}
	
	public boolean equals(Entity entity) {
		return (this.getClass() == entity.getClass() && this.id == entity.getID() && this.energy == entity.getEnergy() && this.location.equals(entity.getLocation()));
	}
}
