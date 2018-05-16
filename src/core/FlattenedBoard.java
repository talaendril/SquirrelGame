package core;

import java.util.logging.Logger;

import entities.BadBeast;
import entities.Entity;
import entities.GoodBeast;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import entities.Squirrel;
import entities.Character;
import location.XY;
import location.XYSupport;

public class FlattenedBoard implements EntityContext, BoardView {
	
	private static final Logger LOGGER = Logger.getLogger(FlattenedBoard.class.getName());
	
	private Board board;
	private Entity[][] entityMatrix;

	public FlattenedBoard(Board b) {
		this.board = b;
		entityMatrix = new Entity[this.board.getBoardSizeY()][this.board.getBoardSizeX()];
		
		for(Entity e : this.board.getEntitySet().getEntities()) {
			if (e != null) {
				entityMatrix[e.getLocation().y][e.getLocation().x] = e;
			}
		}
	}
	
	@Override
	public Entity[][] getEntityMatrix() {
		return entityMatrix;
	}
	
	@Override
	public Entity getEntity(int x, int y) {
		return this.board.getEntitySet().getEntity(new XY(x, y));
	}
	
	@Override
	public Entity getEntity(XY xy) {
		return this.board.getEntitySet().getEntity(xy);
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
		Entity e = this.board.getEntitySet().getEntity(ms.getLocation().plus(direction));
		LOGGER.info("MiniSquirrel" + ms.getID() + " tries to move from " + ms.getLocation().toString() + " in a direction of " + direction.toString());
		if (e != null) {
			if (e instanceof Character) {
				if (EntityType.getEntityType(e) == EntityType.MASTER_SQUIRREL) {
					if (e.equals(ms.getMaster())) {
						ms.getMaster().updateEnergy(ms.getEnergy());
					} else {
						e.updateEnergy(MiniSquirrel.ENERGY_GAIN_NOT_MASTER);
					}
					this.kill(ms);
				} else if (EntityType.getEntityType(e) == EntityType.MINI_SQUIRREL) {
					this.kill(e);
					this.kill(ms);
				} else if (EntityType.getEntityType(e) == EntityType.BAD_BEAST) {
					if(ms.getEnergy() < Math.abs(e.getEnergy())) {
						this.kill(ms);
					} else {
						ms.updateEnergy(e.getEnergy());
					}
					if (((BadBeast) e).getBiteCounterAndIncrement() == BadBeast.MAXIMUM_BITECOUNT) {
						this.killAndReplace(e);
						ms.move(direction);
					}
				} else if (EntityType.getEntityType(e) == EntityType.GOOD_BEAST) {
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
			LOGGER.info("GoodBeast" + gb.getID() + " tries to move from " + gb.getLocation().toString() + " in a direction of " + vector.toString());
			Entity e = this.board.getEntitySet().getEntity(gb.getLocation().plus(vector));
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
			LOGGER.info("BadBeast" + bb.getID() + " tries to move from " + bb.getLocation().toString() + " in a direction of " + vector.toString());
			Entity e = this.board.getEntitySet().getEntity(bb.getLocation().plus(vector));
			if (e != null) {
				if (e instanceof Squirrel) {
					if(EntityType.getEntityType(e) == EntityType.MINI_SQUIRREL) {
						if(e.getEnergy() < Math.abs(bb.getEnergy())) {
							this.kill(e);
							bb.move(vector);
						} else {
							e.updateEnergy(bb.getEnergy());
						}
					} else if(EntityType.getEntityType(e) == EntityType.MASTER_SQUIRREL) {
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
		if(direction == XY.ZERO_ZERO) {
			return;
		}
		Entity e = this.board.getEntitySet().getEntity(master.getLocation().plus(direction));
		if(e != null) {
			if(e instanceof Character) {
				if(EntityType.getEntityType(e) == EntityType.MASTER_SQUIRREL) {
					//do nothing
				} else if(EntityType.getEntityType(e) == EntityType.MINI_SQUIRREL) {
					if(master.checkEntityInProduction(e)) {
						master.updateEnergy(e.getEnergy()); 
					} else {
						master.updateEnergy(MiniSquirrel.ENERGY_GAIN_NOT_MASTER);
					}
					this.kill(e);
					master.move(direction);
				} else {
					if(EntityType.getEntityType(e) == EntityType.BAD_BEAST) {
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
	
	@Override
	public Squirrel nearestPlayerEntity(XY pos) {
		Squirrel[] array = new Squirrel[36];
		int arrayPosition = 0;
		for(int i = pos.y - 6; i < pos.y + 6; i++) {
			for(int j = pos.x - 6; j < pos.x + 6; j++) {
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
				if((newDistance = array[i].getLocation().distanceFrom(pos)) < shortestDistance) {
					shortestDistance = newDistance;
					nearest = array[i];
				}
			}
		}
		return nearest;
	}
	
	@Override
	public void addMiniSquirrel(MiniSquirrel ms) {
		this.board.getEntitySet().addEntity(ms);
	}
	
	public XY bestVectorToEntity(Entity beast, Entity target) {	
		return XYSupport.getVectorBetween(beast.getLocation(), target.getLocation());
	}
	
	public XY bestVectorAwayFromEntity(Entity beast, Entity hunter) {
		XY vectorToEntity = this.bestVectorToEntity(beast, hunter);
		return vectorToEntity.times(-1);
	}
	
	@Override
	public void implode(MiniSquirrel ms, int impactRadius) {
		XY msLoc = ms.getLocation();
		double accumulatedEnergy = 0;
		for(int i = msLoc.y - impactRadius; i < msLoc.y + impactRadius; i++) {
			for(int j = msLoc.x - impactRadius; j < msLoc.x + impactRadius; j++) {
				Entity entity = this.board.getEntitySet().getEntity(new XY(j, i));
				if(entity == null) {
					continue;
				}
				double distance = ms.getLocation().distanceFrom(entity.getLocation());
				if(distance < impactRadius) {
					double impactArea = impactRadius * impactRadius * Math.PI;
					double energyLoss = 200 * (ms.getEnergy()/impactArea) * (1 - distance/impactRadius);
					accumulatedEnergy += this.updateEntityAfterImplosion(ms, entity, energyLoss);
				}
			}
		}
		this.kill(ms);
		ms.getMaster().updateEnergy((int) accumulatedEnergy); 
	}
	
	public double updateEntityAfterImplosion(MiniSquirrel ms, Entity entity, double energyLoss) {
		if(entity == null) 
			return 0;
		int delta = (int) -energyLoss;
		EntityType type = EntityType.getEntityType(entity);
		switch(type) {
		case MASTER_SQUIRREL:
			MasterSquirrel master = ms.getMaster();
			if(entity.equals(master)) {
				return 0;
			}
			entity.updateEnergy(delta);
			return energyLoss;
		case MINI_SQUIRREL:
			master = ms.getMaster();
			if(master.checkEntityInProduction(entity)) {
				return 0;
			}
			if(energyLoss > entity.getEnergy()) {
				this.kill(entity);
				return entity.getEnergy();
			} else {
				entity.updateEnergy(delta);
				return energyLoss;
			}
		case GOOD_BEAST:
		case GOOD_PLANT:
			if(energyLoss > entity.getEnergy()) {
				this.killAndReplace(entity);
				return entity.getEnergy();
			} else {
				entity.updateEnergy(delta);
				return energyLoss;
			}
		case BAD_BEAST:
		case BAD_PLANT:
			if(energyLoss > Math.abs(entity.getEnergy())) {
				this.killAndReplace(entity);
			} else {
				entity.updateEnergy(-delta);
			}
			return 0;
		case WALL:
			return 0;
		default:
			return 0;
		}
	}

	@Override
	public void kill(Entity entity) {
		this.board.getEntitySet().removeEntity(entity);
		entityMatrix[entity.getLocation().y][entity.getLocation().x] = null;
	}

	@Override
	public void killAndReplace(Entity entity) {
		this.kill(entity);
		XY newLocation;
		do {
			newLocation = XYSupport.getRandomLocationBetween(board.getBoardSizeX() - 1, board.getBoardSizeY() - 1);
		} while(this.board.getEntitySet().getEntity(newLocation) != null);
		entity.resetEnergy();
		entity.setLocation(newLocation);
		this.board.getEntitySet().addEntity(entity);
		entityMatrix[entity.getLocation().y][entity.getLocation().x] = entity;
	}
}