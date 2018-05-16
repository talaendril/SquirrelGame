package core;

import entities.BadBeast;
import entities.Entity;
import entities.GoodBeast;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import entities.Squirrel;
import location.XY;

public interface EntityContext {

	XY getSize();
	
	void tryMove(MiniSquirrel ms, XY direction);
	
	void tryMove(GoodBeast gb, XY direction);
	
	void tryMove(BadBeast bb, XY direction);
	
	void tryMove(MasterSquirrel master, XY direction);
	
	Squirrel nearestPlayerEntity(XY pos);
	
	void addMiniSquirrel(MiniSquirrel ms);
	
	void kill(Entity entity);
	
	void killAndReplace(Entity entity);
	
	EntityType getEntityType(XY xy);
	
	Entity getEntity(XY xy);
	
	void implode(MiniSquirrel ms, int impactRadius);
}
