package core;

import location.XY;

public class BoardConfig {
	
	private final XY size = new XY(28, 28);
	
	private final int numberOfGoodPlants = 20;
	private final int numberOfBadPlants = 15;
	private final int numberOfBadBeasts = 9;
	private final int numberOfGoodBeasts = 25;
	private final int numberOfRandomWalls = 40;
	private final int numberOfWalls = 2 * size.x + 2 * size.y - 4 + numberOfRandomWalls;
	private final int totalEntities = 
			numberOfGoodPlants + numberOfBadPlants +
			numberOfGoodBeasts + numberOfBadBeasts +
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