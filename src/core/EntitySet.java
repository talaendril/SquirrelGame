package core;

import java.util.logging.Logger;

import entities.Entity;
import entities.Wall;

@Deprecated
public class EntitySet {
	
	private static final Logger LOGGER = Logger.getLogger(EntitySet.class.getName());
	
	private Entity[] entities;
	private int currentArrayPosition = 0;
	
	public EntitySet(int arraySize) {
		entities = new Entity[arraySize];
	}
	
	private Entity[] increaseArraySize() {
		Entity[] newArray = new Entity[(entities.length + 1) * 2];
        System.arraycopy(entities, 0, newArray, 0, entities.length);
		return newArray;
	}
	
	public void addEntity(Entity entity) {
		if(entity != null) {
			if(currentArrayPosition == entities.length) {
				entities = this.increaseArraySize();
			}
			entities[currentArrayPosition++] = entity;
			//LOGGER.info("added entity " + entity.toString());
		}
	}
	
	public void removeEntity(Entity entity) {
		for(int i = 0; i < entities.length; i++) {
			if(entities[i] != null && entities[i].equals(entity)) {
                System.arraycopy(entities, i + 1, entities, i, entities.length - 1 - i);
                return;
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

	public boolean containsEntity(Entity entity) {
	    for(Entity e : entities) {
	        if(e != null && e.equals(entity)) {
	            return true;
            }
        }
        return false;
    }

	public Entity[] getEntities() {
		return this.entities;
	}
}
