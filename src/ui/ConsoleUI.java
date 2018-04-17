package ui;

import java.io.IOException;

import core.BoardView;

public class ConsoleUI implements UI {
	
	public ConsoleUI() {
		
	}

	@Override
	public void render(BoardView view) {
		view.printBoard();
	}
	
	@Override
	public MoveCommand getCommand() {
		System.out.println("Gib eine Zahl zwischen [1-9] (5 bedeutet:\"do nothing\"):");
		int number = 0;
		try {
			number = System.in.read();
			while (number < '0' || number > '9') {
				number = System.in.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		number -= '0';
		switch(number) {
		case 1:
			return MoveCommand.DOWN_LEFT;
		case 2:
			return MoveCommand.DOWN;
		case 3:
			return MoveCommand.DOWN_RIGHT;
		case 4:
			return MoveCommand.LEFT;
		case 6:
			return MoveCommand.RIGHT;
		case 7:
			return MoveCommand.UP_LEFT;
		case 8:
			return MoveCommand.UP;
		case 9:
			return MoveCommand.UP_RIGHT;
		default:
			return MoveCommand.NONE;
		}
	}
}
