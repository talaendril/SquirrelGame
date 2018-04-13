package entities;

import core.EntityContext;
import entities.Squirrel;
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
	
	@Override
	public String toString() {
		return " ID: " + this.id + " | Energy: " + this.energy + " | Location: " + this.location.toString();
	}
	
	public void setLocation(XY location) {
		if(location != null) {
			this.location = location;
		}
	}
	
	//boolean here returns if MiniSquirrel should die
	public boolean updateEnergy(int delta) {
		if(this instanceof Squirrel) {
			this.energy += delta;
			if(this.energy < 0 && this instanceof MiniSquirrel) {
				return false;
			}
		}
		return true;
	}
	
	public abstract void nextStep(EntityContext context);
	
	public void move(XY direction) {
		this.location = new XY(this.location, direction);
	}
	
	public boolean equals(Entity entity) {
		return (this.getClass() == entity.getClass() && this.id == entity.getID() && this.energy == entity.getEnergy() && this.location.equals(entity.getLocation()));
	}
}
