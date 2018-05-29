package core;

import location.XY;

public class BoardConfig {
	
	private final XY size;
	
	private final int numberOfGoodPlants;
	private final int numberOfBadPlants;
	private final int numberOfBadBeasts;
	private final int numberOfGoodBeasts;
	private final int numberOfRandomWalls;
	private final int numberOfWalls;
	private final int totalEntities;
	
	public BoardConfig() {
		size = new XY(28, 28);

		numberOfGoodPlants = 20;
		numberOfBadPlants = 15;
		numberOfBadBeasts = 9;
		numberOfGoodBeasts = 25;
		numberOfRandomWalls = 40;
		numberOfWalls = 2 * size.x + 2 * size.y - 4 + numberOfRandomWalls;
		totalEntities = numberOfGoodPlants + numberOfBadPlants +
						numberOfGoodBeasts + numberOfBadBeasts +
						numberOfWalls;
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