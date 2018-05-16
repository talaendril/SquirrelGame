package entities;

import core.EntityContext;
import location.XY;
import ui.commandhandle.MoveCommand;

public abstract class Entity {

	private int id;
	private int energy;
	private XY location;
	
	public Entity(int id, int energy, XY xy) {
		this.id = id;
		this.energy = energy;
		this.location = xy;
	}
	
	public abstract void nextStep(EntityContext context, MoveCommand command);
	
	public abstract void resetEnergy();
	
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
	
	public void updateEnergy(int delta) {
		this.energy += delta;
	}
	
	public void move(XY direction) {
		this.location = this.location.plus(direction);
	}
	
	public boolean equals(Entity entity) {
		return (this.getClass() == entity.getClass() && this.id == entity.getID() && this.energy == entity.getEnergy() && this.location.equals(entity.getLocation()));
	}
}
