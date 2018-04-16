package core;

import location.XY;

public interface BoardView {

	EntityType getEntityType(int x, int y);
	
	XY getSize();
	
	void printBoard();
}
