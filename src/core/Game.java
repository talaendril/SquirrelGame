package core;

import ui.Command;
import ui.UI;

public class Game {
	
	private State state;
	private Board board;
	private UI ui;
	private Command nextCommand;
	
	public Game(State state, Board board, UI ui) {
		this.state = state;
		this.board = board;
		this.ui = ui;
	}
	
	public void run() {
		while(true) {
			render();
			processInput();
			update();
		}
	}
	
	protected void render() {
		System.out.println("");
		ui.render(board.flatten());
		System.out.println("");
		System.out.println(board.toString());
	}
	
	protected void processInput() {
		//TODO maybe find a way to process the input we've gotten here instead of in State via GameCommandProcessor
		this.nextCommand = this.ui.getCommand();
	}
	
	protected void update() {
		state.update(nextCommand);
	}
}
