package core;

import location.XY;

public class BoardConfig {
	
	//not sure about this implementation
	//TODO think about something else
	
	private final XY size = new XY(10, 10);
	
	private final int numberOfGoodPlants = 1;
	private final int numberOfBadPlants = 1;
	private final int numberOfBadBeasts = 2;
	private final int numberOfGoodBeasts = 1;
	private final int numberOfHandOperatedMasterSquirrels = 1;
	private final int numberOfMasterSquirrels = 0 + numberOfHandOperatedMasterSquirrels;
	private final int numberOfRandomWalls = 3;
	private final int numberOfWalls = 2 * size.getX() + 2 * size.getY() - 4 + numberOfRandomWalls;
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