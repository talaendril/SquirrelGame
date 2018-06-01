package core;

import entities.MasterSquirrel;
import entities.squirrelbots.MasterSquirrelBot;
import idmanager.ID;
import location.XY;
import ui.UI;
import ui.commandhandle.MoveCommand;
import ui.windows.OutputWindow;

import java.util.logging.Logger;

public class KIGame extends Game {

    private static final Logger LOGGER = Logger.getLogger(KIGame.class.getName());

	private final int FPS = 10;
	
	public KIGame(State state, Board board, UI ui) {
		super(state, board, ui);
		this.getHighScoresFromFile();

        int x, y = x = -1;
        String[] bots = this.getBoard().getConfig().getBotNames();

        MasterSquirrel master1 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--), bots[0]);
        MasterSquirrel master2 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--), bots[1]);
        MasterSquirrel master3 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--), bots[0]);
        MasterSquirrel master4 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--), bots[1]);
        MasterSquirrel[] masters = {master1, master2, master3, master4};
        this.addMasters(masters);
        this.getBoard().generateMasterSquirrels(masters);
	}

	public void run(int steps) {
		Runnable rendering = () -> {
		    while(true) {
                render();
                setMessageToMasterEnergy();
                setRemainingSteps();
                if(this.getUI().checkResetCalled()) {
                    break;
                }
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    LOGGER.severe("Rendering Thread failed to sleep");
                }
            }
        };

        Runnable updating = () -> {
            while(true) {
                if(this.getCurrentStep() != steps) {
                    update();
                }
                if (this.getUI().checkResetCalled()) {
                    this.getUI().changeResetCalled(false);
                    resetGame();
                    break;
                }
                if(this.getCurrentStep() == steps && !(this.getUI().checkResetCalled()) && !this.savedHighScores) {
                    this.saveHighScores();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.severe("Updating Thread failed to sleep");
                }
            }
        };

		Thread renderThread = new Thread(rendering);
		Thread updateThread = new Thread(updating);
		renderThread.start();
        try {
            Thread.sleep(950);
        } catch (InterruptedException e) {
            LOGGER.severe("Failed to sleep Thread between calling rendering and updating");
        }
        updateThread.start();
	}
	
	@Override
	protected void render() {
		this.getUI().render(this.getBoard().flatten());
	}
	
	@Override
	protected void update() {
	    this.incrementCurrentStep();
		this.getState().update(MoveCommand.NONE);
	}

	@Override
	protected void processInput() {
		//do nothing
	}
}