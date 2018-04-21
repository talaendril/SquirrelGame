package core;

import ui.Command;
import ui.GameCommandProcessor;

public class State {
	
	//private int highscore = 0;
	private Board board;
	private GameCommandProcessor gcp;

	public State(Board board) {
		this.board = board;
		gcp = new GameCommandProcessor(this.board);
	}
	
	public void update(Command command) {
		gcp.process(command);
	}
	
	public FlattenedBoard flattenedBoard() {
		return board.flatten();
	}
}
