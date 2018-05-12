package entities.squirrelbots;

import botapi.BotController;
import botapi.BotControllerFactory;
import botapi.ControllerContext;
import core.EntityContext;
import core.EntityType;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import location.XY;
import ui.commandhandle.MoveCommand;

public class MiniSquirrelBot extends MiniSquirrel {
	
	private BotControllerFactory botControllerFactory;
	private BotController miniBotController;
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void spawnMiniBot(XY direction, int energy) {
			//should never be called
		}

		@Override
		public int getEnergy() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
}