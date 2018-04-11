package location;

import java.util.Random;

public final class XY {
	
	private final int x;
	private final int y;
	
	public static final XY ORIGIN = new XY(0,0);
	public static final XY UP = new XY(0, 1); //goes up in a coordinate system not in a matrix
	public static final XY DOWN = new XY(0, -1);
	public static final XY RIGHT = new XY(1, 0);
	public static final XY LEFT = new XY(-1, 0);
	public static final XY UP_RIGHT = new XY(1, 1);
	public static final XY UP_LEFT = new XY(-1, 1);
	public static final XY DOWN_RIGHT = new XY(1, -1);
	public static final XY DOWN_LEFT = new XY(-1, -1);
	
	public XY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public XY(XY location, XY vector) {
		int newX = location.getX() + vector.getX();
		int newY = location.getY() + vector.getY();
		this.x = (newX < 0) ? 0 : newX;
		this.y = (newY < 0) ? 0 : newY;
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
	
	public static int randomNumber() {
		return new Random().nextInt(9) + 1;
	}
	
	public static XY getVector(int number) {
		switch(number) {
		case 1:
			return DOWN_LEFT;
		case 2:
			return DOWN;
		case 3:
			return DOWN_RIGHT;
		case 4:
			return LEFT;
		case 6:
			return RIGHT;
		case 7:
			return UP_LEFT;
		case 8:
			return UP;
		case 9:
			return UP_RIGHT;
		default:
			return ORIGIN;
		}
	}	
	
	public static XY outOfBoundsLocation() {
		return new XY(-1, 1);
	}
}
