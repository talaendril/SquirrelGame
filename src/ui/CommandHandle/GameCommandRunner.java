package ui.commandhandle;

import core.State;
import entities.MasterSquirrel;
import exceptions.BelowThresholdException;
import exceptions.NotEnoughEnergyException;
import exceptions.ScanException;

public class GameCommandRunner {
	/*
	 * this class is used to work with the processed command
	 * meaning it is used as the reflection and any method invoked in Game will be called here
	 * this class then continues to call either Board, prints help, exit System, etc
	 * 
	 * TODO needs cleanup since KeyEvents have been implemented in exercise 5 and certain commands arent called anymore
	 * ALSO FIX GAMECOMMANDTYPE
	 */

	private State state;
	
	public GameCommandRunner(State state) {
		this.state = state;
	}
	
	public void move(Object[] obj) {	//needs a MoveCommand to work properly
		//MoveCommand command = MoveCommand.parseMoveCommand((String) obj);
		if(obj.length != 1) {
			throw new ScanException("Too many Parameters");
		}
		MoveCommand command = (MoveCommand) obj[0];
		if(command == null) {
			throw new ScanException("Unknown Direction");	//TODO think about how to change this
		}
		this.state.update(command);
	}
	
	public void spawnMiniSquirrel(Object[] params) {
		if(params.length != 2) {
			throw new ScanException("Wrong Number of Parameters");
		}
		try {
			this.state.getBoard().spawnMiniSquirrel((MasterSquirrel) params[0], Integer.parseInt((String) params[1]));
			/*
			 * TODO maybe add a check for wrong order
			 */
		} catch (NotEnoughEnergyException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			throw new ScanException("No Energy specified!");
		} catch (BelowThresholdException e) {}
	}
	
	public void doNothing() {
		this.state.update(MoveCommand.NONE);
	}
}
