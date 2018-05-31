package core;

import location.XY;

public class BoardConfig {
	
	private final XY size;

	private final int maximumSteps = 10;

	private final String[] botNames = {"randombot", "dijkstrabot"};
	
	private final int numberOfGoodPlants;
	private final int numberOfBadPlants;
	private final int numberOfBadBeasts;
	private final int numberOfGoodBeasts;
	private final int numberOfRandomWalls;
    private final int totalEntities;
	
	public BoardConfig() {
		size = new XY(28, 28);

		numberOfGoodPlants = 20;
		numberOfBadPlants = 15;
		numberOfBadBeasts = 9;
		numberOfGoodBeasts = 25;
		numberOfRandomWalls = 40;
        int numberOfWalls = 2 * size.x + 2 * size.y - 4 + numberOfRandomWalls;
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

	public int getTotalEntities() {
		return totalEntities;
	}

	public int getMaximumSteps() {
	    return this.maximumSteps;
    }
	
	public XY getSize() {
		return this.size;
	}

	public String[] getBotNames() {
	    return this.botNames;
    }
	
	public int getNumberOfGoodPlants() {
		return this.numberOfGoodPlants;
	}
}