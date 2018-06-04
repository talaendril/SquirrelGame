package core;

import ui.commandhandle.MoveCommand;

public class State {
	
	private Board board;

	public State(Board board) {
		this.board = board;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public void update(MoveCommand command) {
		this.board.nextStep(command);
	}
}
