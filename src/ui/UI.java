package ui;

import core.BoardView;
import ui.CommandHandle.Command;

public interface UI {

	void render(BoardView view);
	
	Command getCommand();
}
