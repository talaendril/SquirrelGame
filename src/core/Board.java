package core;

import java.util.ArrayList;
import java.util.Random;

import entities.*;
import entities.Character;
import exceptions.EntityNotFoundException;
import exceptions.NotEnoughEnergyException;
import idmanager.ID;
import location.XY;
import ui.commandhandle.MoveCommand;

public class Board {
	
	private final int boardSizeX;
	private final int boardSizeY;
	private EntitySet es;
	private BoardConfig config;
	
	private ArrayList<MasterSquirrel> masters = new ArrayList<>();
	
	public Board(BoardConfig config) {
		this.config = config;
		this.es = new EntitySet(config.getTotalEntities());
		this.boardSizeX = config.getSize().getX();
		this.boardSizeY = config.getSize().getY();
		
		generateEntities();
	}
	
	public int getBoardSizeX() {
		return this.boardSizeX;
	}
	
	public int getBoardSizeY() {
		return this.boardSizeY;
	}
	
	public EntitySet getEntitySet() {
		return this.es;
	}
	
	public void generateEntities() {
		for(int i = 0; i < this.boardSizeY; i++) {
			for(int j = 0; j < this.boardSizeX; j++) {
				if((i == 0) || (j == 0) || (i == this.boardSizeY - 1) || (j == this.boardSizeX - 1)) {
					es.addEntity(new Wall(ID.getNewID(), new XY(j, i)));
				}
			}
		}
		for(int badBeastsCount = 0; badBeastsCount < this.config.getNumberOfBadBeasts(); badBeastsCount++) {
			es.addEntity(new BadBeast(ID.getNewID(), getEmptyLocation()));
		}
		for(int goodBeastsCount = 0; goodBeastsCount < this.config.getNumberOfGoodBeasts(); goodBeastsCount++) {
			es.addEntity(new GoodBeast(ID.getNewID(), getEmptyLocation()));
		}
		for(int badPlantsCount = 0; badPlantsCount < this.config.getNumberOfBadPlants(); badPlantsCount++) {
			es.addEntity(new BadPlant(ID.getNewID(), getEmptyLocation()));
		}
		for(int goodPlantsCount = 0; goodPlantsCount < this.config.getNumberOfGoodPlants(); goodPlantsCount++) {
			es.addEntity(new GoodPlant(ID.getNewID(), getEmptyLocation()));
		}
		for(int wallCount = 0; wallCount < this.config.getNumberOfRandomWalls(); wallCount++) {
			es.addEntity(new Wall(ID.getNewID(), getEmptyLocation()));
		}
	}
	
	public void generateMasterSquirrels(MasterSquirrel[] masters) {
		for(MasterSquirrel ms : masters) {
			ms.setLocation(getEmptyLocation());
			es.addEntity(ms);
			this.masters.add(ms);
		}
	}
	
	public void spawnMiniSquirrel(int energy) throws NotEnoughEnergyException {
		MasterSquirrel ms = this.masters.get(0);
		if(ms == null) {
			throw new EntityNotFoundException("No MasterSquirrel in the EntitySet");	//TODO think about how to handle this situation
		} else if (ms.getEnergy() < energy ){
			throw new NotEnoughEnergyException();
		} else {
			XY location = this.getEntitySet().getEmptyLocationAround(ms.getLocation());
			if(location == null) {
				return;
			} else {
				MiniSquirrel newMS = ms.spawnMiniSquirrel(location, energy);
				ms.updateEnergy(-energy);
				this.getEntitySet().addEntity(newMS);
			}
		}
	}
	
	public XY getEmptyLocation() {
		while (true) {
			int randomX = new Random().nextInt(this.boardSizeX - 1);
			int randomY = new Random().nextInt(this.boardSizeY - 1);
			XY check = new XY(randomX, randomY);
			if (es.getEntity(check) == null) {
				return check;
			} 
		}
	}
	
	public FlattenedBoard flatten() {
		return new FlattenedBoard(this);
	}
	
	public void nextStep(MoveCommand command) {
		for(Entity e : es.getEntities()) {
			if(e != null && e instanceof Character) {
				if (e instanceof HandOperatedMasterSquirrel) {
					((HandOperatedMasterSquirrel) e).nextStep(this.flatten(), command);
					continue;
				}
				e.nextStep(this.flatten(), MoveCommand.getRandomCommand());
			}
		}
	}
	
	@Override
	public String toString() {
		return this.es.toString();
	}
}