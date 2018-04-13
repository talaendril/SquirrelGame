package core;

import java.util.Iterator;

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
	private Entity[][] entityMatrix;

	public FlattenedBoard(EntitySet es, Board b) {
		this.cells = es;
		this.board = b;
		entityMatrix = new Entity[this.board.getBoardSizeY()][this.board.getBoardSizeX()];
		
		Iterator<Entity> iterator = cells.getEntities().iterator();
		while(iterator.hasNext()) {
			Entity e = iterator.next();
			entityMatrix[e.getLocation().getY()][e.getLocation().getX()] = e;
		}
	}
	
	public Entity[][] getEntityMatrix() {
		return entityMatrix;
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
					if(!ms.updateEnergy(e.getEnergy())) {
						this.kill(ms);
					} else {
						ms.move(direction);
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
					ms.move(direction);
				}
			}
		} else {
			ms.move(direction);
		}
	}

	@Override
	public void tryMove(GoodBeast gb, XY direction) {
		if(gb.getStepCounter() == GoodBeast.MAXIMUM_STEPCOUNT) {
			return;
		}
		Entity e = cells.getEntity(new XY(gb.getLocation(), direction));
		if(e != null) {
			if(e instanceof Squirrel) {
				e.updateEnergy(gb.getEnergy());
				this.killAndReplace(gb);
			} else {
				//do nothing
			}
		} else {
			gb.move(direction);
		}
	}

	@Override
	public void tryMove(BadBeast bb, XY direction) {
		if(bb.getStepCounter() == BadBeast.MAXIMUM_STEPCOUNT) {
			return;
		}
		Entity e = cells.getEntity(new XY(bb.getLocation(), direction));
		if(e != null) {
			if(e instanceof Squirrel) {
				if(!e.updateEnergy(bb.getEnergy())) {
					this.killAndReplace(e);
				}
				if(bb.getBiteCounter() == BadBeast.MAXIMUM_BITECOUNT) {
					this.killAndReplace(bb);
				} else {
					bb.move(direction);
				}
			} else {
				//do nothing
			}
		} else {
			bb.move(direction);
		}
	}

	@Override
	public void tryMove(MasterSquirrel master, XY direction) {
		if(master.getStunned()) {
			return;
		}
		
		Entity e = cells.getEntity(new XY(master.getLocation(), direction));
		if(e != null) {
			if(e instanceof Character) {
				if(e instanceof MasterSquirrel) {
					//do nothing
				} else if(e instanceof MiniSquirrel) {
					if(master.checkEntityInProduction(e)) {
						master.updateEnergy(e.getEnergy()); 
					} else {
						master.updateEnergy(MiniSquirrel.ENERGY_GAIN_NOT_MASTER);
					}
					this.kill(e);
				} else {
					if(e instanceof BadBeast) {
						if(((BadBeast) e).getBiteCounter() == BadBeast.MAXIMUM_BITECOUNT) {
							this.killAndReplace(e);
						}
					} else {
						this.killAndReplace(e);
					}
					master.updateEnergy(e.getEnergy());
				} 
			} else {
				if(e instanceof Wall) {
					master.setStunned();
				} else {
					this.killAndReplace(e);
				}
				master.updateEnergy(e.getEnergy());
				
			}
		} else {
			master.move(direction);
		}
	}

	@Override
	public Squirrel nearestPlayerEntity(XY pos) {
		// TODO check if any Entity is within 6 steps of another Entity
		return null;
	}

	@Override
	public void kill(Entity entity) {
		cells.removeEntity(entity);
		entityMatrix[entity.getLocation().getY()][entity.getLocation().getX()] = null;
	}

	@Override
	public void killAndReplace(Entity entity) {
		this.kill(entity);
		XY newLocation;
		do {
			newLocation = XY.getRandomLocationBetween(board.getBoardSizeX() - 1, board.getBoardSizeY() - 1);
		} while(cells.getEntity(newLocation) != null);
		entity.setLocation(newLocation);
		cells.addEntity(entity);
		entityMatrix[entity.getLocation().getY()][entity.getLocation().getX()] = entity;
	}
}
