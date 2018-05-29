package test;

import core.BoardConfig;
import location.XY;

public class DummyBoardConfig extends BoardConfig {

    private final XY size;

    private final int numberOfGoodPlants;
    private final int numberOfBadPlants;
    private final int numberOfBadBeasts;
    private final int numberOfGoodBeasts;
    private final int numberOfRandomWalls;
    private final int numberOfWalls;
    private final int totalEntities;

    public DummyBoardConfig() {
        size = new XY(10, 10);

        numberOfGoodPlants = 0;
        numberOfBadPlants = 0;
        numberOfBadBeasts = 0;
        numberOfGoodBeasts = 0;
        numberOfRandomWalls = 0;
        numberOfWalls = 0;
        totalEntities = numberOfGoodPlants + numberOfBadPlants +
                        numberOfGoodBeasts + numberOfBadBeasts +
                        numberOfWalls;
    }

    public int getNumberOfBadPlants() {
        return this.numberOfBadPlants;
    }

    public int getNumberOfBadBeasts() {
        return this.numberOfBadBeasts;
    }

    public int getNumberOfGoodBeasts() {
        return this.numberOfGoodBeasts;
    }

    public int getNumberOfRandomWalls() {
        return this.numberOfRandomWalls;
    }

    public int getNumberOfWalls() {
        return this.numberOfWalls;
    }

    public int getTotalEntities() {
        return this.totalEntities;
    }

    public XY getSize() {
        return this.size;
    }

    public int getNumberOfGoodPlants() {
        return this.numberOfGoodPlants;
    }
}
