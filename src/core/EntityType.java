package core;

import entities.*;

public enum EntityType {
	GOOD_BEAST, BAD_BEAST, 
	GOOD_PLANT, BAD_PLANT, 
	MASTER_SQUIRREL, MINI_SQUIRREL, 
	WALL, NONE;
	
	public static EntityType getEntityType(Entity e) {
		if(e instanceof GoodBeast) {
			return EntityType.GOOD_BEAST;
		} else if(e instanceof BadBeast) {
			return EntityType.BAD_BEAST;
		} else if(e instanceof GoodPlant) {
			return EntityType.GOOD_PLANT;
		} else if(e instanceof BadPlant) {
			return EntityType.BAD_PLANT;
		} else if(e instanceof MiniSquirrel) {
			return EntityType.MINI_SQUIRREL;
		} else if(e instanceof Wall) {
			return EntityType.WALL;
		} else if(e instanceof MasterSquirrel) {
			return EntityType.MASTER_SQUIRREL;
		} else {
			return NONE;
		}
	}

}
