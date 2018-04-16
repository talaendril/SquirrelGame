package core;

import ui.MoveCommand;
import ui.UI;

public class Game {
	
	private State state;
	private Board board;
	private UI ui;
	private MoveCommand nextCommand;
	
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
		ui.render(board.flatten());
		System.out.println(board.toString());
	}
	
	protected void processInput() {
		//nextCommand = ui.getCommand();
	}
	
	protected void update() {
		//board.callNextStep(EntityType.HANDOPERATEDMASTERSQUIRREL, nextCommand);
		board.callNextStep();
	}
}
