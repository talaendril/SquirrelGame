package entities.squirrelbots;

import botapi.BotController;
import botapi.BotControllerFactory;
import botapi.ControllerContext;
import botimpl.BotControllerFactoryImpl;
import core.EntityContext;
import core.EntityType;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import location.XY;
import ui.commandhandle.MoveCommand;

public class MiniSquirrelBot extends MiniSquirrel {
	
	private BotControllerFactory botControllerFactory = new BotControllerFactoryImpl();
	private BotController miniBotController = this.botControllerFactory.createMiniBotController();
	private ControllerContext contContext;

	public MiniSquirrelBot(int id, int energy, XY location, MasterSquirrel master) {
		super(id, energy, location, master);
	}
	
	private ControllerContext getControllerContext(EntityContext context) {
		if(this.contContext == null) {
			this.contContext = new ControllerContextImpl(context);
		}
		return this.contContext;
	}
	
	public void nextStep(EntityContext context, MoveCommand command) {
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public XY getViewUpperRight() {
			// TODO Auto-generated method stub
			return null;
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
			//should never be called
		}

		@Override
		public int getEnergy() {
			return MiniSquirrelBot.this.getEnergy();
		}

		@Override
		public XY locate() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isMine(XY xy) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public XY directionOfMaster() {
			MasterSquirrel master = MiniSquirrelBot.this.getMaster();
			return XY.getVectorBetween(MiniSquirrelBot.this.getLocation(), master.getLocation());
		}

		@Override
		public long getRemainingSteps() {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}