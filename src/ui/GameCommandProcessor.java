package ui;

import core.Board;
import entities.MasterSquirrel;
import exceptions.EntityNotFoundException;
import exceptions.NotEnoughEnergyException;
import exceptions.ScanException;

public class GameCommandProcessor {
	private Board board;
	
	public GameCommandProcessor(Board board) {
		this.board = board;
	}
	
	public void process(Command command) {		//HANDLED ALL EXCEPTIONS HERE
		Object[] params = command.getParams();
		GameCommandType commandType = (GameCommandType) command.getCommandType();
		switch(commandType) {
		case ALL:
			System.out.println("No idea what to do with that Command");
			break;
		case DOWN:
			this.board.nextStep(MoveCommand.DOWN);
			break;
		case DOWN_LEFT:
			this.board.nextStep(MoveCommand.DOWN_LEFT);
			break;
		case DOWN_RIGHT:
			this.board.nextStep(MoveCommand.DOWN_RIGHT);
			break;
		case LEFT:
			this.board.nextStep(MoveCommand.LEFT);
			break;	
		case RIGHT:
			this.board.nextStep(MoveCommand.RIGHT);
			break;
		case UP:
			this.board.nextStep(MoveCommand.UP);
			break;
		case UP_LEFT:
			this.board.nextStep(MoveCommand.UP_LEFT);
			break;
		case UP_RIGHT:
			this.board.nextStep(MoveCommand.UP_RIGHT);
			break;
		case DO_NOTHING:
			this.board.nextStep(MoveCommand.NONE);
		case EXIT:
			System.exit(0);
			break;
		case HELP:
			this.help();
			break;
		case MASTER_ENERGY:	//TODO think about what happens with multiple MasterSquirrels
			MasterSquirrel ms = board.getMaster();
			if(ms == null) {
				throw new EntityNotFoundException("No MasterSquirrel in the EntitySet");	//TODO think about how to handle this situation
			} else {
				System.out.println("Our MasterSquirrel has " + ms.getEnergy());
			}
			break;
		case SPAWN_MINI:
			if(params.length == 0) {
				throw new ScanException("No Energy given");	//TODO think about how to handle this situation
			}
			try {
				board.flatten().spawnMiniSquirrel(Integer.parseInt((String) params[0]));
			} catch (NotEnoughEnergyException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	
	public void help() {
		StringBuilder sb = new StringBuilder("List of all Commands: \n");
		for(GameCommandType gct : GameCommandType.values()) {
			sb.append("\t" + gct.toString() + "\n");
		}
		System.out.println(sb.toString());
	}
}
