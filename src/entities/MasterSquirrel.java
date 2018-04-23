package entities;

import java.util.ArrayList;
import java.util.List;

import core.EntityContext;
import exceptions.NotEnoughEnergyException;
import idmanager.ID;
import location.XY;
import ui.CommandHandle.MoveCommand;

public class MasterSquirrel extends Squirrel {
	
	List<MiniSquirrel> production = new ArrayList<>();
	public static final int MINISQUIRREL_THRESHOLD = 1500;
	
	public MasterSquirrel(int id, XY location) {
		super(id, 1600, location);
	}
	
	@Override
	public String toString() {
		return "| MasterSquirrel" + super.toString();
	}
	
	public MiniSquirrel spawnMiniSquirrel(XY pos, int energy) throws NotEnoughEnergyException {
		if(this.getEnergy() < energy) {
			throw new NotEnoughEnergyException();
		}
		MiniSquirrel newMS = new MiniSquirrel(ID.getNewID(), energy, new XY(pos.getX(), pos.getY()), this);
		production.add(newMS);
		return newMS;	
	}
	
	public boolean checkEntityInProduction(Entity entity) {
		return production.contains(entity);
	}

	@Override
	public void nextStep(EntityContext context, MoveCommand command) {
		if(this.getStunnedAndDecrement()) {
			return;
		}
		context.tryMove(this, XY.getVector(command));
	}
}
