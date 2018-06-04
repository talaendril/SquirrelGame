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
		for (Entity e : this.board.getEntitySet()) {
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
		return this.board.getEntity(new XY(x, y));
	}

	@Override
	public Entity getEntity(XY xy) {
		return this.board.getEntity(xy);
	}

	@Override
	public EntityType getEntityType(int x, int y) {
		return EntityType.getEntityType(this.board.getEntity(new XY(x, y)));
	}

	@Override
	public EntityType getEntityType(XY xy) {
		return EntityType.getEntityType(this.board.getEntity(xy));
	}

	@Override
	public XY getSize() {
		return new XY(board.getBoardSizeX(), board.getBoardSizeY());
	}

    /*
    checks collision for MiniSquirrel
     */
	@Override
	public void tryMove(MiniSquirrel ms, XY direction) {
		Entity e = this.board.getEntity(ms.getLocation().plus(direction));
		LOGGER.info("MiniSquirrel" + ms.getID() + " tries to move from " + ms.getLocation().toString()
				+ " in a direction of " + direction.toString());
		if (e != null) {
			if (e instanceof Character) {
				if (EntityType.getEntityType(e) == EntityType.MASTER_SQUIRREL) {
					if (e.equals(ms.getMaster())) {
						e.updateEnergy(ms.getEnergy());
					} else {
						e.updateEnergy(MiniSquirrel.ENERGY_GAIN_NOT_MASTER);
					}
					this.kill(ms);
				} else if (EntityType.getEntityType(e) == EntityType.MINI_SQUIRREL) {
					this.kill(e);
					this.kill(ms);
				} else if (EntityType.getEntityType(e) == EntityType.BAD_BEAST) {
					if (ms.getEnergy() <= Math.abs(e.getEnergy())) {
						this.kill(ms);
					} else {
						ms.updateEnergy(e.getEnergy());
					}
					if (((BadBeast) e).getBiteCounter() == BadBeast.MAXIMUM_BITECOUNT) {
						this.killAndReplace(e);
						ms.move(direction);
					}
					((BadBeast) e).incrementBiteCounter();
				} else if (EntityType.getEntityType(e) == EntityType.GOOD_BEAST) {
					ms.updateEnergy(e.getEnergy());
					this.killAndReplace(e);
					ms.move(direction);
				}
			} else {
			    if(EntityType.getEntityType(e) == EntityType.GOOD_PLANT) {
			        this.killAndReplace(e);
			        ms.updateEnergy(e.getEnergy());
			        ms.move(direction);
                }
                if(EntityType.getEntityType(e) == EntityType.BAD_PLANT) {
			        if(ms.getEnergy() <= Math.abs(e.getEnergy())) {
			            this.kill(ms);
                    } else {
			            ms.updateEnergy(e.getEnergy());
			            this.killAndReplace(e);
			            ms.move(direction);
                    }
                }
				if (EntityType.getEntityType(e) == EntityType.WALL) {
			        if(ms.getEnergy() <= Math.abs(e.getEnergy())) {
			            this.kill(ms);
                    } else {
			            ms.updateEnergy(e.getEnergy());
			            ms.setStunned();
                    }
				}
                if(EntityType.getEntityType(e) == EntityType.NONE) {
                    ms.move(direction);
                }
			}
		} else {
			ms.move(direction);
		}
	}

    /*
    checks collision for GoodBeast
     */
	@Override
	public void tryMove(GoodBeast gb, XY direction) {
		XY vector = direction;
        //TODO ADD THIS LINE AFTER DONE TESTING
        /*
		Entity squirrel;
		if ((squirrel = this.nearestPlayerEntity(gb.getLocation())) != null) {
			vector = this.bestVectorAwayFromEntity(gb, squirrel);
		}
		*/
		if (gb.getStepCount() == GoodBeast.MAXIMUM_STEPCOUNT) {
			gb.resetStepCount();
			LOGGER.info("GoodBeast" + gb.getID() + " tries to move from " + gb.getLocation().toString()
					+ " in a direction of " + vector.toString());
			Entity e = this.board.getEntity(gb.getLocation().plus(vector));
			if (e != null) {
				if (e instanceof Squirrel) {
					e.updateEnergy(gb.getEnergy());
					this.killAndReplace(gb);
				} else {
					// do nothing
				}
			} else {
				gb.move(vector);
			}
		}
		gb.incrementStepCount();
	}

    /*
    checks collision for BadBeast
     */
	@Override
	public void tryMove(BadBeast bb, XY direction) {
		XY vector = direction;
        //TODO ADD THIS LINE AFTER DONE TESTING
        /*
		Entity squirrel;
		if ((squirrel = this.nearestPlayerEntity(bb.getLocation())) != null) {
			vector = this.bestVectorToEntity(bb, squirrel);
		}
		*/
		if (bb.getStepCount() == BadBeast.MAXIMUM_STEPCOUNT) {
		    bb.resetStepCount();
			LOGGER.info("BadBeast" + bb.getID() + " tries to move from " + bb.getLocation().toString()
					+ " in a direction of " + vector.toString());
			Entity e = this.board.getEntity(bb.getLocation().plus(vector));
			if (e != null) {
				if (e instanceof Squirrel) {
					if (EntityType.getEntityType(e) == EntityType.MINI_SQUIRREL) {
						if (e.getEnergy() < Math.abs(bb.getEnergy())) {
							this.kill(e);
							bb.move(vector);
						} else {
							e.updateEnergy(bb.getEnergy());
						}
					} else if (EntityType.getEntityType(e) == EntityType.MASTER_SQUIRREL) {
						e.updateEnergy(bb.getEnergy());
					}
					if (bb.getBiteCounter() == BadBeast.MAXIMUM_BITECOUNT) {
						this.killAndReplace(bb);
					} else {
						// do nothing
					}
                    bb.incrementBiteCounter();
				} else {
					// do nothing
				}
			} else {
				bb.move(vector);
			}
		}
		bb.incrementStepCount();
	}

	/*
	checks collision for MasterSquirrel
	 */
	@Override
	public void tryMove(MasterSquirrel master, XY direction) {
		if (direction == XY.ZERO_ZERO) {
			return;
		}
		Entity e = this.board.getEntity(master.getLocation().plus(direction));
		if (e != null) {
			if (e instanceof Character) {
				if (EntityType.getEntityType(e) == EntityType.MASTER_SQUIRREL) {
					// do nothing
				} else if (EntityType.getEntityType(e) == EntityType.MINI_SQUIRREL) {
					if (master.checkEntityInProduction(e)) {
						master.updateEnergy(e.getEnergy());
					} else {
						master.updateEnergy(MiniSquirrel.ENERGY_GAIN_NOT_MASTER);
					}
					this.kill(e);
					master.move(direction);
				} else {
					if (EntityType.getEntityType(e) == EntityType.BAD_BEAST) {
						if (((BadBeast) e).getBiteCounter() == BadBeast.MAXIMUM_BITECOUNT) {
							this.killAndReplace(e);
							master.move(direction);
						}
                        ((BadBeast) e).incrementBiteCounter();
					} else {
						this.killAndReplace(e);
						master.move(direction);
					}
					master.updateEnergy(e.getEnergy());
				}
			} else {
				if (EntityType.getEntityType(e) == EntityType.WALL) {
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
		for (int i = pos.y - 6; i < pos.y + 6; i++) {
			for (int j = pos.x - 6; j < pos.x + 6; j++) {
				Entity e = this.board.getEntity(new XY(j, i));
				if (e instanceof Squirrel) {
					array[arrayPosition++] = (Squirrel) e;
				}
			}
		}
		if (array[0] == null) {
			return null;
		}
		Squirrel nearest = null;
		double shortestDistance = entityMatrix.length * entityMatrix.length; // initialized with something big
		double newDistance;
        for (Squirrel anArray : array) {
            if (anArray != null) {
                if ((newDistance = anArray.getLocation().distanceFrom(pos)) < shortestDistance) {
                    shortestDistance = newDistance;
                    nearest = anArray;
                }
            }
        }
		return nearest;
	}

	@Override
	public void addMiniSquirrel(MiniSquirrel ms) {
		this.board.addEntity(ms);
	}

	private XY bestVectorToEntity(Entity beast, Entity target) {
		return XYSupport.getVectorBetween(beast.getLocation(), target.getLocation());
	}

	private XY bestVectorAwayFromEntity(Entity beast, Entity hunter) {
		XY vectorToEntity = this.bestVectorToEntity(beast, hunter);
		return vectorToEntity.times(-1);
	}

	@Override
	public void implode(MiniSquirrel ms, int impactRadius) {
		XY msLoc = ms.getLocation();
		double accumulatedEnergy = 0;
		for (int i = msLoc.y - impactRadius; i <= msLoc.y + impactRadius; i++) {
			for (int j = msLoc.x - impactRadius; j <= msLoc.x + impactRadius; j++) {
				Entity entity = this.board.getEntity(new XY(j, i));
				if (entity == null || entity.equals(ms)) {
					continue;
				}
				double distance = ms.getLocation().distanceFrom(entity.getLocation());
				if (distance < impactRadius) {
					double impactArea = impactRadius * impactRadius * Math.PI;
					double energyLoss = 200 * (ms.getEnergy() / impactArea) * (1 - distance / impactRadius);
					accumulatedEnergy += this.updateEntityAfterImplosion(ms, entity, energyLoss);
					System.out.println(entity.getClass() + " ACCUMULATED ENERGY " + accumulatedEnergy);
				}
			}
		}
		this.kill(ms);
		accumulatedEnergy += ms.getEnergy();
		LOGGER.info("MasterSquirrel" + ms.getMaster().getID() + " received " + accumulatedEnergy
				+ " Energy from that implosion");
		ms.getMaster().updateEnergy(((int) accumulatedEnergy));
	}

	@Override
	public int remainingSteps() {
		return this.board.getRemainingSteps();
	}

	/*
	updates an Entity after it was hit by an Implosion
	 */
	private double updateEntityAfterImplosion(MiniSquirrel ms, Entity entity, double energyLoss) {
		if (entity == null)
			return 0;
		int delta = (int) -energyLoss;
		EntityType type = EntityType.getEntityType(entity);
		switch (type) {
		case MASTER_SQUIRREL:
			MasterSquirrel master = ms.getMaster();
			if (entity.equals(master)) {
				return 0;
			}
			entity.updateEnergy(delta);
			return energyLoss;
		case MINI_SQUIRREL:
			master = ms.getMaster();
			if (master.checkEntityInProduction(entity)) {
				return 0;
			}
			if (energyLoss > entity.getEnergy()) {
				this.kill(entity);
				return entity.getEnergy();
			}
			entity.updateEnergy(delta);
			return energyLoss;
		case GOOD_BEAST:
		case GOOD_PLANT:
			if (energyLoss > entity.getEnergy()) {
				this.killAndReplace(entity);
				return entity.getEnergy();
			}
			entity.updateEnergy(delta);
			return energyLoss;
		case BAD_BEAST:
		case BAD_PLANT:
			if (energyLoss > Math.abs(entity.getEnergy())) {
				this.killAndReplace(entity);
			} else {
				entity.updateEnergy(-delta);
			}
			return 0;
		case WALL:
		default:
			return 0;
		}
	}

	/*
	removes entity from collection and in case of MiniSquirrel removes it from Production of Master
	 */
	@Override
	public void kill(Entity entity) {
		if (EntityType.getEntityType(entity) == EntityType.MINI_SQUIRREL) {
			((MiniSquirrel) entity).getMaster().removeFromProduction(entity);
		}
		this.board.removeEntity(entity);
		//entityMatrix[entity.getLocation().y][entity.getLocation().x] = null;
	}

	/*
	kills the entity, resets its energy, gives it a new position and then puts it back into the collection
	 */
	@Override
	public void killAndReplace(Entity entity) {
		this.kill(entity);
		XY newLocation;
		do {
			newLocation = XYSupport.getRandomLocationBetween(board.getBoardSizeX() - 1, board.getBoardSizeY() - 1);
		} while (this.board.getEntity(newLocation) != null);
		entity.resetEnergy();
		entity.setLocation(newLocation);
		this.board.addEntity(entity);
		//entityMatrix[entity.getLocation().y][entity.getLocation().x] = entity;
        /* TODO ADD THAT LINE AGAIN SO GAME WORKS
        THIS IS GETTING CALLED BUT FOR SOME REASON THE LENGTH OF THIS MATRIX
        IS ALWAYS 0 SO REMOVING THAT FOR TESTING PURPOSES
         */
	}
}