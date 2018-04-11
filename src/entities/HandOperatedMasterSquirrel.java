package entities;

import java.io.IOException;

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
	public XY tryNextStep() {
		try {
			XY next = new XY(this.getLocation(), XY.getVector(this.readInput()));
			this.setNextLocation(next);
			return next;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void nextStep() {	//not the perfect approach with tryNextStep but it works for now
		this.setLocation(this.getNextLocation());
	}
}
