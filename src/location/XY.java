package location;

public final class XY {
	
	public final int x;
	public final int y;
	
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
	
	public XY minus(XY xy) {
		return new XY(this.x + xy.x, this.y + xy.y);
	}
	
	public XY times(int factor) {
		//not certain about this impl
		return new XY(this.x * factor, this.y * factor);
	}
	
	public double length() {
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}
    /**
     * @param xy a second coordinate pair
     * @return the euklidian distance (pythagoras)
     */
    public double distanceFrom(XY xy) {
    	int deltaX = Math.abs(this.x - xy.x);
		int deltaY = Math.abs(this.y - xy.y);
		return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }
    
    public int hashCode() {
    	//TODO
    	return 0;
    }
    
    public boolean equals(Object obj) {
		if(obj instanceof XY) {
			XY location = (XY) obj;
			return (location.x == this.x && location.y == this.y);
		} 
		return false;
	}
	
	@Override
	public String toString() {
		return this.x + " " + this.y;
	}
}
