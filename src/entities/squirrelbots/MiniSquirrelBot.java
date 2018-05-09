package entities.squirrelbots;

import botapi.BotController;
import botapi.BotControllerFactory;
import botapi.ControllerContext;
import core.EntityType;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import location.XY;

public class MiniSquirrelBot extends MiniSquirrel {
	
	private BotControllerFactory botControllerFactory;
	private BotController miniBotController;

	public MiniSquirrelBot(int id, int energy, XY location, MasterSquirrel master) {
		super(id, energy, location, master);
		// TODO Auto-generated constructor stub
	}
	
	public void nextStep(ControllerContext context) {
		//TODO:
	}

	private class ControllerContextImplt implements ControllerContext {

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
}