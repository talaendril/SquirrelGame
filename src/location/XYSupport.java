package location;

import java.util.Random;

import ui.commandhandle.MoveCommand;

public class XYSupport {

	public static int normalize(int number) {
		if(number < 0) {
			return -1;
		}
		if(number > 0) {
			return 1;
		}
		return 0;
	}
	
	public static XY normalize(XY vector) {
		return new XY(normalize(vector.x), normalize(vector.y));
	}
	
	public static boolean isInvalid(XY location) {
		return location.x < 0 || location.y < 0;
	}

	public static XY getVectorBetween(XY location, XY direction) {
		int deltaX = direction.x - location.x;
		int deltaY = direction.y - location.y;
		return new XY(normalize(deltaX), normalize(deltaY));
	}

	public static XY getRandomLocationBetween(int maxX, int maxY) {
		return new XY(new Random().nextInt(maxX), new Random().nextInt(maxY));
	}

	public static XY getVector(MoveCommand command) {
		switch(command) {
		case DOWN_LEFT:
			return XY.LEFT_DOWN;
		case DOWN:
			return XY.DOWN;
		case DOWN_RIGHT:
			return XY.RIGHT_DOWN;
		case LEFT:
			return XY.LEFT;
		case RIGHT:
			return XY.RIGHT;
		case UP_LEFT:
			return XY.LEFT_UP;
		case UP:
			return XY.UP;
		case UP_RIGHT:
			return XY.RIGHT_UP;
		default:
			return XY.ZERO_ZERO;
		}
	}
}