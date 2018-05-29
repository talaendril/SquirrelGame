package test;

import core.*;
import entities.*;
import idmanager.ID;
import location.XY;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FlattenedBoardTest {

    private DummyBoardConfig config = new DummyBoardConfig();
    private Board board = new Board(config);
    private EntitySet entitySet = board.getEntitySet();
    private FlattenedBoard flattenedBoard = board.flatten();

    private MasterSquirrel master;
    private MiniSquirrel miniSquirrelOne;
    private MasterSquirrel masterTwo;
    private MiniSquirrel miniSquirrelTwo;

    private BadBeast badBeast;
    private GoodBeast goodBeast;
    private BadPlant badPlant;
    private GoodPlant goodPlant;

    //TODO: try to implement Mocks and how to see if kill or killAndReplace was called

    @Test
    public void tryMoveMasterAgainstBadBeastTest() {
        master = new MasterSquirrel(ID.getNewID(), new XY(1, 1));
        int originalEnergy = master.getEnergy();
        entitySet.addEntity(master);
        badBeast = new BadBeast(ID.getNewID(), new XY(2, 2));
        entitySet.addEntity(badBeast);

        flattenedBoard.tryMove(master, XY.RIGHT_DOWN);

        //after colliding with a BadBeast the MasterSquirrel shouldn't move
        assertEquals(new XY(1, 1), master.getLocation());

        //after colliding with a BadBeast the MasterSquirrel loses 150 energy
        assertEquals(originalEnergy + badBeast.getEnergy(), master.getEnergy());

        //BiteCounter starts at 0 and goes up by one every time he collides with a Squirrel
        assertEquals(1, badBeast.getBiteCounter());

        while(badBeast.getBiteCounter() != BadBeast.MAXIMUM_BITECOUNT) {
            badBeast.incrementBiteCounter();
        }
        flattenedBoard.tryMove(master, XY.RIGHT_DOWN);

        //since BiteCounter is at maximum the BadBeast should die and the MasterSquirrel should take its place
        assertEquals(new XY(2, 2), master.getLocation());

        entitySet.removeEntity(badBeast);
        entitySet.removeEntity(master);
    }

    @Test
    public void tryMoveMasterAgainstGoodBeastTest() {
        master = new MasterSquirrel(ID.getNewID(), new XY(1, 1));
        int originalEnergy = master.getEnergy();
        entitySet.addEntity(master);
        goodBeast = new GoodBeast(ID.getNewID(), new XY(2, 2));
        entitySet.addEntity(goodBeast);

        flattenedBoard.tryMove(master, XY.RIGHT_DOWN);
        assertEquals(new XY(2, 2), master.getLocation());

        assertEquals(originalEnergy + goodBeast.getEnergy(), master.getEnergy());

        entitySet.removeEntity(goodBeast);
        entitySet.removeEntity(master);
    }
}
