package ui;

import java.util.Random;

public enum MoveCommand {
	UP, DOWN, RIGHT, LEFT, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT, NONE;
	
	public static MoveCommand getMoveCommand(int number) {
		if(number == 1) {
			return DOWN_LEFT;
		} else if(number == 2) {
			return DOWN;
		} else if(number == 3) {
			return DOWN_RIGHT;
		} else if(number == 4) {
			return LEFT;
		} else if(number == 6) {
			return RIGHT;
		} else if(number == 7) {
			return UP_LEFT;
		} else if(number == 8) {
			return UP;
		} else if(number == 9) {
			return UP_RIGHT;
		} else {
			return NONE;
		}
	}
	
	public static MoveCommand getRandomCommand() {
		while(true) {
			int random = new Random().nextInt(9) + 1;
			if(random != 5) {
				return getMoveCommand(random);
			}
		}
	}
}
