package core;

import entities.BadBeast;
import entities.Entity;
import entities.GoodBeast;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import entities.Squirrel;
import entities.Character;
import location.XY;
import ui.commandhandle.MoveCommand;

public class FlattenedBoard implements EntityContext, BoardView {
	
	private Board board;
	private Entity[][] entityMatrix;

	public FlattenedBoard(Board b) {
		this.board = b;
		entityMatrix = new Entity[this.board.getBoardSizeY()][this.board.getBoardSizeX()];
		
		for(Entity e : this.board.getEntitySet().getEntities()) {
			if (e != null) {
				entityMatrix[e.getLocation().getY()][e.getLocation().getX()] = e;
			}
		}
	}
	
	@Override
	public Entity[][] getEntityMatrix() {
		return entityMatrix;
	}

	@Override
	public EntityType getEntityType(int x, int y) {
		return EntityType.getEntityType(this.board.getEntitySet().getEntity(new XY(x, y)));
	}
	
	@Override
	public EntityType getEntityType(XY xy) {
		return EntityType.getEntityType(this.board.getEntitySet().getEntity(xy));
	}

	@Override
	public XY getSize() {
		return new XY(board.getBoardSizeX(), board.getBoardSizeY());
	}

	@Override
	public void tryMove(MiniSquirrel ms, XY direction) {
		ms.updateEnergy(-1);
		Entity e = this.board.getEntitySet().getEntity(new XY(ms.getLocation(), direction));
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
					if(ms.getEnergy() < Math.abs(e.getEnergy())) {
						this.kill(ms);
					} else {
						ms.updateEnergy(e.getEnergy());
					}
					if (((BadBeast) e).getBiteCounterAndIncrement() == BadBeast.MAXIMUM_BITECOUNT) {
						this.killAndReplace(e);
						ms.move(direction);
					}
				} else if (EntityType.getEntityType(e) == EntityType.GOODBEAST) {
					ms.updateEnergy(e.getEnergy());
					this.killAndReplace(e);
				} 
			} else {
				if(ms.getEnergy() < Math.abs(e.getEnergy())) {
					this.kill(ms);
				} else {
					ms.updateEnergy(e.getEnergy());
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
		if (gb.getStepCounterAndIncrement() == GoodBeast.MAXIMUM_STEPCOUNT) {
			System.out.println("GoodBeast" + gb.getID() + " tries to move from " + gb.getLocation().toString() + " in a direction of " + vector.toString());
			Entity e = this.board.getEntitySet().getEntity(new XY(gb.getLocation(), vector));
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
		if (bb.getStepCounterAndIncrement() == BadBeast.MAXIMUM_STEPCOUNT) {
			System.out.println("BadBeast" + bb.getID() + " tries to move from " + bb.getLocation().toString() + " in a direction of " + vector.toString());
			Entity e = this.board.getEntitySet().getEntity(new XY(bb.getLocation(), vector));
			if (e != null) {
				if (e instanceof Squirrel) {
					if(EntityType.getEntityType(e) == EntityType.MINISQUIRREL) {
						if(e.getEnergy() < Math.abs(bb.getEnergy())) {
							this.kill(e);
							bb.move(vector);
						} else {
							e.updateEnergy(bb.getEnergy());
						}
					} else if(EntityType.getEntityType(e) == EntityType.MASTERSQUIRREL || EntityType.getEntityType(e) == EntityType.HANDOPERATEDMASTERSQUIRREL) {
						e.updateEnergy(bb.getEnergy());
					}
					if (bb.getBiteCounterAndIncrement() == BadBeast.MAXIMUM_BITECOUNT) {
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
		if(direction == XY.ORIGIN) {
			return;
		}
		Entity e = this.board.getEntitySet().getEntity(new XY(master.getLocation(), direction));
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
					master.move(direction);
				} else {
					if(EntityType.getEntityType(e) == EntityType.BADBEAST) {
						if(((BadBeast) e).getBiteCounterAndIncrement() == BadBeast.MAXIMUM_BITECOUNT) {
							this.killAndReplace(e);
							master.move(direction);
						}
					} else {
						this.killAndReplace(e);
						master.move(direction);
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
	}
	
	public Squirrel nearestPlayerEntity(XY pos) {
		Squirrel[] array = new Squirrel[36];
		int arrayPosition = 0;
		for(int i = pos.getY() - 6; i < pos.getY() + 6; i++) {
			for(int j = pos.getX() - 6; j < pos.getX() + 6; j++) {
				Entity e = this.board.getEntitySet().getEntity(new XY(j, i));
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
	
	public XY bestVectorToEntity(Entity beast, Entity target) {	
		int deltaX = target.getLocation().getX() - beast.getLocation().getX();
		int deltaY = target.getLocation().getY() - beast.getLocation().getY();
		if(deltaX == 0 && deltaY > 0) {	//Squirrel is above you
			return XY.getVector(MoveCommand.DOWN);
		}
		if(deltaX == 0 && deltaY < 0) {	//Squirrel is below you
			return XY.getVector(MoveCommand.UP);
		}
		if(deltaX > 0 && deltaY == 0) {	//Squirrel is right of you
			return XY.getVector(MoveCommand.RIGHT);
		}
		if(deltaX < 0 && deltaY == 0) {	//Squirrel is left of you
			return XY.getVector(MoveCommand.LEFT);
		}
		if(deltaX > 0 && deltaY > 0) {	//Squirrel is up and right
			return XY.getVector(MoveCommand.DOWN_RIGHT);
		}
		if(deltaX < 0 && deltaY > 0) {	//Squirrel is up and left
			return XY.getVector(MoveCommand.DOWN_LEFT);
		}
		if(deltaX > 0 && deltaY < 0) {	//Squirrel is down and right
			return XY.getVector(MoveCommand.UP_RIGHT);
		}
		if(deltaX < 0 && deltaY < 0) {	//Squirrel is down and left
			return XY.getVector(MoveCommand.UP_LEFT);
		}
		return XY.getVector(MoveCommand.getRandomCommand());
	}
	
	public XY bestVectorAwayFromEntity(Entity beast, Entity hunter) {
		XY vectorToEntity = this.bestVectorToEntity(beast, hunter);
		return XY.invertVector(vectorToEntity);
	}

	@Override
	public void kill(Entity entity) {
		this.board.getEntitySet().removeEntity(entity);
		entityMatrix[entity.getLocation().getY()][entity.getLocation().getX()] = null;
	}

	@Override
	public void killAndReplace(Entity entity) {
		this.kill(entity);
		XY newLocation;
		do {
			newLocation = XY.getRandomLocationBetween(board.getBoardSizeX() - 1, board.getBoardSizeY() - 1);
		} while(this.board.getEntitySet().getEntity(newLocation) != null);
		entity.setLocation(newLocation);
		this.board.getEntitySet().addEntity(entity);
		entityMatrix[entity.getLocation().getY()][entity.getLocation().getX()] = entity;
	}
}