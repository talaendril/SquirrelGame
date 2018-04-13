package core;

import location.XY;

public class BoardConfig {
	
	//not sure about this implementation
	//TODO think about something else
	
	public static final XY size = new XY(10, 10);
	
	public static final int numberOfGoodPlants = 1;
	public static final int numberOfBadPlants = 1;
	public static final int numberOfBadBeasts = 2;
	public static final int numberOfGoodBeasts = 1;
	public static final int numberOfMasterSquirrels = 0;
	public static final int numberOfHandOperatedMasterSquirrels = 1;
	public static final int numberOfRandomWalls = 3;
	public static final int numberOfWalls = 2 * size.getX() + 2 * size.getY() - 4 + numberOfRandomWalls;
	public static final int totalEntities = 
			numberOfGoodPlants + numberOfBadPlants +
			numberOfGoodBeasts + numberOfBadBeasts +
			numberOfMasterSquirrels +
			numberOfWalls;
}