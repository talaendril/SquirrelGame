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
		MasterSquirrel master = new MasterSquirrelBot(ID.getNewID(), new XY(-1, -1));
		MasterSquirrel[] masters = {master};
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
	
	protected void setMessageToMasterEnergy() {
		StringBuilder sb = new StringBuilder("");
		MasterSquirrel[] masters = this.getMasters();
		for(int i = 0; i < masters.length; i++) {
			sb.append("Master Energy" + i + ": " + masters[i].getEnergy() + "\n");
		}
		this.getUI().message(sb.toString());
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