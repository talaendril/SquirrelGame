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
	private Entity[][] entityMatrix;

	public FlattenedBoard(EntitySet es, Board b) {
		this.cells = es;
		this.board = b;
		entityMatrix = new Entity[this.board.getBoardSizeY()][this.board.getBoardSizeX()];
		
		for(Entity e : cells.getEntities()) {
			if (e != null) {
				entityMatrix[e.getLocation().getY()][e.getLocation().getX()] = e;
			}
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
		Entity squirrel;
		if((squirrel = this.nearestPlayerEntity(gb.getLocation())) != null) {
			gb.setPreferredDirection(this.bestVectorAwayFromEntity(gb, squirrel));
		} else {
			gb.setPreferredDirection(null);
		}
		if (gb.getStepCounter() == GoodBeast.MAXIMUM_STEPCOUNT) {
			Entity e = cells.getEntity(new XY(gb.getLocation(), direction));
			if (e != null) {
				if (e instanceof Squirrel) {
					e.updateEnergy(gb.getEnergy());
					this.killAndReplace(gb);
				} else {
					//do nothing
				}
			} else {
				gb.move(direction);
			} 
		}
	}

	@Override
	public void tryMove(BadBeast bb, XY direction) {
		Entity squirrel;
		if((squirrel = this.nearestPlayerEntity(bb.getLocation())) != null) {
			bb.setPreferredDirection(this.bestVectorToEntity(bb, squirrel));
		} else {
			bb.setPreferredDirection(null);
		}
		if (bb.getStepCounter() == BadBeast.MAXIMUM_STEPCOUNT) {
			Entity e = cells.getEntity(new XY(bb.getLocation(), direction));
			if (e != null) {
				if (e instanceof Squirrel) {
					if (!e.updateEnergy(bb.getEnergy())) {
						this.kill(e);
					}
					if (bb.getBiteCounter() == BadBeast.MAXIMUM_BITECOUNT) {
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
					master.move(direction);
				}
				master.updateEnergy(e.getEnergy());
			}
		} else {
			master.move(direction);
		}
	}
	
	public Squirrel nearestPlayerEntity(XY pos) {
		int count = 0;
		int size = 6;
		int elements = size * size;
		int x = pos.getX(), y = pos.getY();
		Entity e = null;
		
		int left = -1, down = 1, right = 2, up = -2;
		
		while(count < elements-1) {
			for(int i = 0; i > left; i--) {
				y--;
				if((e = cells.getEntity(new XY(x, y++))) != null && e instanceof Squirrel) {
					return (Squirrel) e;
				}
				count++;
			}
			for(int i = 0; i < down; i++) {
				x++;
				if((e = cells.getEntity(new XY(x, y++))) != null && e instanceof Squirrel) {
					return (Squirrel) e;
				}
				count++;
			}
			for(int i = 0; i < right ;i++) {
				y--;
				if((e = cells.getEntity(new XY(x, y++))) != null && e instanceof Squirrel) {
					return (Squirrel) e;
				}
				count++;
			}
			for(int i = 0; i > up; i--) {
				x--;
				if((e = cells.getEntity(new XY(x, y++))) != null && e instanceof Squirrel) {
					return (Squirrel) e;
				}
				count++;
			}
			left -= 2;
			down += 2;
			right += 2;
			up -= 2;
		}
		return null;
	}
	
	public XY bestVectorToEntity(BadBeast beast, Entity target) {	
		int deltaX = target.getLocation().getX() - beast.getLocation().getX();
		int deltaY = target.getLocation().getY() - beast.getLocation().getY();
		if(deltaX == 0 && deltaY > 0) {	//Squirrel is above you
			return XY.getVector(8);
		}
		if(deltaX == 0 && deltaY < 0) {	//Squirrel is below you
			return XY.getVector(2);
		}
		if(deltaX > 0 && deltaY == 0) {	//Squirrel is right of you
			return XY.getVector(6);
		}
		if(deltaX < 0 && deltaY == 0) {	//Squirrel is left of you
			return XY.getVector(4);
		}
		if(deltaX > 0 && deltaY > 0) {	//Squirrel is up and right
			return XY.getVector(9);
		}
		if(deltaX < 0 && deltaY > 0) {	//Squirrel is up and left
			return XY.getVector(7);
		}
		if(deltaX > 0 && deltaY < 0) {	//Squirrel is down and right
			return XY.getVector(3);
		}
		if(deltaX < 0 && deltaY < 0) {	//Squirrel is down and left
			return XY.getVector(1);
		}
		return XY.getVector(5);	//default: do nothing
	}
	
	public XY bestVectorAwayFromEntity(GoodBeast beast, Entity hunter) {
		int deltaX = hunter.getLocation().getX() - beast.getLocation().getX();
		int deltaY = hunter.getLocation().getY() - beast.getLocation().getY();
		if(deltaX == 0 && deltaY > 0) {	//Squirrel is above you
			return XY.getVector(2);
		}
		if(deltaX == 0 && deltaY < 0) {	//Squirrel is below you
			return XY.getVector(8);
		}
		if(deltaX > 0 && deltaY == 0) {	//Squirrel is right of you
			return XY.getVector(4);
		}
		if(deltaX < 0 && deltaY == 0) {	//Squirrel is left of you
			return XY.getVector(6);
		}
		if(deltaX > 0 && deltaY > 0) {	//Squirrel is up and right
			return XY.getVector(1);
		}
		if(deltaX < 0 && deltaY > 0) {	//Squirrel is up and left
			return XY.getVector(3);
		}
		if(deltaX > 0 && deltaY < 0) {	//Squirrel is down and right
			return XY.getVector(7);
		}
		if(deltaX < 0 && deltaY < 0) {	//Squirrel is down and left
			return XY.getVector(9);
		}
		return XY.getVector(5);	//default: do nothing
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