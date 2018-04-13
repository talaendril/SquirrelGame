package core;

import entities.Entity;
import entities.Wall;
import location.XY;

public class EntitySet {
	
	private Entity[] entities;
	private int currentArrayPosition = 0;
	
	public EntitySet(int ArraySize) {
		entities = new Entity[ArraySize];
	}
	
	public Entity[] doubleArraySize() {
		Entity[] newArray = new Entity[entities.length * 2];
		for(int i = 0; i < entities.length; i++) {
			newArray[i] = entities[i];
		}
		return newArray;
	}
	
	public void addEntity(Entity entity) {
		if(entity != null) {
			if(currentArrayPosition == entities.length) {
				entities = this.doubleArraySize();
			}
			entities[currentArrayPosition++] = entity;
		}
	}
	
	public void removeEntity(Entity entity) {
		for(int i = 0; i < entities.length; i++) {
			if(entities[i] != null && entities[i].equals(entity)) {
				for(int j = i; j < entities.length - 1; j++) {
					entities[j] = entities[j+1];
				}
			}
		}
	}
	
	@Override
	public String toString() {
		String string = "";
		for(Entity e : entities) {
			if (e != null) {
				if (!(e instanceof Wall)) {
					string += e.toString() + "\n";
				} 
			}
		}
		return string;
	}
	
	
	public Entity[] getEntities() {
		return this.entities;
	}
	
	public Entity getEntity(XY location) {
		for(Entity e : entities) {
			if(e != null) {
				if(e.getLocation().equals(location)) {
					return e;
				}
			}
		}
		return null;
	}
}
