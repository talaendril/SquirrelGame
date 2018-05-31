package core;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.HandOperatedMasterSquirrel;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
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
	
	private static final Logger LOGGER = Logger.getLogger(SinglePlayer.class.getName());
	private final int FPS = 15;

	private Command nextCommand = new Command(GameCommandType.MOVE, MoveCommand.UP);

	public SinglePlayer(State state, Board board, UI ui) {
		super(state, board, ui);
		MasterSquirrel master = new HandOperatedMasterSquirrel(ID.getNewID(), new XY(-1, -1));
		MasterSquirrel[] masters = { master };
		this.addMasters(masters);
		this.getBoard().generateMasterSquirrels(masters);
	}

	public void run(int steps) {
		Timer renderTimer = new Timer();
		renderTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				render();
				setMessageToMasterEnergy();
			}
		}, 0, 1000 / FPS);

		Timer updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				processInput();
				update();
			}
		}, 0, 500);
	}

	@Override
	protected void render() {
		this.getUI().render(this.getBoard().flatten());
	}

	@Override
	protected void processInput() {
		nextCommand = this.getUI().getCommand();
		LOGGER.info(nextCommand.toString());
	}

	@Override
	protected void update() {
		Object params[] = nextCommand.getParams();
		GameCommandType type = (GameCommandType) nextCommand.getCommandType();
		switch (type) {
		case MOVE:
			if (params.length != 1) {
				throw new ScanException("Wrong Number of Parameters");
			}
			this.getState().update((MoveCommand) params[0]);
			break;
		case NOTHING:
			this.getState().update(MoveCommand.NONE);
			break;
		case SPAWN_MINI:
			if (params.length != 1) {
				throw new ScanException("Wrong Number of Parameters");
			}
			try {
				List<MasterSquirrel> masters = this.getMasters();	//this works if only one MasterSquirrel is in the game
				this.getBoard().spawnMiniSquirrel(masters.get(0), Integer.parseInt((String) params[0]));
				break;
			} catch (NumberFormatException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				e.printStackTrace();
			} catch (NotEnoughEnergyException e) {
				LOGGER.info("MasterSquirrel doesn't have enough energy to spawn a MiniSquirrel");
				e.printStackTrace();
			} catch (BelowThresholdException e) {
				LOGGER.info("MasterSquirrel doesn't have enough energy to hit the threshold");
			}
		case IMPLODE_MINI:
			if(params.length != 1) {
				throw new ScanException("Wrong Number of Parameters");
			}
			List<MasterSquirrel> masters = this.getMasters();
			List<MiniSquirrel> list = masters.get(0).getProduction();
			try {
				MiniSquirrel ms = list.get(0);
				int impactRadius = Integer.parseInt((String) params[0]);
				this.getBoard().flatten().implode(ms, impactRadius);
			} catch(IndexOutOfBoundsException e) {
				LOGGER.severe("Tried to implode a MiniSquirrel that doesn't exist");
			}
			break;
		default:
			break;
		}
	}
}