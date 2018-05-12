package entities.squirrelbots;

import java.util.logging.Logger;

import botapi.BotController;
import botapi.BotControllerFactory;
import botapi.ControllerContext;
import botimpl.BotControllerFactoryImpl;
import core.EntityContext;
import core.EntityType;
import core.logging.LoggingProxyFactory;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import exceptions.BelowThresholdException;
import exceptions.NotEnoughEnergyException;
import location.XY;
import ui.commandhandle.MoveCommand;

public class MasterSquirrelBot extends MasterSquirrel  {
	
	private static final Logger LOGGER = Logger.getLogger(MasterSquirrelBot.class.getName());
	
	private final BotControllerFactory botControllerFactory = new BotControllerFactoryImpl();	//TODO change location maybe
	private final BotController masterBotController = botControllerFactory.createMasterBotController();
	private ControllerContext contContext;

	public MasterSquirrelBot(int id, XY location) {
		super(id, location);
	}
	
	private ControllerContext getControllerContext(EntityContext context) {
		if(this.contContext == null) {
			ControllerContextImpl contContext = new ControllerContextImpl(context);
			this.contContext = (ControllerContext) LoggingProxyFactory.create(contContext, Integer.toString(this.getID()));
		}
		return this.contContext;
	}
	
	@Override
	public void nextStep(EntityContext context, MoveCommand command) {
		if(this.getStunnedAndDecrement()) {
			return;
		}
		this.masterBotController.nextStep(this.getControllerContext(context));
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
				LOGGER.entering(MasterSquirrelBot.class.getName(), "spawnMiniBot(XY, int)");
				MiniSquirrel ms = spawnMiniSquirrel(new XY(MasterSquirrelBot.this.getLocation(), direction), energy);
				MasterSquirrelBot.this.updateEnergy(-energy);
				this.entContext.addMiniSquirrel(ms);
			} catch (NotEnoughEnergyException e) {
				LOGGER.info("MasterSquirrel doesn't have enough energy to spawn a MiniSquirrel");
				e.printStackTrace();
			} catch (BelowThresholdException e) {
				LOGGER.info("MasterSquirrel doesn't have enough energy to hit the threshold");
			}
		}

		@Override
		public int getEnergy() {
			return MasterSquirrelBot.this.getEnergy();
		}	
	}
}
