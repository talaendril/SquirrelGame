package entities;

import java.util.ArrayList;
import java.util.List;

import idmanager.ID;
import location.XY;

public class MasterSquirrel extends Squirrel {
	
	List<MiniSquirrel> production = new ArrayList<>();

	public MasterSquirrel(int id, XY location) {
		super(id, 1000, location);
	}
	
	@Override
	public String toString() {
		return "| MasterSquirrel" + super.toString();
	}
	
	public void spawnMiniSquirrel() {
		if (this.getEnergy() >= 1500) {
			production.add(new MiniSquirrel(ID.getNewID(), 500, new XY(this.getLocation(), XY.getVector(XY.randomNumber())), this));
			//TODO checking if new MiniSquirrel location is a valid location
		}
	}
	
	public boolean checkEntityInProduction(Entity entity) {
		return production.contains(entity);
	}
}
