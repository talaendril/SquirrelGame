package core;

import location.XY;

public class BoardConfig {
	
	private final XY size = new XY(25, 25);
	
	private final int numberOfGoodPlants = 7;
	private final int numberOfBadPlants = 3;
	private final int numberOfBadBeasts = 4;
	private final int numberOfGoodBeasts = 10;
	private final int numberOfHandOperatedMasterSquirrels = 0;	//we are adding MasterSquirrels in the constructor of a game so this is probbaly useless
	private final int numberOfMasterSquirrels = 0 + numberOfHandOperatedMasterSquirrels;
	private final int numberOfRandomWalls = 11;
	private final int numberOfWalls = 2 * size.x + 2 * size.y - 4 + numberOfRandomWalls;
	private final int totalEntities = 
			numberOfGoodPlants + numberOfBadPlants +
			numberOfGoodBeasts + numberOfBadBeasts +
			numberOfMasterSquirrels + 
			numberOfWalls;
	
	public BoardConfig() {
		
	}
	
	public int getNumberOfBadPlants() {
		return numberOfBadPlants;
	}

	public int getNumberOfBadBeasts() {
		return numberOfBadBeasts;
	}

	public int getNumberOfGoodBeasts() {
		return numberOfGoodBeasts;
	}

	public int getNumberOfHandOperatedMasterSquirrels() {
		return numberOfHandOperatedMasterSquirrels;
	}

	public int getNumberOfMasterSquirrels() {
		return numberOfMasterSquirrels;
	}

	public int getNumberOfRandomWalls() {
		return numberOfRandomWalls;
	}

	public int getNumberOfWalls() {
		return numberOfWalls;
	}

	public int getTotalEntities() {
		return totalEntities;
	}
	
	public XY getSize() {
		return this.size;
	}
	
	public int getNumberOfGoodPlants() {
		return this.numberOfGoodPlants;
	}
}