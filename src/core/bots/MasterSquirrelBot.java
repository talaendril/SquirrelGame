package core.bots;

import botapi.BotController;
import botapi.BotControllerFactory;
import botapi.ControllerContext;
import core.EntityType;
import entities.MasterSquirrel;
import location.XY;

public class MasterSquirrelBot extends MasterSquirrel implements ControllerContext {
	
	private BotControllerFactory botControllerFactory;
	private BotController masterBotController;

	public MasterSquirrelBot(int id, XY location) {
		super(id, location);
		// TODO Auto-generated constructor stub
	}
	
	public void nextStep(ControllerContext context) {
		//TODO:
	}

	@Override
	public XY getViewLowerLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XY getViewUpperRight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityType getEnityAt(XY xy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void move(XY direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnMiniBot(XY direction, int energy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}
}
