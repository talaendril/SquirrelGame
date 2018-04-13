package core;

public class Game {
	
	public Game(State state) {
		
	}

	public static void main(String[] args) {
		Board board = new Board();
		board.printBoard();

		while(true) {
			board.callNextStep();
			System.out.println(board.toString());
		}
	}
	
	public void run() {
		while(true) {
			render();
			processInput();
			update();
		}
	}
	
	protected void render() {
		//TODO show output
	}
	
	protected void processInput() {
		//TODO works with user Input
	}
	
	protected void update() {
		//TODO updates current state of the game
	}
}
