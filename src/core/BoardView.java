package core;

import entities.Entity;
import location.XY;

public interface BoardView {

	EntityType getEntityType(int x, int y);
	
	XY getSize();
	
	Entity[][] getEntityMatrix();
}
