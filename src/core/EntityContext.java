package core;

import entities.BadBeast;
import entities.Entity;
import entities.GoodBeast;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import entities.Squirrel;
import location.XY;

public interface EntityContext {

    /**
     *
     * @return size of the Board
     */
	XY getSize();

    /**
     *
     * @param ms : MiniSquirrel that tries to move
     * @param direction : direction the MiniSquirrel will take
     */
	void tryMove(MiniSquirrel ms, XY direction);

    /**
     *
     * @param gb : GoodBeast that tries to move
     * @param direction : direction the GoodBeast will take
     */
	void tryMove(GoodBeast gb, XY direction);

    /**
     *
     * @param bb : BadBeast that tries to move
     * @param direction : direction the BadBeast will take
     */
	void tryMove(BadBeast bb, XY direction);

    /**
     *
     * @param master : MasterSquirrel that tries to move
     * @param direction : direction the MasterSquirrel will take
     */
	void tryMove(MasterSquirrel master, XY direction);

    /**
     *
     * @param pos : position around which you want to find the Squirrel
     * @return closest Squirrel to pos
     */
	Squirrel nearestPlayerEntity(XY pos);

    /**
     *
     * @param ms : MiniSquirrel that needs to be added to the Board
     */
	void addMiniSquirrel(MiniSquirrel ms);

    /**
     *
     * @param entity : Entity that needs to be removed from the Board
     */
	void kill(Entity entity);

    /**
     * First removes the Entity and then spawns it at a random location again
     * @param entity : Entity that gets removed from the Board
     */
	void killAndReplace(Entity entity);

    /**
     *
     * @param xy : location you want to check
     * @return EntityType of the Entity at xy, NONE if none was found
     */
	EntityType getEntityType(XY xy);

    /**
     *
     * @param xy : location you want to check
     * @return Entity at xy, null if none was found
     */
	Entity getEntity(XY xy);

    /**
     *
     * @param ms : miniSquirrel that wants to implode
     * @param impactRadius : radius in which entities will get hit by the implosion
     */
	void implode(MiniSquirrel ms, int impactRadius);

    /**
     *
     * @return remaining steps of the game
     */
	int remainingSteps();
}
