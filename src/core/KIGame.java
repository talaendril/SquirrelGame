package core;

import ui.UI;
import ui.commandhandle.MoveCommand;

import java.util.logging.Logger;

public class KIGame extends Game {

    private static final Logger LOGGER = Logger.getLogger(KIGame.class.getName());

	private final int FPS = 10;
	
	public KIGame(State state, Board board, UI ui) {
		super(state, board, ui);
		createMasters();
	}

	public void run(int steps) {
        Runnable updating = () -> {
            while(this.getCurrentStep() != steps) {
                update();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.severe("Updating Thread failed to sleep");
                }
            }
        };

		Runnable rendering = () -> {
		    while(true) {
                render();
                setMessageToMasterEnergy();
                setRemainingSteps();
                if(this.getUI().checkResetCalled()) {
                    this.getUI().changeResetCalled(false);
                    resetGame();
                    break;
                }
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    LOGGER.severe("Rendering Thread failed to sleep");
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