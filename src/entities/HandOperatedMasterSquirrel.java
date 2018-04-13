package entities;

import java.io.IOException;

import core.EntityContext;
import location.XY;

public class HandOperatedMasterSquirrel extends MasterSquirrel {

	public HandOperatedMasterSquirrel(int id, XY location) {
		super(id, location);
	}
	
	public int readInput() throws IOException{
		System.out.println("Gib eine Zahl zwischen [1-9] (5 bedeutet:\"do nothing\"):");
		int number = System.in.read();
		while (number < '0' || number > '9') {
			number = System.in.read();
		}
		return number - '0';
	}
	
	@Override
	public String toString() {
		return "OPERATED " + super.toString();
	}
	
	@Override
	public void nextStep(EntityContext context) {
		try {
			context.tryMove(this, XY.getVector(this.readInput()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
