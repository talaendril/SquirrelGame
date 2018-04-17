package core;

import entities.BadBeast;
import entities.BadPlant;
import entities.Entity;
import entities.GoodBeast;
import entities.GoodPlant;
import entities.HandOperatedMasterSquirrel;
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
	
	@Override
	public void printBoard() {
		for(int i = 0; i < entityMatrix.length; i++) {
			for(int j = 0; j < entityMatrix[0].length; j++) {
				if(entityMatrix[i][j] instanceof Wall) {
					System.out.print("W\t");
				} else if(entityMatrix[i][j] instanceof BadBeast) {
					System.out.print("BB\t");
				} else if(entityMatrix[i][j] instanceof GoodBeast) {
					System.out.print("GB\t");
				} else if(entityMatrix[i][j] instanceof BadPlant) {
					System.out.print("BP\t");
				} else if(entityMatrix[i][j] instanceof GoodPlant) {
					System.out.print("GP\t");
				} else if(entityMatrix[i][j] instanceof HandOperatedMasterSquirrel) {
					HandOperatedMasterSquirrel homs = (HandOperatedMasterSquirrel) entityMatrix[i][j];
					System.out.print("OS" + homs.getID() + "\t");
				} else if(entityMatrix[i][j] instanceof MasterSquirrel) {
					MasterSquirrel ms = (MasterSquirrel) entityMatrix[i][j];
					System.out.print("S" + ms.getID() + "\t");
				} else if(entityMatrix[i][j] instanceof MiniSquirrel) {
					MiniSquirrel ms = (MiniSquirrel) entityMatrix[i][j];
					System.out.print("s" + ms.getMaster().getID() + "\t");
				} else {
					System.out.print(".\t");
				}
			}
			System.out.println("");
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
		if(ms.getStunned() || direction == XY.ORIGIN) {
			return;
		}
		Entity e = cells.getEntity(new XY(ms.getLocation(), direction));
		if (e != null) {
			if (e instanceof Character) {
				if (EntityType.getEntityType(e) == EntityType.MASTERSQUIRREL) {
					if (e.equals(ms.getMaster())) {
						ms.getMaster().updateEnergy(ms.getEnergy());
					} else {
						e.updateEnergy(MiniSquirrel.ENERGY_GAIN_NOT_MASTER);
					}
					this.kill(ms);
				} else if (EntityType.getEntityType(e) == EntityType.MINISQUIRREL) {
					this.kill(e);
					this.kill(ms);
				} else if (EntityType.getEntityType(e) == EntityType.BADBEAST) {
					if (((BadBeast) e).getBiteCounter() == BadBeast.MAXIMUM_BITECOUNT) {
						this.killAndReplace(e);
					}
					if(!ms.updateEnergy(e.getEnergy())) {
						this.kill(ms);
					} else {
						ms.move(direction);
					}
				} else if (EntityType.getEntityType(e) == EntityType.GOODBEAST) {
					ms.updateEnergy(e.getEnergy());
					this.killAndReplace(e);
				} 
			} else {
				if(!ms.updateEnergy(e.getEnergy())) {
					this.kill(ms);
				}
				if(EntityType.getEntityType(e) == EntityType.WALL) {
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
		XY vector = direction;
		Entity squirrel;
		if((squirrel = this.nearestPlayerEntity(gb.getLocation())) != null) {
			vector = this.bestVectorAwayFromEntity(gb, squirrel);
		} 
		if (gb.getStepCounter() == GoodBeast.MAXIMUM_STEPCOUNT) {
			System.out.println("GoodBeast" + gb.getID() + " tries to move from " + gb.getLocation().toString() + " in a direction of " + vector.toString());
			Entity e = cells.getEntity(new XY(gb.getLocation(), vector));
			if (e != null) {
				if (e instanceof Squirrel) {
					e.updateEnergy(gb.getEnergy());
					this.killAndReplace(gb);
				} else {
					//do nothing
				}
			} else {
				gb.move(vector);
			} 
		}
	}

	@Override
	public void tryMove(BadBeast bb, XY direction) {
		XY vector = direction;
		Entity squirrel;
		if((squirrel = this.nearestPlayerEntity(bb.getLocation())) != null) {
			vector = this.bestVectorToEntity(bb, squirrel);
		} 
		if (bb.getStepCounter() == BadBeast.MAXIMUM_STEPCOUNT) {
			System.out.println("BadBeast" + bb.getID() + " tries to move from " + bb.getLocation().toString() + " in a direction of " + vector.toString());
			Entity e = cells.getEntity(new XY(bb.getLocation(), vector));
			if (e != null) {
				if (e instanceof Squirrel) {
					if (!e.updateEnergy(bb.getEnergy())) {
						this.kill(e);
					}
					if (bb.getBiteCounter() == BadBeast.MAXIMUM_BITECOUNT) {
						this.killAndReplace(bb);
					} else {
						//do nothing
					}
				} else {
					//do nothing
				}
			} else {
				bb.move(vector);
			} 
		}
	}

	@Override
	public void tryMove(MasterSquirrel master, XY direction) {
		if(master.getStunned() || direction == XY.ORIGIN) {
			return;
		}
		Entity e = cells.getEntity(new XY(master.getLocation(), direction));
		if(e != null) {
			if(e instanceof Character) {
				if(EntityType.getEntityType(e) == EntityType.MASTERSQUIRREL) {
					//do nothing
				} else if(EntityType.getEntityType(e) == EntityType.MINISQUIRREL) {
					if(master.checkEntityInProduction(e)) {
						master.updateEnergy(e.getEnergy()); 
					} else {
						master.updateEnergy(MiniSquirrel.ENERGY_GAIN_NOT_MASTER);
					}
					this.kill(e);
				} else {
					if(EntityType.getEntityType(e) == EntityType.BADBEAST) {
						if(((BadBeast) e).getBiteCounter() == BadBeast.MAXIMUM_BITECOUNT) {
							this.killAndReplace(e);
						}
					} else {
						this.killAndReplace(e);
					}
					master.updateEnergy(e.getEnergy());
				} 
			} else {
				if(EntityType.getEntityType(e) == EntityType.WALL) {
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
		if(master.getEnergy() > MasterSquirrel.MINISQUIRREL_THRESHOLD) {
			XY location = cells.getEmptyLocationAround(master.getLocation());
			if(location == null) {
				return;
			} else {
				MiniSquirrel newMS = master.spawnMiniSquirrel(location);
				master.updateEnergy(-MasterSquirrel.ENERGY_GIVEN);
				cells.addEntity(newMS);
			}
		}
	}
	
	public Squirrel nearestPlayerEntity(XY pos) {
		Squirrel[] array = new Squirrel[36];
		int arrayPosition = 0;
		for(int i = pos.getY() - 6; i < pos.getY() + 6; i++) {
			for(int j = pos.getX() - 6; j < pos.getX() + 6; j++) {
				Entity e = cells.getEntity(new XY(j, i));
				if(e instanceof Squirrel) {
					array[arrayPosition++] = (Squirrel) e;
				}
			}
		}
		if(array[0] == null) {
			return null;
		}
		Squirrel nearest = null;
		double shortestDistance = entityMatrix.length * entityMatrix.length;	//initialized with something big
		double newDistance;
		for(int i = 0; i < array.length; i++) {
			if(array[i] != null) {
				if((newDistance = XY.distanceBetween(array[i].getLocation(), pos)) < shortestDistance) {
					shortestDistance = newDistance;
					nearest = array[i];
				}
			}
		}
		return nearest;
	}
	
	public XY bestVectorToEntity(BadBeast beast, Entity target) {	
		int deltaX = target.getLocation().getX() - beast.getLocation().getX();
		int deltaY = target.getLocation().getY() - beast.getLocation().getY();
		if(deltaX == 0 && deltaY > 0) {	//Squirrel is above you
			return XY.getVector(2);
		}
		if(deltaX == 0 && deltaY < 0) {	//Squirrel is below you
			return XY.getVector(8);
		}
		if(deltaX > 0 && deltaY == 0) {	//Squirrel is right of you
			return XY.getVector(6);
		}
		if(deltaX < 0 && deltaY == 0) {	//Squirrel is left of you
			return XY.getVector(4);
		}
		if(deltaX > 0 && deltaY > 0) {	//Squirrel is up and right
			return XY.getVector(3);
		}
		if(deltaX < 0 && deltaY > 0) {	//Squirrel is up and left
			return XY.getVector(1);
		}
		if(deltaX > 0 && deltaY < 0) {	//Squirrel is down and right
			return XY.getVector(9);
		}
		if(deltaX < 0 && deltaY < 0) {	//Squirrel is down and left
			return XY.getVector(7);
		}
		return XY.getVector(XY.randomNumber());
	}
	
	public XY bestVectorAwayFromEntity(GoodBeast beast, Entity hunter) {
		int deltaX = hunter.getLocation().getX() - beast.getLocation().getX();
		int deltaY = hunter.getLocation().getY() - beast.getLocation().getY();
		if(deltaX == 0 && deltaY > 0) {	//Squirrel is above you
			return XY.getVector(8);
		}
		if(deltaX == 0 && deltaY < 0) {	//Squirrel is below you
			return XY.getVector(2);
		}
		if(deltaX > 0 && deltaY == 0) {	//Squirrel is right of you
			return XY.getVector(4);
		}
		if(deltaX < 0 && deltaY == 0) {	//Squirrel is left of you
			return XY.getVector(6);
		}
		if(deltaX > 0 && deltaY > 0) {	//Squirrel is up and right
			return XY.getVector(7);
		}
		if(deltaX < 0 && deltaY > 0) {	//Squirrel is up and left
			return XY.getVector(9);
		}
		if(deltaX > 0 && deltaY < 0) {	//Squirrel is down and right
			return XY.getVector(1);
		}
		if(deltaX < 0 && deltaY < 0) {	//Squirrel is down and left
			return XY.getVector(3);
		}
		return XY.getVector(XY.randomNumber());
	}
	
	public XY miniSquirrelSpawnable(XY pos) {
		XY location = null;
		if((location = cells.getEmptyLocationAround(pos)) == null) {
			return null;
		}
		return location;
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