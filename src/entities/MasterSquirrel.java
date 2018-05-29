package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import core.EntityContext;
import entities.squirrelbots.MiniSquirrelBot;
import exceptions.BelowThresholdException;
import exceptions.NotEnoughEnergyException;
import exceptions.ShouldNotBeCalledException;
import idmanager.ID;
import location.XY;
import location.XYSupport;
import ui.commandhandle.MoveCommand;

public class MasterSquirrel extends Squirrel {
	
	private static final Logger LOGGER = Logger.getLogger(MasterSquirrel.class.getName());
	
	List<MiniSquirrel> production = new ArrayList<>();
	public static final int MINISQUIRREL_THRESHOLD = 999;
	
	public MasterSquirrel(int id, XY location) {
		super(id, 1100, location);
	}
	
	@Override
	public void resetEnergy() {
		throw new ShouldNotBeCalledException();
	}
	
	@Override
	public String toString() {
		return "| MasterSquirrel" + super.toString();
	}
	
	public MiniSquirrel spawnMiniSquirrel(XY pos, int energy) throws NotEnoughEnergyException, BelowThresholdException {
		if(this.getEnergy() < MINISQUIRREL_THRESHOLD) {
			throw new BelowThresholdException();
		}
		if(this.getEnergy() < energy) {
			throw new NotEnoughEnergyException();
		}
		MiniSquirrel newMS = new MiniSquirrelBot(ID.getNewID(), energy, new XY(pos.x, pos.y), this);
		this.updateEnergy(-energy);
		production.add(newMS);
		LOGGER.exiting(MasterSquirrel.class.getName(), "spawnMiniSquirrel(XY, int)");
		return newMS;	
	}
	
	public String productionToString() {
		StringBuilder s = new StringBuilder();
		for(MiniSquirrel ms : production) {
			s.append(ms.toString() + "\n");
		}
		return s.toString();
	}
	
	public List<MiniSquirrel> getProduction() {
		return this.production;
	}
	
	public void removeFromProduction(Entity ms) {
		if(this.checkEntityInProduction(ms)) {
			this.production.remove(ms);
		}
	}
	
	public boolean checkEntityInProduction(Entity entity) {
		return production.contains(entity);
	}

	@Override
	public void nextStep(EntityContext context, MoveCommand command) {
		if(this.getStunnedAndDecrement()) {
			return;
		}
		context.tryMove(this, XYSupport.getVector(command));
	}
	
	@Override 
	public void updateEnergy(int delta) {
		if(this.getEnergy() == 1 && delta < 0) {
			return;
		}
		if(Math.abs(delta) >= this.getEnergy() && delta < 0) {
			super.updateEnergy((-this.getEnergy()) + 1);
		} else {
			super.updateEnergy(delta);
		}
	}
	
	@Override
	public void move(XY direction) {
		LOGGER.info("MASTERSQUIRREL" + this.getID() 
		+ " moves from " + this.getLocation().toString() 
		+ " in a direction of " + direction.toString());
		super.move(direction);
	}
}
