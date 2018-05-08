package core.bots;

import botapi.BotController;
import botapi.BotControllerFactory;
import botapi.ControllerContext;
import core.EntityContext;
import core.EntityType;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import exceptions.NotEnoughEnergyException;
import location.XY;
import ui.commandhandle.MoveCommand;

public class MasterSquirrelBot extends MasterSquirrel  {
	
	private final BotControllerFactory botControllerFactory = new BotControllerFactoryImpl();	//TODO change location maybe
	private final BotController masterBotController = botControllerFactory.createMasterBotController();
	private ControllerContext contContext;

	public MasterSquirrelBot(int id, XY location) {
		super(id, location);
	}
	
	private ControllerContext getControllerContext(EntityContext context) {
		if(this.contContext == null) {
			this.contContext = new ControllerContextImpl(context);
		}
		return this.contContext;
	}
	
	@Override
	public void nextStep(EntityContext context, MoveCommand command) {
		if(this.getStunnedAndDecrement()) {
			return;
		}
		this.masterBotController.nextStep(getControllerContext(context));
		//this.contContext.spawnMiniBot(direction, 100);
	}
	
	private class ControllerContextImpl implements ControllerContext {
		
		private final EntityContext entContext;
		
		ControllerContextImpl(EntityContext context) {
			this.entContext = context;
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
			entContext.tryMove(MasterSquirrelBot.this, direction);
		}

		@Override
		public void spawnMiniBot(XY direction, int energy) {
			try {
				MiniSquirrel ms = spawnMiniSquirrel(new XY(MasterSquirrelBot.this.getLocation(), direction), energy);
			} catch (NotEnoughEnergyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public int getEnergy() {
			return MasterSquirrelBot.this.getEnergy();
		}	
	}
}
