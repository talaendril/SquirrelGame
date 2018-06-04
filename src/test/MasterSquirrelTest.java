package test;

import core.EntityContext;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import exceptions.BelowThresholdException;
import exceptions.NotEnoughEnergyException;
import exceptions.ShouldNotBeCalledException;
import location.XY;
import location.XYSupport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ui.commandhandle.MoveCommand;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MasterSquirrelTest {

    private MasterSquirrel master;

    @Before
    public void setup() {
        master = new MasterSquirrel(1, new XY(1, 1));
    }

    @Test(expected = ShouldNotBeCalledException.class)
    public void resetEnergyTest() {
        master.resetEnergy();
    }

    @Test
    public void toStringTest() {
        assertEquals("| MasterSquirrel ID: 1 | Energy: 1100 | Location: 1 1", master.toString());
    }

    @Test
    public void getProductionTest() throws BelowThresholdException, NotEnoughEnergyException {
        MasterSquirrel testMaster = new MasterSquirrel(1, new XY(1, 1));
        List<MiniSquirrel> list = new ArrayList<>();
        assertEquals(list, testMaster.getProduction());

        MiniSquirrel miniSquirrel = testMaster.spawnMiniSquirrel(new XY(2, 2), 100);
        list.add(miniSquirrel);
        assertEquals(list, testMaster.getProduction());
    }

    @Test
    public void removeFromProductionTest() throws BelowThresholdException, NotEnoughEnergyException {
        MasterSquirrel testMaster = new MasterSquirrel(1, new XY(1, 1));
        List<MiniSquirrel> list = new ArrayList<>();
        MiniSquirrel miniSquirrel = testMaster.spawnMiniSquirrel(new XY(2, 2), 100);
        list.add(miniSquirrel);
        assertEquals(list, testMaster.getProduction());

        testMaster.removeFromProduction(miniSquirrel);
        list.remove(miniSquirrel);
        assertEquals(list, testMaster.getProduction());
    }

    @Test
    public void checkEntityInProductionTest() throws BelowThresholdException, NotEnoughEnergyException {
        MasterSquirrel testMaster = new MasterSquirrel(1, new XY(1, 1));
        MiniSquirrel miniSquirrel = testMaster.spawnMiniSquirrel(new XY(2, 2), 100);

        assertTrue(testMaster.checkEntityInProduction(miniSquirrel));
    }

    @Test
    public void nextStepTest() {
        EntityContext context = mock(EntityContext.class);
        master.nextStep(context, MoveCommand.NONE);

        verify(context).tryMove(master, XYSupport.getVector(MoveCommand.NONE));
    }

    @Test
    public void updateEnergyTest() {
        MasterSquirrel testMaster = new MasterSquirrel(1, new XY(1, 1));
        assertEquals(1100, testMaster.getEnergy());

        testMaster.updateEnergy(200);
        assertEquals(1300, testMaster.getEnergy());

        testMaster.updateEnergy(-1400);
        assertEquals(1, testMaster.getEnergy());
    }

    @Test
    public void moveTest() {
        MasterSquirrel testMaster = new MasterSquirrel(1, new XY(1, 1));
        testMaster.move(XY.RIGHT_DOWN);

        assertEquals(new XY(2, 2), testMaster.getLocation());
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void spawnMiniSquirrelBelowThresholdTest() throws BelowThresholdException, NotEnoughEnergyException {
        MasterSquirrel testMaster = new MasterSquirrel(1, new XY(1, 1));
        testMaster.updateEnergy(-200);
        exception.expect(BelowThresholdException.class);
        testMaster.spawnMiniSquirrel(new XY(2, 2), 100);
    }

    @Test
    public void spawnMiniSquirrelNotEnoughEnergyTest() throws BelowThresholdException, NotEnoughEnergyException {
        MasterSquirrel testMaster = new MasterSquirrel(1, new XY(1, 1));
        exception.expect(NotEnoughEnergyException.class);
        testMaster.spawnMiniSquirrel(new XY(2, 2), 1200);
    }

    @Test
    public void spawnMiniSquirrelTest() throws BelowThresholdException, NotEnoughEnergyException {
        MasterSquirrel testMaster = new MasterSquirrel(1, new XY(1, 1));
        MiniSquirrel miniSquirrel = testMaster.spawnMiniSquirrel(new XY(2, 2), 100);
        assertNotNull(miniSquirrel);

        assertEquals(1000, testMaster.getEnergy());

        assertTrue(testMaster.checkEntityInProduction(miniSquirrel));
    }
}
