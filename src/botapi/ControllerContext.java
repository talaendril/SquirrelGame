package botapi;

import core.EntityType;
import location.XY;

public interface ControllerContext {

	XY getViewLowerLeft();
	
	XY getViewUpperRight();
	
	EntityType getEnityAt(XY xy);
	
	void move(XY direction);
	
	void spawnMiniBot(XY direction, int energy);
	
	int getEnergy();
}
