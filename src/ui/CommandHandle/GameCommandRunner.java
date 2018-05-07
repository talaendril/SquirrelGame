package ui.commandhandle;

import core.State;
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
	
	public void move(Object obj) {	//needs a MoveCommand to work properly
		//MoveCommand command = MoveCommand.parseMoveCommand((String) obj);
		MoveCommand command = (MoveCommand) obj;
		if(command == null) {
			throw new ScanException("Unknown Direction");	//TODO think about how to change this
		}
		if(this.state == null) {
			System.out.println("makes no sense");
		}
		this.state.update(command);
	}
	
	public void exitSystem() {
		System.exit(0);
	}
	
	public void help() {
		StringBuilder sb = new StringBuilder("List of all Commands: \n");
		for(GameCommandType gct : GameCommandType.values()) {
			sb.append("\t" + gct.toString() + "\n");
		}
		System.out.println(sb.toString());
	}
	
	public void all() {
		System.out.println("You have used a command which has no proper effect yet");
	}
	
	public void spawnMiniSquirrel(Object energy) {	//needs an int to work properly
		try {
			this.state.getBoard().spawnMiniSquirrel(Integer.parseInt((String) energy));
		} catch (NotEnoughEnergyException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			throw new ScanException("No Energy specified!");
		}
	}
	
	public void getMasterSquirrelEnergy() {
		System.out.println(this.state.getBoard().getMaster().getEnergy());
	}
	
	public void doNothing() {
		this.state.update(MoveCommand.NONE);
	}
}
