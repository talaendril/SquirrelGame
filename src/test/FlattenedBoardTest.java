package test;

import core.FlattenedBoard;
import entities.*;
import exceptions.BelowThresholdException;
import exceptions.NotEnoughEnergyException;
import location.XY;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FlattenedBoardTest {

    @Mock
    private MasterSquirrel master;
    private BadBeast badBeast;
    private GoodBeast goodBeast;
    private BadPlant badPlant;
    private GoodPlant goodPlant;
    private MiniSquirrel miniSquirrel;

    @InjectMocks
    private FlattenedBoard flattenedBoard;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        try {
            miniSquirrel = master.spawnMiniSquirrel(new XY(0, 0), 100);
        } catch (NotEnoughEnergyException e) {
            e.printStackTrace();
        } catch (BelowThresholdException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tryMoveMasterTest() {

    }
}
