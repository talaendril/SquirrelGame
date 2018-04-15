package entities;

import location.XY;

public abstract class Character extends Entity {

	public Character(int id, int energy, XY xy) {
		super(id, energy, xy);
	}
}
