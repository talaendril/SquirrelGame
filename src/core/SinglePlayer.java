package core;

import java.util.Timer;
import java.util.TimerTask;

import entities.HandOperatedMasterSquirrel;
import entities.MasterSquirrel;
import exceptions.BelowThresholdException;
import exceptions.NotEnoughEnergyException;
import exceptions.ScanException;
import idmanager.ID;
import location.XY;
import ui.UI;
import ui.commandhandle.Command;
import ui.commandhandle.GameCommandType;
import ui.commandhandle.MoveCommand;

public class SinglePlayer extends Game {
	
	private final int FPS = 10;
	
	//private static int processingCount = 0;		//TODO remove once down debugging

	private Command nextCommand = new Command(GameCommandType.NOTHING);
	
	public SinglePlayer(State state, Board board, UI ui) {
		super(state, board, ui);
		MasterSquirrel master = new HandOperatedMasterSquirrel(ID.getNewID(), new XY(-1, -1));
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
		
		Timer inputTimer = new Timer();
		inputTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				processInput();
			}
		}, 0, 500);
		
		Timer updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				update();
			}
		}, 950, 1000);
	}
	
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
	
	protected void processInput() {
		nextCommand = this.getUI().getCommand();
		System.err.println(nextCommand.toString());
	}
	
	protected void update() {
		Object params[] = nextCommand.getParams();
		GameCommandType type = (GameCommandType) nextCommand.getCommandType();
		switch(type) {
		case MOVE:
			if(params.length != 1) {
				throw new ScanException("Wrong Number of Parameters");
			}
			this.getState().update(MoveCommand.parseMoveCommand(params[0].toString()));
			break;
		case NOTHING:
			this.getState().update(MoveCommand.NONE);
			break;
		case SPAWN_MINI:
			if(params.length != 1) {
				throw new ScanException("Wrong Number of Parameters");
			}
			try {
				MasterSquirrel[] masters = this.getMasters();
				this.getBoard().spawnMiniSquirrel(masters[0], Integer.parseInt((String) params[0]));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (NotEnoughEnergyException e) {
				e.printStackTrace();
			} catch (BelowThresholdException e) {}
			break;
		default:
			break;
		}
	}
}
