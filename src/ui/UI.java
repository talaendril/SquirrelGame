package ui;

import core.BoardView;
import location.XY;
import ui.commandhandle.Command;

public interface UI {

	void render(BoardView view);
	
	Command getCommand();
	
	void message(final String msg);
	
	void implode(XY location, int impactRadius);
}
