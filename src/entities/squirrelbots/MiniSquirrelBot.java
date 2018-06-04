package entities.squirrelbots;

import botapi.BotController;
import botapi.BotControllerFactory;
import botapi.ControllerContext;
import core.EntityContext;
import core.EntityType;
import entities.Entity;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import exceptions.DynamicCreationFailureException;
import exceptions.ShouldNotBeCalledException;
import location.XY;
import location.XYSupport;
import ui.commandhandle.MoveCommand;

import java.util.logging.Logger;

public class MiniSquirrelBot extends MiniSquirrel {

    private static final Logger LOGGER = Logger.getLogger(MiniSquirrelBot.class.getName());
	
	private final BotControllerFactory botControllerFactory;
	private final BotController miniBotController;
	private ControllerContext contContext;

	public MiniSquirrelBot(int id, int energy, XY location, MasterSquirrel master, String name) {
		super(id, energy, location, master);
        Object placeHolder;
        try {
            Class<?> aClass = Class.forName("de.hsa.games.fatsquirrel.botimpls." + name + ".BotControllerFactoryImpl");
            placeHolder = aClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            LOGGER.severe("Could not create BotControllerFactory");
            throw new DynamicCreationFailureException("Could not create BotControllerFactory");
        }
        this.botControllerFactory = (BotControllerFactory) placeHolder;
        this.miniBotController = botControllerFactory.createMiniBotController();
	}
	
	private ControllerContext getControllerContext(EntityContext context) {
		if(this.contContext == null) {
			this.contContext = new ControllerContextImpl(context);
		}
		return this.contContext;
	}
	
	public void nextStep(EntityContext context, MoveCommand command) {
		this.updateEnergy(-1);
		if(this.getStunnedAndDecrement()) {
			return;
		}
		this.miniBotController.nextStep(this.getControllerContext(context));
	}

	private class ControllerContextImpl implements ControllerContext {
		
		private final EntityContext entContext;
		private final int sightRange = 10;
		
		ControllerContextImpl(EntityContext context) {
			this.entContext = context;
		}
		
		@Override
		public void implode(int impactRadius) {
			this.entContext.implode(MiniSquirrelBot.this, impactRadius);
		}

		@Override
		public XY getViewLowerLeft() {
			return new XY(MiniSquirrelBot.this.getLocation().x - this.sightRange, 
					MiniSquirrelBot.this.getLocation().y + this.sightRange);
		}

		@Override
		public XY getViewUpperRight() {
			return new XY(MiniSquirrelBot.this.getLocation().x + this.sightRange,
					MiniSquirrelBot.this.getLocation().x - this.sightRange);
		}

		@Override
		public EntityType getEntityAt(XY xy) {
			return this.entContext.getEntityType(xy);
		}

		@Override
		public void move(XY direction) {
			this.entContext.tryMove(MiniSquirrelBot.this, direction);
		}

		@Override
		public void spawnMiniBot(XY direction, int energy) {
			throw new ShouldNotBeCalledException();
		}

		@Override
		public int getEnergy() {
			return MiniSquirrelBot.this.getEnergy();
		}

		@Override
		public XY locate() {
			return MiniSquirrelBot.this.getLocation();
		}
		/*
		 * returns true if the Entity at xy is a MiniSquirrel of the same production as this MiniSquirrelBot
		 * (non-Javadoc)
		 * @see botapi.ControllerContext#isMine(location.XY)
		 */
		@Override
		public boolean isMine(XY xy) {		
			Entity e = this.entContext.getEntity(xy);
			if(EntityType.getEntityType(e) == EntityType.MASTER_SQUIRREL) {
				MasterSquirrel master = MiniSquirrelBot.this.getMaster();
				return master.equals(e);
			}
			if(EntityType.getEntityType(e) == EntityType.MINI_SQUIRREL) {
				MasterSquirrel master = MiniSquirrelBot.this.getMaster();
				return master.checkEntityInProduction(e);
			}
			return false;
		}

		@Override
		public XY directionOfMaster() {
			MasterSquirrel master = MiniSquirrelBot.this.getMaster();
			return XYSupport.getVectorBetween(MiniSquirrelBot.this.getLocation(), master.getLocation());
		}

		@Override
		public long getRemainingSteps() {
            return this.entContext.remainingSteps();
		}
	}
}