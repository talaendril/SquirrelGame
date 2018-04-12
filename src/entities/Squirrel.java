package entities;

import location.XY;

public class Squirrel extends Character {
	
	private int stunnedSteps = 3;
	private boolean stunned = false;
	
	public Squirrel(int id, int energy, XY location) {
		super(id, energy, location);
	}
	
	public int getStunnedSteps() {
		return this.stunnedSteps;
	}
	
	public boolean getStunned() {
		if(this.stunned && this.stunnedSteps > 0) {
			this.stunnedSteps--;
			return true;
		}
		return false;
	}
	
	public void setStunned() {
		this.stunned = true;
		this.stunnedSteps = 3;
	}
}
