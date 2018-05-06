package ui;

import core.BoardView;
import ui.commandhandle.Command;

public interface UI {

	void render(BoardView view);
	
	Command getCommand();
	
	void message(final String msg);
}
