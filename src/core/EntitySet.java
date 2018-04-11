package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.Entity;
import entities.GoodPlant;
import entities.MasterSquirrel;
import entities.Wall;
import location.XY;

public class EntitySet {
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	public EntitySet() {
		
	}
	
	public void addEntity(Entity entity) {
		if(entity != null) {
			entities.add(entity);
		}
	}
	
	public void removeEntity(Entity entity) {
		if(entities.contains(entity)) {
			entities.remove(entity);
		}
	}
	
	@Override
	public String toString() {
		String string = "";
		Iterator<Entity> iterator = entities.iterator();
		while(iterator.hasNext()) {
			Entity e = iterator.next();
			if(!(e instanceof Wall)) {
				string += e.toString() + "\n";
			}
		}
		return string;
	}
	
	public void callNextStep() {
		Entity toBeRemoved = null;
		Iterator<Entity> iterator = entities.iterator();
		while(iterator.hasNext()) {
			Entity e = iterator.next();
			if(e instanceof MasterSquirrel) {
				toBeRemoved = this.getEntity(e.tryNextStep());
				if(toBeRemoved != null && toBeRemoved instanceof GoodPlant) {
					e.updateEnergy(toBeRemoved.getEnergy());
					toBeRemoved.setLocation(XY.outOfBoundsLocation());
				}
			}
			e.nextStep();
		}
		this.removeEntity(toBeRemoved);
	}
	
	public List<Entity> getEntities() {
		return this.entities;
	}
	
	public Entity getEntity(XY location) {
		Iterator<Entity> iterator = entities.iterator();
		while(iterator.hasNext()) {
			Entity e = iterator.next();
			if(e.getLocation().equals(location)) {
				return e;
			}
		}
		return null;
	}
}
