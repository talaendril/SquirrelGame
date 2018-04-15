package core;

import entities.*;

public enum EntityType {
	GOODBEAST, BADBEAST, 
	GOODPLANT, BADPLANT, 
	MASTERSQUIRREL, MINISQUIRREL, 
	WALL;
	
	public static EntityType getEntityType(Entity e) {
		if(e instanceof GoodBeast) {
			return EntityType.GOODBEAST;
		} else if(e instanceof BadBeast) {
			return EntityType.BADBEAST;
		} else if(e instanceof GoodPlant) {
			return EntityType.GOODPLANT;
		} else if(e instanceof BadPlant) {
			return EntityType.BADPLANT;
		} else if(e instanceof MasterSquirrel) {
			return EntityType.MASTERSQUIRREL;
		} else if(e instanceof MiniSquirrel) {
			return EntityType.MINISQUIRREL;
		} else if(e instanceof Wall) {
			return EntityType.WALL;
		} else {
			return null;
		}
	}

}
