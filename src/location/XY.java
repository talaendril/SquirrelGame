package location;

import java.util.Random;

import ui.commandhandle.MoveCommand;

public final class XY {
	
	private final int x;
	private final int y;
	
	public static final XY ZERO_ZERO = new XY(0, 0);
    public static final XY RIGHT = new XY(1, 0);
    public static final XY LEFT = new XY(-1, 0);
    public static final XY UP = new XY(0, -1);
    public static final XY DOWN = new XY(0, 1);
    public static final XY RIGHT_UP = new XY(1, -1);
    public static final XY RIGHT_DOWN = new XY(1, 1);
    public static final XY LEFT_UP = new XY(-1, -1);
    public static final XY LEFT_DOWN = new XY(-1, 1);
	
	public XY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public XY plus(XY xy) {
		return new XY(this.x + xy.x, this.y + xy.y);
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public boolean equals(XY location) {
		return (location.getX() == this.x && location.getY() == this.y);
	}
	
	@Override
	public String toString() {
		return this.x + " " + this.y;
	}
	
	public static XY getVector(MoveCommand command) {
		switch(command) {
		case DOWN_LEFT:
			return LEFT_DOWN;
		case DOWN:
			return DOWN;
		case DOWN_RIGHT:
			return RIGHT_DOWN;
		case LEFT:
			return LEFT;
		case RIGHT:
			return RIGHT;
		case UP_LEFT:
			return LEFT_UP;
		case UP:
			return UP;
		case UP_RIGHT:
			return RIGHT_UP;
		default:
			return ZERO_ZERO;
		}
	}
	
	public static XY getRandomLocationBetween(int maxX, int maxY) {
		return new XY(new Random().nextInt(maxX), new Random().nextInt(maxY));
	}
	
	public static double distanceBetween(XY first, XY second) {
		int deltaX = Math.abs(first.getX() - second.getX());
		int deltaY = Math.abs(first.getY() - second.getY());
		return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
	}
	
	public static XY invertVector(XY vector) {
		return new XY(vector.getX() * -1, vector.getY() * -1);
	}
	
	public static XY getVectorBetween(XY location, XY direction) {
		int deltaX = direction.getX() - location.getX();
		int deltaY = direction.getY() - location.getY();
		return new XY(normalize(deltaX), normalize(deltaY));
	}
	
	public static int normalize(int number) {
		if(number < 0) {
			return -1;
		}
		if(number > 0) {
			return 1;
		}
		return 0;
	}
}
