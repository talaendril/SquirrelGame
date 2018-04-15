package core;

public class State {
	
	private int highscore = 0;
	private Board board;

	public State(Board board) {
		
	}
	
	public void update() {
		
	}
	
	public FlattenedBoard flattenedBoard() {
		return board.flatten();
	}
}
