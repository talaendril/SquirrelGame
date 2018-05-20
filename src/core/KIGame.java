package core;

import java.util.Timer;
import java.util.TimerTask;

import entities.MasterSquirrel;
import entities.squirrelbots.MasterSquirrelBot;
import idmanager.ID;
import location.XY;
import ui.UI;
import ui.commandhandle.MoveCommand;

public class KIGame extends Game {

	private final int FPS = 10;
	
	public KIGame(State state, Board board, UI ui) {
		super(state, board, ui);
		int x, y = x = -1;
		MasterSquirrel master1 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--));
		MasterSquirrel master2 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--));
		MasterSquirrel master3 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--));
		MasterSquirrel master4 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--));
		MasterSquirrel[] masters = {master1, master2, master3, master4};
		this.addMasters(masters);
		this.getBoard().generateMasterSquirrels(masters);
	}
	
	public void run() {
		Timer renderTimer = new Timer();
		renderTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				render();
				setMessageToMasterEnergy();
			}
		}, 0, 1000/FPS);
		
		Timer updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				update();
			}
		}, 950, 1000);
	}
	
	@Override
	protected void render() {
		this.getUI().render(this.getBoard().flatten());
	}
	
	@Override
	protected void update() {
		this.getState().update(MoveCommand.NONE);
	}

	@Override
	protected void processInput() {
		//do nothing
	}
}