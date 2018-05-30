package test;

import core.*;
import entities.*;
import exceptions.BelowThresholdException;
import exceptions.NotEnoughEnergyException;
import idmanager.ID;
import location.XY;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlattenedBoardTest {

    private DummyBoardConfig config = new DummyBoardConfig();
    private Board board = new Board(config);
    private EntitySet entitySet;
    private FlattenedBoard flattenedBoard;

    //TODO: try to implement Mocks

    @Test
    public void tryMoveMasterAgainstBadBeastTest() {
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 1));
        int originalMasterEnergy = master.getEnergy();
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();

        entitySet.addEntity(master);
        BadBeast badBeast = new BadBeast(ID.getNewID(), new XY(2, 2));
        entitySet.addEntity(badBeast);

        flattenedBoard.tryMove(master, XY.RIGHT_DOWN);

        //after colliding with a BadBeast the MasterSquirrel shouldn't move
        assertEquals(new XY(1, 1), master.getLocation());

        //after colliding with a BadBeast the MasterSquirrel loses 150 energy
        assertEquals(originalMasterEnergy + badBeast.getEnergy(), master.getEnergy());

        //BiteCounter starts at 0 and goes up by one every time he collides with a Squirrel
        assertEquals(1, badBeast.getBiteCounter());

        while (badBeast.getBiteCounter() != BadBeast.MAXIMUM_BITECOUNT) {
            badBeast.incrementBiteCounter();
        }
        flattenedBoard.tryMove(master, XY.RIGHT_DOWN);

        //since BiteCounter is at maximum the BadBeast should die and the MasterSquirrel should take its place
        assertEquals(new XY(2, 2), master.getLocation());
    }

    @Test
    public void tryMoveMasterAgainstGoodBeastTest() {
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 1));
        int originalMasterEnergy = master.getEnergy();
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();

        entitySet.addEntity(master);
        GoodBeast goodBeast = new GoodBeast(ID.getNewID(), new XY(2, 2));
        entitySet.addEntity(goodBeast);

        flattenedBoard.tryMove(master, XY.RIGHT_DOWN);
        assertEquals(new XY(2, 2), master.getLocation());

        assertEquals(originalMasterEnergy + goodBeast.getEnergy(), master.getEnergy());
    }

    @Test
    public void tryMoveMasterAgainstSquirrelsTest() throws BelowThresholdException, NotEnoughEnergyException {
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 1));
        int originalMasterEnergy = master.getEnergy();
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();

        entitySet.addEntity(master);

        MasterSquirrel masterTwo = new MasterSquirrel(ID.getNewID(), new XY(2, 2));
        entitySet.addEntity(masterTwo);

        MiniSquirrel miniSquirrelOne = master.spawnMiniSquirrel(new XY(2, 1), 100);
        entitySet.addEntity(miniSquirrelOne);

        MiniSquirrel miniSquirrelTwo = masterTwo.spawnMiniSquirrel(new XY(1, 2), 100);
        entitySet.addEntity(miniSquirrelTwo);

        //Master 1 eats his own MiniSquirrel
        flattenedBoard.tryMove(master, XY.RIGHT);
        assertEquals(new XY(2, 1), master.getLocation());

        assertEquals(originalMasterEnergy, master.getEnergy());

        assertFalse(entitySet.containsEntity(miniSquirrelOne));
        //Master 1 hits Master 2
        flattenedBoard.tryMove(master, XY.DOWN);
        assertEquals(new XY(2, 1), master.getLocation());

        assertEquals(originalMasterEnergy, master.getEnergy());
        //Master 1 hits MiniSquirrel of Master 2
        master.move(XY.LEFT);
        flattenedBoard.tryMove(master, XY.DOWN);
        assertEquals(new XY(1, 2), master.getLocation());

        assertEquals(originalMasterEnergy + MiniSquirrel.ENERGY_GAIN_NOT_MASTER, master.getEnergy());
    }

    @Test
    public void tryMoveMasterAgainstPlantsTest() {
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 1));
        int originalMasterEnergy = master.getEnergy();
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();

        entitySet.addEntity(master);
        BadPlant badPlant = new BadPlant(ID.getNewID(), new XY(1, 2));
        entitySet.addEntity(badPlant);
        GoodPlant goodPlant = new GoodPlant(ID.getNewID(), new XY(2, 1));
        entitySet.addEntity(goodPlant);

        flattenedBoard.tryMove(master, XY.DOWN);
        assertEquals(new XY(1, 2), master.getLocation());

        assertEquals(originalMasterEnergy + badPlant.getEnergy(), master.getEnergy());
        master.updateEnergy(-badPlant.getEnergy()); //resetting energy

        flattenedBoard.tryMove(master, XY.RIGHT_UP);
        assertEquals(new XY(2, 1), master.getLocation());

        assertEquals(originalMasterEnergy + goodPlant.getEnergy(), master.getEnergy());
    }

    @Test
    public void tryMoveMasterAgainstWallTest() {
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 1));
        int originalMasterEnergy = master.getEnergy();
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        master.setLocation(new XY(1, 1));

        entitySet.addEntity(master);
        Wall wall = new Wall(ID.getNewID(), new XY(0, 0));
        entitySet.addEntity(wall);

        flattenedBoard.tryMove(master, XY.LEFT_UP);
        assertEquals(originalMasterEnergy + wall.getEnergy(), master.getEnergy());

        assertEquals(new XY(1, 1), master.getLocation());

        assertTrue(master.getStunnedAndDecrement());
    }

    @Test
    public void tryMoveMiniSquirrelAgainstOwnMasterTest() throws BelowThresholdException, NotEnoughEnergyException {
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 1));
        int originalMasterEnergy = master.getEnergy();
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        entitySet.addEntity(master);

        MiniSquirrel miniSquirrel = master.spawnMiniSquirrel(new XY(2, 2), 100);
        entitySet.addEntity(miniSquirrel);
        flattenedBoard.tryMove(miniSquirrel, XY.LEFT_UP);

        assertEquals(originalMasterEnergy, master.getEnergy());

        assertFalse(entitySet.containsEntity(miniSquirrel));
    }

    @Test
    public void tryMoveMiniSquirrelAgainstEnemyMasterTest() throws BelowThresholdException, NotEnoughEnergyException {
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 1));
        entitySet.addEntity(master);
        MasterSquirrel enemy = new MasterSquirrel(ID.getNewID(), new XY(2, 2));
        int originalEnemyEnergy = enemy.getEnergy();
        entitySet.addEntity(enemy);

        MiniSquirrel miniSquirrel = master.spawnMiniSquirrel(new XY(1, 2), 100);
        entitySet.addEntity(miniSquirrel);
        flattenedBoard.tryMove(miniSquirrel, XY.RIGHT);
        assertEquals(originalEnemyEnergy + MiniSquirrel.ENERGY_GAIN_NOT_MASTER, enemy.getEnergy());

        assertFalse(entitySet.containsEntity(miniSquirrel));

    }

    @Test
    public void tryMoveMiniSquirrelAgainstMiniSquirrelTest() {
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 2));    //just needs to exist
        MiniSquirrel miniSquirrel = new MiniSquirrel(ID.getNewID(), 100, new XY(1, 1), master);
        entitySet.addEntity(miniSquirrel);
        MiniSquirrel enemy = new MiniSquirrel(ID.getNewID(), 100, new XY(2, 2), master);
        entitySet.addEntity(enemy);

        flattenedBoard.tryMove(miniSquirrel, XY.RIGHT_DOWN);
        assertFalse(entitySet.containsEntity(miniSquirrel));

        assertFalse(entitySet.containsEntity(enemy));
    }

    @Test
    public void tryMoveMiniSquirrelAgainstBadBeastTest() {
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 2));    //just needs to exist
        MiniSquirrel miniSquirrel = new MiniSquirrel(ID.getNewID(), 100, new XY(1, 1), master);
        entitySet.addEntity(miniSquirrel);
        BadBeast badBeast = new BadBeast(ID.getNewID(), new XY(2, 2));
        entitySet.addEntity(badBeast);

        flattenedBoard.tryMove(miniSquirrel, XY.RIGHT_DOWN);
        assertFalse(entitySet.containsEntity(miniSquirrel));

        assertEquals(1, badBeast.getBiteCounter());

        miniSquirrel = new MiniSquirrel(ID.getNewID(), 200, new XY(3, 3), master);
        entitySet.addEntity(miniSquirrel);

        flattenedBoard.tryMove(miniSquirrel, XY.LEFT_UP);
        assertTrue(entitySet.containsEntity(miniSquirrel));

        assertEquals(200 + badBeast.getEnergy(), miniSquirrel.getEnergy());
    }

    @Test
    public void tryMoveMiniSquirrelAgainstGoodBeastTest() {
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 2));    //just needs to exist
        MiniSquirrel miniSquirrel = new MiniSquirrel(ID.getNewID(), 100, new XY(1, 1), master);
        entitySet.addEntity(miniSquirrel);
        GoodBeast goodBeast = new GoodBeast(ID.getNewID(), new XY(2, 2));
        entitySet.addEntity(goodBeast);

        flattenedBoard.tryMove(miniSquirrel, XY.RIGHT_DOWN);
        assertEquals(new XY(2, 2), miniSquirrel.getLocation());

        assertEquals(100 + goodBeast.getEnergy(), miniSquirrel.getEnergy());
    }

    @Test
    public void tryMoveMiniSquirrelAgainstPlants() {
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 2));
        MiniSquirrel miniSquirrel = new MiniSquirrel(ID.getNewID(), 100, new XY(2, 1), master);
        entitySet.addEntity(miniSquirrel);
        BadPlant badPlant = new BadPlant(ID.getNewID(), new XY(1, 1));
        entitySet.addEntity(badPlant);
        GoodPlant goodPlant = new GoodPlant(ID.getNewID(), new XY(2, 2));
        entitySet.addEntity(goodPlant);

        flattenedBoard.tryMove(miniSquirrel, XY.DOWN);
        assertEquals(100 + goodPlant.getEnergy(), miniSquirrel.getEnergy());    //energy should 200

        flattenedBoard.tryMove(miniSquirrel, XY.LEFT_UP);
        assertEquals(200 + badPlant.getEnergy(), miniSquirrel.getEnergy());
    }

    @Test
    public void tryMoveMiniSquirrelAgainstWall() {
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(1, 2));
        MiniSquirrel miniSquirrel = new MiniSquirrel(ID.getNewID(), 100, new XY(1, 1), master);
        entitySet.addEntity(miniSquirrel);
        Wall wall = new Wall(ID.getNewID(), new XY(0, 0));
        entitySet.addEntity(wall);

        flattenedBoard.tryMove(miniSquirrel, XY.LEFT_UP);
        assertEquals(new XY(1, 1), miniSquirrel.getLocation());

        assertEquals(100 + wall.getEnergy(), miniSquirrel.getEnergy());

        assertTrue(miniSquirrel.getStunnedAndDecrement());
    }

    @Test
    public void tryMoveGoodBeastAgainstSquirrelTest() {
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        Squirrel squirrel = new MasterSquirrel(ID.getNewID(), new XY(1, 1));
        int originalEnergy = squirrel.getEnergy();
        entitySet.addEntity(squirrel);
        GoodBeast goodBeast = new GoodBeast(ID.getNewID(), new XY(2, 2));
        entitySet.addEntity(goodBeast);

        while (goodBeast.getStepCount() != GoodBeast.MAXIMUM_STEPCOUNT) {
            goodBeast.incrementStepCount();
        }

        flattenedBoard.tryMove(goodBeast, XY.LEFT_UP);  //this only works with disabling the runaway function in flattenedBoard
        assertEquals(originalEnergy + goodBeast.getEnergy(), squirrel.getEnergy());

        assertNull(flattenedBoard.getEntity(new XY(2, 2)));

        assertEquals(1, goodBeast.getStepCount());
    }

    @Test
    public void tryMoveGoodBeastAgainstNonSquirrelEntitiesTest() {
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        BadBeast badBeast = new BadBeast(ID.getNewID(), new XY(1, 2));
        entitySet.addEntity(badBeast);
        Wall wall = new Wall(ID.getNewID(), new XY(0, 0));
        entitySet.addEntity(wall);
        GoodPlant goodPlant = new GoodPlant(ID.getNewID(), new XY(2, 2));
        entitySet.addEntity(goodPlant);
        BadPlant badPlant = new BadPlant(ID.getNewID(), new XY(2, 1));
        entitySet.addEntity(badPlant);
        GoodBeast goodBeast = new GoodBeast(ID.getNewID(), new XY(1, 1));
        entitySet.addEntity(goodBeast);

        while (goodBeast.getStepCount() != GoodBeast.MAXIMUM_STEPCOUNT) {
            goodBeast.incrementStepCount();
        }

        flattenedBoard.tryMove(goodBeast, XY.LEFT_UP);
        assertEquals(goodBeast, flattenedBoard.getEntity(new XY(1, 1)));

        while (goodBeast.getStepCount() != GoodBeast.MAXIMUM_STEPCOUNT) {
            goodBeast.incrementStepCount();
        }

        flattenedBoard.tryMove(goodBeast, XY.RIGHT_DOWN);
        assertEquals(goodBeast, flattenedBoard.getEntity(new XY(1, 1)));

        while (goodBeast.getStepCount() != GoodBeast.MAXIMUM_STEPCOUNT) {
            goodBeast.incrementStepCount();
        }

        flattenedBoard.tryMove(goodBeast, XY.RIGHT);
        assertEquals(goodBeast, flattenedBoard.getEntity(new XY(1, 1)));

        while (goodBeast.getStepCount() != GoodBeast.MAXIMUM_STEPCOUNT) {
            goodBeast.incrementStepCount();
        }

        flattenedBoard.tryMove(goodBeast, XY.DOWN);
        assertEquals(goodBeast, flattenedBoard.getEntity(new XY(1, 1)));
    }

    @Test
    public void tryMoveBadBeastAgainstSquirrelsTest() {
        /*
        had to remove follow function in flattenedBoard for these tests
         */
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        MasterSquirrel master = new MasterSquirrel(ID.getNewID(), new XY(2, 1));
        int originalEnergy = master.getEnergy();
        entitySet.addEntity(master);
        MiniSquirrel miniSquirrel = new MiniSquirrel(ID.getNewID(), 100, new XY(1, 2), master);
        entitySet.addEntity(miniSquirrel);
        BadBeast badBeast = new BadBeast(ID.getNewID(), new XY(1, 1));
        entitySet.addEntity(badBeast);

        while (badBeast.getStepCount() != GoodBeast.MAXIMUM_STEPCOUNT) {
            badBeast.incrementStepCount();
        }
        flattenedBoard.tryMove(badBeast, XY.RIGHT);
        assertEquals(1, badBeast.getBiteCounter());

        assertEquals(badBeast, flattenedBoard.getEntity(new XY(1, 1)));

        assertEquals(originalEnergy + badBeast.getEnergy(), master.getEnergy());

        assertEquals(1, badBeast.getStepCount());

        while (badBeast.getStepCount() != GoodBeast.MAXIMUM_STEPCOUNT) {
            badBeast.incrementStepCount();
        }

        flattenedBoard.tryMove(badBeast, XY.DOWN);
        assertEquals(2, badBeast.getBiteCounter());

        assertEquals(badBeast, flattenedBoard.getEntity(miniSquirrel.getLocation()));

        assertFalse(entitySet.containsEntity(miniSquirrel));
    }

    @Test
    public void tryMoveBadBeastAgainstNonSquirrelEntitiesTest() {
        entitySet = board.getEntitySet();
        flattenedBoard = board.flatten();
        GoodBeast goodBeast = new GoodBeast(ID.getNewID(), new XY(1, 2));
        entitySet.addEntity(goodBeast);
        Wall wall = new Wall(ID.getNewID(), new XY(0, 0));
        entitySet.addEntity(wall);
        GoodPlant goodPlant = new GoodPlant(ID.getNewID(), new XY(2, 2));
        entitySet.addEntity(goodPlant);
        BadPlant badPlant = new BadPlant(ID.getNewID(), new XY(2, 1));
        entitySet.addEntity(badPlant);
        BadBeast badBeast = new BadBeast(ID.getNewID(), new XY(1, 1));
        entitySet.addEntity(badBeast);

        while (badBeast.getStepCount() != GoodBeast.MAXIMUM_STEPCOUNT) {
            badBeast.incrementStepCount();
        }

        flattenedBoard.tryMove(badBeast, XY.LEFT_UP);
        assertEquals(badBeast, flattenedBoard.getEntity(new XY(1, 1)));

        while (badBeast.getStepCount() != GoodBeast.MAXIMUM_STEPCOUNT) {
            badBeast.incrementStepCount();
        }

        flattenedBoard.tryMove(badBeast, XY.RIGHT_DOWN);
        assertEquals(badBeast, flattenedBoard.getEntity(new XY(1, 1)));

        while (badBeast.getStepCount() != GoodBeast.MAXIMUM_STEPCOUNT) {
            badBeast.incrementStepCount();
        }

        flattenedBoard.tryMove(badBeast, XY.RIGHT);
        assertEquals(badBeast, flattenedBoard.getEntity(new XY(1, 1)));

        while (badBeast.getStepCount() != GoodBeast.MAXIMUM_STEPCOUNT) {
            badBeast.incrementStepCount();
        }

        flattenedBoard.tryMove(badBeast, XY.DOWN);
        assertEquals(badBeast, flattenedBoard.getEntity(new XY(1, 1)));
    }
}
