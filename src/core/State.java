package core;

import ui.MoveCommand;

public class State {
	
	//private int highscore = 0;
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
	
	public FlattenedBoard flattenedBoard() {
		return board.flatten();
	}
}
