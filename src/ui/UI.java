package ui;

import core.BoardView;

public interface UI {

	void render(BoardView view);
	
	Command getCommand();
}
