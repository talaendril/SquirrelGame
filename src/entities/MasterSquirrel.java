package entities;

import java.util.ArrayList;
import java.util.List;

import core.EntityContext;
import idmanager.ID;
import location.XY;
import ui.MoveCommand;

public class MasterSquirrel extends Squirrel {
	
	List<MiniSquirrel> production = new ArrayList<>();
	public static final int MINISQUIRREL_THRESHOLD = 1500;
	public static final int ENERGY_GIVEN = 150;
	
	public MasterSquirrel(int id, XY location) {
		super(id, 1000, location);
	}
	
	@Override
	public String toString() {
		return "| MasterSquirrel" + super.toString();
	}
	
	public MiniSquirrel spawnMiniSquirrel(XY pos) {
		MiniSquirrel newMS = new MiniSquirrel(ID.getNewID(), ENERGY_GIVEN, new XY(pos.getX(), pos.getY()), this);
		production.add(newMS);
		return newMS;	
	}
	
	public boolean checkEntityInProduction(Entity entity) {
		return production.contains(entity);
	}

	@Override
	public void nextStep(EntityContext context) {
		context.tryMove(this, XY.getVector(XY.randomNumber()));
	}
}
