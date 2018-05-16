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
import exceptions.ShouldNotBeCalledException;
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
		private final int sightRange = 15;
		
		ControllerContextImpl(EntityContext context) {
			this.entContext = context;
		}

		public XY getViewLowerLeft() {
			return new XY(MasterSquirrelBot.this.getLocation().x - this.sightRange, 
					MasterSquirrelBot.this.getLocation().y + this.sightRange);
		}

		@Override
		public XY getViewUpperRight() {
			return new XY(MasterSquirrelBot.this.getLocation().x + this.sightRange,
					MasterSquirrelBot.this.getLocation().x - this.sightRange);
		}

		@Override
		public EntityType getEntityAt(XY xy) {
			return this.entContext.getEntityType(xy);
		}

		@Override
		public void move(XY direction) {
			entContext.tryMove(MasterSquirrelBot.this, direction);
		}

		@Override
		public void spawnMiniBot(XY direction, int energy) {
			try {
				LOGGER.entering(MasterSquirrelBot.class.getName(), "spawnMiniBot(XY, int)");
				MiniSquirrel ms = spawnMiniSquirrel(MasterSquirrelBot.this.getLocation().plus(direction), energy);
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

		@Override
		public void implode(int impactRadius) {
			throw new ShouldNotBeCalledException();
		}

		@Override
		public XY locate() {
			return MasterSquirrelBot.this.getLocation();
		}

		@Override
		public boolean isMine(XY xy) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public XY directionOfMaster() {
			throw new ShouldNotBeCalledException();
		}

		@Override
		public long getRemainingSteps() {
			// TODO Auto-generated method stub
			return 0;
		}	
	}
}
