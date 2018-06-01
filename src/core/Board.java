package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.*;
import entities.Character;
import exceptions.BelowThresholdException;
import exceptions.EntityNotFoundException;
import exceptions.NotEnoughEnergyException;
import idmanager.ID;
import location.XY;
import ui.commandhandle.MoveCommand;

public class Board {

    private static final Logger LOGGER = Logger.getLogger(Board.class.getName());

    private final int boardSizeX;
    private final int boardSizeY;
    private List<Entity> entitySet;
    private BoardConfig config;
    private int currentStep = 0;

    private ArrayList<MasterSquirrel> masters = new ArrayList<>();

    public Board(BoardConfig config) {
        this.config = config;
        this.entitySet = new CopyOnWriteArrayList<>();
        this.boardSizeX = config.getSize().x;
        this.boardSizeY = config.getSize().y;

        generateEntities();
    }

    public BoardConfig getConfig() {
        return this.config;
    }

    public int getBoardSizeX() {
        return this.boardSizeX;
    }

    public int getBoardSizeY() {
        return this.boardSizeY;
    }

    public List<Entity> getEntitySet() {
        return this.entitySet;
    }

    private void generateEntities() {
        for (int i = 0; i < this.boardSizeY; i++) {
            for (int j = 0; j < this.boardSizeX; j++) {
                if ((i == 0) || (j == 0) || (i == this.boardSizeY - 1) || (j == this.boardSizeX - 1)) {
                    this.entitySet.add(new Wall(ID.getNewID(), new XY(j, i)));
                }
            }
        }
        for (int badBeastsCount = 0; badBeastsCount < this.config.getNumberOfBadBeasts(); badBeastsCount++) {
            this.entitySet.add(new BadBeast(ID.getNewID(), getEmptyLocation()));
        }
        for (int goodBeastsCount = 0; goodBeastsCount < this.config.getNumberOfGoodBeasts(); goodBeastsCount++) {
            this.entitySet.add(new GoodBeast(ID.getNewID(), getEmptyLocation()));
        }
        for (int badPlantsCount = 0; badPlantsCount < this.config.getNumberOfBadPlants(); badPlantsCount++) {
            this.entitySet.add(new BadPlant(ID.getNewID(), getEmptyLocation()));
        }
        for (int goodPlantsCount = 0; goodPlantsCount < this.config.getNumberOfGoodPlants(); goodPlantsCount++) {
            this.entitySet.add(new GoodPlant(ID.getNewID(), getEmptyLocation()));
        }
        for (int wallCount = 0; wallCount < this.config.getNumberOfRandomWalls(); wallCount++) {
            this.entitySet.add(new Wall(ID.getNewID(), getEmptyLocation()));
        }
    }

    public void generateMasterSquirrels(MasterSquirrel[] masters) {
        for (MasterSquirrel ms : masters) {
            ms.setLocation(getEmptyLocation());
            this.entitySet.add(ms);
            this.masters.add(ms);
        }
    }

    public void spawnMiniSquirrel(MasterSquirrel ms, int energy) throws NotEnoughEnergyException, BelowThresholdException {
        if (ms == null) {
            throw new EntityNotFoundException("No MasterSquirrel in the EntitySet");
        } else if (ms.getEnergy() < energy) {
            throw new NotEnoughEnergyException();
        } else {
            XY location = this.getEmptyLocationAround(ms.getLocation());
            if (location == null) {
                LOGGER.log(Level.INFO, "couldn't find a location for the MiniSquirrel to spawn");
                return;
            } else {
                LOGGER.entering(Board.class.getName(), "spawnMiniSquirrel(MasterSquirrel, int)");
                MiniSquirrel newMS = ms.spawnMiniSquirrel(location, energy);
                this.entitySet.add(newMS);
            }
        }
    }

    private XY getEmptyLocation() {
        while (true) {
            int randomX = new Random().nextInt(this.boardSizeX - 1);
            int randomY = new Random().nextInt(this.boardSizeY - 1);
            XY check = new XY(randomX, randomY);
            if (this.getEntity(check) == null) {
                return check;
            }
        }
    }

    public FlattenedBoard flatten() {
        return new FlattenedBoard(this);
    }

    public void nextStep(MoveCommand command) {
        currentStep++;
        List<Entity> copy = this.entitySet;
        for (Entity e : copy) {
            if (e instanceof Character) {
                LOGGER.info(e.toString());
                if (e instanceof HandOperatedMasterSquirrel) {
                    e.nextStep(this.flatten(), command);
                    continue;
                }
                e.nextStep(this.flatten(), MoveCommand.getRandomCommand());
            }
        }
    }

    public int getRemainingSteps() {
        return this.config.getMaximumSteps() - this.currentStep;
    }

    @Override
    public String toString() {
        String string = "";
        for (Entity e : this.entitySet) {
            if (!(e instanceof Wall)) {
                string += e.toString() + "\n";
            }
        }
        return string;
    }

    public Entity getEntity(XY location) {
        for (Entity e : this.entitySet) {
            if (e != null) {
                if (e.getLocation().equals(location)) {
                    return e;
                }
            }
        }
        return null;
    }

    private XY getEmptyLocationAround(XY pos) {
        if (this.getEntity(new XY(pos.x - 1, pos.y)) == null) {
            return new XY(pos.x - 1, pos.y);
        }
        if (this.getEntity(new XY(pos.x - 1, pos.y)) == null) {
            return new XY(pos.x + 1, pos.y);
        }
        if (this.getEntity(new XY(pos.x - 1, pos.y)) == null) {
            return new XY(pos.x, pos.y - 1);
        }
        if (this.getEntity(new XY(pos.x - 1, pos.y)) == null) {
            return new XY(pos.x, pos.y + 1);
        }
        return null;
    }
}