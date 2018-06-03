package test;

import core.*;
import entities.*;
import location.XY;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class FlattenedBoardTest {
    @Mock
    private Board boardMock;

    @InjectMocks
    private FlattenedBoard flattenedBoard;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        when(boardMock.getBoardSizeX()).thenReturn(10);
        when(boardMock.getBoardSizeY()).thenReturn(10);
    }

    @Test
    public void tryMoveMasterTest() {
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        when(masterMock.getLocation()).thenReturn(new XY(1, 1));
        flattenedBoard.tryMove(masterMock, XY.DOWN);

        verify(masterMock).move(XY.DOWN);
    }

    @Test
    public void tryMoveMasterAgainstBadBeastTest() {
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        BadBeast badMock = mock(BadBeast.class);

        when(boardMock.getEntity(new XY(2, 2))).thenReturn(badMock);
        when(badMock.getEnergy()).thenReturn(-150);
        when(masterMock.getLocation()).thenReturn(new XY(1, 1));
        when(badMock.getLocation()).thenReturn(new XY(2, 2));
        when(badMock.getBiteCounter()).thenReturn(0);

        flattenedBoard.tryMove(masterMock, XY.RIGHT_DOWN);

        verify(masterMock).updateEnergy(-150);
        verify(badMock).incrementBiteCounter();
        verify(masterMock, never()).move(XY.RIGHT_DOWN);
        verify(boardMock, never()).removeEntity(badMock);
    }

    @Test
    public void tryMoveMasterAgainstBadBeastMaxBiteTest() {
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        BadBeast badMock = mock(BadBeast.class);

        when(boardMock.getEntity(new XY(2, 2))).thenReturn(badMock);
        when(badMock.getEnergy()).thenReturn(-150);
        when(masterMock.getLocation()).thenReturn(new XY(1, 1));
        when(badMock.getLocation()).thenReturn(new XY(2, 2));
        when(badMock.getBiteCounter()).thenReturn(6);

        flattenedBoard.tryMove(masterMock, XY.RIGHT_DOWN);

        verify(masterMock).updateEnergy(-150);
        verify(masterMock).move(XY.RIGHT_DOWN);
        verify(boardMock).removeEntity(badMock);
        verify(boardMock).addEntity(badMock);
    }

    @Test
    public void tryMoveMasterAgainstGoodBeastTest() {
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        GoodBeast goodMock = mock(GoodBeast.class);

        when(boardMock.getEntity(new XY(2, 2))).thenReturn(goodMock);
        when(goodMock.getEnergy()).thenReturn(200);
        when(masterMock.getLocation()).thenReturn(new XY(1, 1));
        when(goodMock.getLocation()).thenReturn(new XY(2, 2));

        flattenedBoard.tryMove(masterMock, XY.RIGHT_DOWN);

        verify(masterMock).updateEnergy(200);
        verify(masterMock).move(XY.RIGHT_DOWN);
        verify(boardMock).removeEntity(goodMock);
        verify(boardMock).addEntity(goodMock);
    }

    @Test
    public void tryMoveMasterAgainstMasterTest() {
        MasterSquirrel masterOneMock = mock(MasterSquirrel.class);
        MasterSquirrel masterTwoMock = mock(MasterSquirrel.class);

        when(boardMock.getEntity(new XY(2, 2))).thenReturn(masterTwoMock);
        when(masterOneMock.getLocation()).thenReturn(new XY(1, 1));

        flattenedBoard.tryMove(masterOneMock, XY.RIGHT_DOWN);

        verify(masterOneMock, never()).move(XY.RIGHT_DOWN);
        verify(masterOneMock, never()).updateEnergy(150);
        verify(masterTwoMock, never()).updateEnergy(150);
        verify(boardMock, never()).removeEntity(masterOneMock);
        verify(boardMock, never()).removeEntity(masterTwoMock);
    }

    @Test
    public void tryMoveMasterAgainstOwnMiniTest() {
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        MiniSquirrel miniMock = mock(MiniSquirrel.class);

        when(masterMock.checkEntityInProduction(miniMock)).thenReturn(true);
        when(miniMock.getMaster()).thenReturn(masterMock);
        when(miniMock.getEnergy()).thenReturn(200);
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(miniMock);
        when(masterMock.getLocation()).thenReturn(new XY(1, 1));

        flattenedBoard.tryMove(masterMock, XY.RIGHT_DOWN);

        verify(masterMock).updateEnergy(miniMock.getEnergy());
        verify(masterMock).move(XY.RIGHT_DOWN);
        verify(boardMock).removeEntity(miniMock);
        verify(masterMock).removeFromProduction(miniMock);
    }

    @Test
    public void tryMoveMasterAgainstEnemyMiniTest() {
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        MasterSquirrel masterTwoMock = mock(MasterSquirrel.class);
        MiniSquirrel miniMock = mock(MiniSquirrel.class);

        when(masterMock.checkEntityInProduction(miniMock)).thenReturn(false);
        when(miniMock.getMaster()).thenReturn(masterTwoMock);
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(miniMock);
        when(masterMock.getLocation()).thenReturn(new XY(1, 1));

        flattenedBoard.tryMove(masterMock, XY.RIGHT_DOWN);

        verify(masterMock).updateEnergy(MiniSquirrel.ENERGY_GAIN_NOT_MASTER);
        verify(masterMock).move(XY.RIGHT_DOWN);
        verify(boardMock).removeEntity(miniMock);
        verify(masterTwoMock).removeFromProduction(miniMock);
    }

    @Test
    public void tryMoveMasterAgainstBadPlantsTest() {
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        BadPlant badPlantMock = mock(BadPlant.class);

        when(masterMock.getLocation()).thenReturn(new XY(1, 1));
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(badPlantMock);
        when(badPlantMock.getEnergy()).thenReturn(-100);

        flattenedBoard.tryMove(masterMock, XY.RIGHT_DOWN);

        verify(masterMock).updateEnergy(-100);
        verify(masterMock).move(XY.RIGHT_DOWN);
        verify(boardMock).removeEntity(badPlantMock);
        verify(boardMock).addEntity(badPlantMock);
    }

    @Test
    public void tryMoveMasterAgainstGoodPlants() {
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        GoodPlant goodPlantMock = mock(GoodPlant.class);

        when(masterMock.getLocation()).thenReturn(new XY(1, 1));
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(goodPlantMock);
        when(goodPlantMock.getEnergy()).thenReturn(100);

        flattenedBoard.tryMove(masterMock, XY.RIGHT_DOWN);

        verify(masterMock).updateEnergy(100);
        verify(masterMock).move(XY.RIGHT_DOWN);
        verify(boardMock).removeEntity(goodPlantMock);
        verify(boardMock).addEntity(goodPlantMock);
    }

    @Test
    public void tryMoveMasterAgainstWallTest() {
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        Wall wallMock = mock(Wall.class);

        when(masterMock.getLocation()).thenReturn(new XY(1, 1));
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(wallMock);
        when(wallMock.getEnergy()).thenReturn(-10);

        flattenedBoard.tryMove(masterMock, XY.RIGHT_DOWN);

        verify(masterMock).updateEnergy(-10);
        verify(masterMock).setStunned();
        verify(masterMock, never()).move(XY.RIGHT_DOWN);
        verify(boardMock, never()).removeEntity(wallMock);
    }

    @Test
    public void tryMoveMiniAgainstOwnMasterTest() {
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        MiniSquirrel miniMock = mock(MiniSquirrel.class);

        when(masterMock.checkEntityInProduction(miniMock)).thenReturn(true);
        when(miniMock.getMaster()).thenReturn(masterMock);
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(masterMock);
        when(miniMock.getLocation()).thenReturn(new XY(1 , 1));
        when(miniMock.getEnergy()).thenReturn(100);
        when(masterMock.equals(miniMock.getMaster())).thenReturn(true);

        flattenedBoard.tryMove(miniMock, XY.RIGHT_DOWN);

        verify(masterMock).updateEnergy(100);
        verify(masterMock).removeFromProduction(miniMock);
        verify(boardMock).removeEntity(miniMock);
        verify(boardMock, never()).addEntity(miniMock);
        verify(miniMock, never()).move(XY.RIGHT_DOWN);
    }

    @Test
    public void tryMoveMiniAgainstEnemyMasterTest() {
        MiniSquirrel miniMock = mock(MiniSquirrel.class);
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        MasterSquirrel enemyMock = mock(MasterSquirrel.class);

        when(miniMock.getLocation()).thenReturn(new XY(1, 1));
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(enemyMock);
        when(miniMock.getMaster()).thenReturn(masterMock);

        flattenedBoard.tryMove(miniMock, XY.RIGHT_DOWN);

        verify(enemyMock).updateEnergy(MiniSquirrel.ENERGY_GAIN_NOT_MASTER);
        verify(boardMock).removeEntity(miniMock);
        verify(boardMock, never()).addEntity(miniMock);
        verify(miniMock, never()).move(XY.RIGHT_DOWN);
    }

    @Test
    public void tryMoveMiniAgainstMiniTest() {
        MiniSquirrel miniMock = mock(MiniSquirrel.class);
        MiniSquirrel enemyMiniMock = mock(MiniSquirrel.class);
        MasterSquirrel masterMock = mock(MasterSquirrel.class);
        MasterSquirrel enemyMock = mock(MasterSquirrel.class);

        when(miniMock.getMaster()).thenReturn(masterMock);
        when(enemyMiniMock.getMaster()).thenReturn(enemyMock);
        when(miniMock.getLocation()).thenReturn(new XY(1, 1));
        when(boardMock.getEntity(new XY(2,2))).thenReturn(enemyMiniMock);

        flattenedBoard.tryMove(miniMock, XY.RIGHT_DOWN);

        verify(boardMock).removeEntity(miniMock);
        verify(boardMock).removeEntity(enemyMiniMock);
        verify(boardMock, never()).addEntity(miniMock);
        verify(boardMock, never()).addEntity(enemyMiniMock);
        verify(miniMock, never()).move(XY.RIGHT_DOWN);
    }

    @Test
    public void tryMoveMiniAgainstBadBeastTest() {
        MiniSquirrel miniMock = mock(MiniSquirrel.class);
        BadBeast badMock = mock(BadBeast.class);

        when(miniMock.getEnergy()).thenReturn(200);
        when(miniMock.getLocation()).thenReturn(new XY(1, 1));
        when(badMock.getEnergy()).thenReturn(-150);
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(badMock);

        flattenedBoard.tryMove(miniMock, XY.RIGHT_DOWN);

        verify(miniMock).updateEnergy(-150);
        verify(miniMock, never()).move(XY.RIGHT_DOWN);
        verify(boardMock, never()).removeEntity(miniMock);
        verify(badMock).incrementBiteCounter();
    }

    @Test
    public void tryMoveMiniAgainstGoodBeastTest() {
        MiniSquirrel miniMock = mock(MiniSquirrel.class);
        GoodBeast goodMock = mock(GoodBeast.class);

        when(miniMock.getLocation()).thenReturn(new XY(1, 1));
        when(goodMock.getEnergy()).thenReturn(200);
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(goodMock);

        flattenedBoard.tryMove(miniMock, XY.RIGHT_DOWN);

        verify(miniMock).updateEnergy(200);
        verify(miniMock).move(XY.RIGHT_DOWN);
        verify(boardMock).removeEntity(goodMock);
        verify(boardMock).addEntity(goodMock);
    }

    @Test
    public void tryMoveMiniAgainstBadPlantsTest() {
        MiniSquirrel miniMock = mock(MiniSquirrel.class);
        BadPlant badMock = mock(BadPlant.class);

        when(miniMock.getLocation()).thenReturn(new XY(1, 1));
        when(badMock.getEnergy()).thenReturn(-100);
        when(miniMock.getEnergy()).thenReturn(150);
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(badMock);

        flattenedBoard.tryMove(miniMock, XY.RIGHT_DOWN);

        verify(boardMock, never()).removeEntity(miniMock);
        verify(boardMock, never()).addEntity(miniMock);
        verify(boardMock).removeEntity(badMock);
        verify(boardMock).addEntity(badMock);
        verify(miniMock).move(XY.RIGHT_DOWN);
    }

    @Test
    public void tryMoveMiniAgainstGoodPlantsTest() {
        MiniSquirrel miniMock = mock(MiniSquirrel.class);
        GoodPlant goodMock = mock(GoodPlant.class);

        when(miniMock.getLocation()).thenReturn(new XY(1, 1));
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(goodMock);
        when(goodMock.getEnergy()).thenReturn(200);

        flattenedBoard.tryMove(miniMock, XY.RIGHT_DOWN);

        verify(miniMock).updateEnergy(200);
        verify(miniMock).move(XY.RIGHT_DOWN);
        verify(boardMock).removeEntity(goodMock);
        verify(boardMock).addEntity(goodMock);
    }

    @Test
    public void tryMoveMiniAgainstWallTest() {
        MiniSquirrel miniMock = mock(MiniSquirrel.class);
        Wall wallMock = mock(Wall.class);

        when(miniMock.getLocation()).thenReturn(new XY(1, 1));
        when(miniMock.getEnergy()).thenReturn(100);
        when(wallMock.getEnergy()).thenReturn(-10);
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(wallMock);

        flattenedBoard.tryMove(miniMock, XY.RIGHT_DOWN);

        verify(miniMock).updateEnergy(-10);
        verify(miniMock, never()).move(XY.RIGHT_DOWN);
        verify(miniMock).setStunned();
        verify(boardMock, never()).removeEntity(wallMock);
    }

    @Test
    public void tryMoveGoodBeastAgainstSquirrelTest() {
        Squirrel squirrelMock = mock(Squirrel.class);
        GoodBeast goodMock = mock(GoodBeast.class);

        when(goodMock.getEnergy()).thenReturn(200);
        when(goodMock.getLocation()).thenReturn(new XY(1, 1));
        when(goodMock.getStepCount()).thenReturn(4);
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(squirrelMock);

        flattenedBoard.tryMove(goodMock, XY.RIGHT_DOWN);

        verify(squirrelMock).updateEnergy(200);
        verify(boardMock).removeEntity(goodMock);
        verify(boardMock).addEntity(goodMock);
    }

    @Test
    public void tryMoveGoodBeastAgainstNonSquirrelTest() {
        GoodBeast goodMock = mock(GoodBeast.class);
        BadPlant badPlantMock = mock(BadPlant.class);

        when(goodMock.getStepCount()).thenReturn(4);
        when(goodMock.getLocation()).thenReturn(new XY(1, 1));
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(badPlantMock);

        flattenedBoard.tryMove(goodMock, XY.RIGHT_DOWN);

        verify(goodMock, never()).move(XY.RIGHT_DOWN);
        verify(boardMock, never()).removeEntity(goodMock);
        verify(boardMock, never()).addEntity(goodMock);
        verify(boardMock, never()).removeEntity(badPlantMock);
        verify(boardMock, never()).addEntity(badPlantMock);
    }

    @Test
    public void tryMoveBadBeastAgainstSquirrelsTest() {
        MasterSquirrel squirrelMock = mock(MasterSquirrel.class);
        BadBeast badMock = mock(BadBeast.class);

        when(badMock.getLocation()).thenReturn(new XY(1, 1));
        when(badMock.getStepCount()).thenReturn(4);
        when(badMock.getEnergy()).thenReturn(-150);
        when(boardMock.getEntity(new XY(2, 2))).thenReturn(squirrelMock);
        when(squirrelMock.getEnergy()).thenReturn(1000);

        flattenedBoard.tryMove(badMock, XY.RIGHT_DOWN);

        verify(squirrelMock).updateEnergy(-150);
        verify(badMock).incrementBiteCounter();
        verify(badMock, never()).move(XY.RIGHT_DOWN);
    }

    @Test
    public void tryMoveBadBeastAgainstNonSquirrelTest() {
        BadBeast badMock = mock(BadBeast.class);
        GoodBeast goodMock = mock(GoodBeast.class);

        when(badMock.getStepCount()).thenReturn(4);
        when(badMock.getLocation()).thenReturn(new XY(1, 1));
        when(boardMock.getEntity(new XY(2,2 ))).thenReturn(goodMock);

        flattenedBoard.tryMove(badMock, XY.RIGHT_DOWN);

        verify(goodMock, never()).move(XY.RIGHT_DOWN);
        verify(boardMock, never()).removeEntity(goodMock);
        verify(boardMock, never()).addEntity(goodMock);
        verify(boardMock, never()).removeEntity(badMock);
        verify(boardMock, never()).addEntity(badMock);
    }
}
