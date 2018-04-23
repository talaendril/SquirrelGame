package ui;

import core.State;
import exceptions.NotEnoughEnergyException;
import exceptions.ScanException;

public class GameCommandRunner {

	private State state;
	
	public GameCommandRunner(State state) {
		this.state = state;
	}
	
	public void move(Object obj) {
		MoveCommand command = MoveCommand.parseMoveCommand((String) obj);
		if(command == null) {
			throw new ScanException("Unknown Command");	//TODO think about how to change this
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
		//TODO nothing here
	}
	
	public void spawnMiniSquirrel(Object energy) {	//parse needed
		try {
			this.state.getBoard().spawnMiniSquirrel(Integer.parseInt((String) energy));
		} catch (NotEnoughEnergyException e) {
			e.printStackTrace();
		}
	}
	
	public void getMasterSquirrelEnergy() {
		System.out.println(this.state.getBoard().getMaster().getEnergy());
	}
}
