package core;

import entities.BadBeast;
import entities.Entity;
import entities.GoodBeast;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import entities.Squirrel;
import entities.Wall;
import entities.Character;
import location.XY;

public class FlattenedBoard implements EntityContext, BoardView {
	
	private Board board;
	private EntitySet cells;

	public FlattenedBoard(EntitySet es, Board b) {
		this.cells = es;
		this.board = b;
	}

	@Override
	public EntityType getEntityType(int x, int y) {
		return EntityType.getEntityType(cells.getEntity(new XY(x, y)));
	}
	
	@Override
	public EntityType getEntityType(XY xy) {
		return EntityType.getEntityType(cells.getEntity(xy));
	}

	@Override
	public XY getSize() {
		return new XY(board.getBoardSizeX(), board.getBoardSizeY());
	}

	@Override
	public void tryMove(MiniSquirrel ms, XY direction) {
		if(ms.getStunned()) {
			return;
		}
		
		Entity e = cells.getEntity(new XY(ms.getLocation(), direction));
		if (e != null) {
			if (e instanceof Character) {
				if (e instanceof MasterSquirrel) {
					if (e.equals(ms.getMaster())) {
						ms.getMaster().updateEnergy(ms.getEnergy());
					} else {
						e.updateEnergy(MiniSquirrel.ENERGY_GAIN_NOT_MASTER);
					}
					this.kill(ms);
				} else if (e instanceof MiniSquirrel) {
					this.kill(e);
					this.kill(ms);
				} else if (e instanceof BadBeast) {
					if (((BadBeast) e).getBiteCounter() == BadBeast.MAXIMUM_BITECOUNT) {
						this.killAndReplace(e);
					}
					if(ms.updateEnergy(e.getEnergy())) {
						//TODO move ms
					} else {
						this.kill(ms);
					}
				} else if (e instanceof GoodBeast) {
					ms.updateEnergy(e.getEnergy());
					this.killAndReplace(e);
				} 
			} else {
				if(!ms.updateEnergy(e.getEnergy())) {
					this.kill(ms);
				}
				if(e instanceof Wall) {
					ms.setStunned();
				} else {
					this.killAndReplace(e);
					//TODO move ms
				}
			}
		} 
		//TODO move ms
	}

	@Override
	public void tryMove(GoodBeast gb, XY direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tryMove(BadBeast bb, XY direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tryMove(MasterSquirrel master, XY direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Squirrel nearestPlayerEntity(XY pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void kill(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void killAndReplace(Entity entity) {
		// TODO Auto-generated method stub
		
	}
}
