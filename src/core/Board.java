package core;

import java.util.Random;

import entities.*;
import entities.Character;
import idmanager.ID;
import location.XY;
import ui.MoveCommand;

public class Board {
	
	private final int boardSizeX;
	private final int boardSizeY;
	private EntitySet es;
	private BoardConfig config;

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
	
	public MasterSquirrel getMaster() {
		return this.es.getMaster();
	}
	
	public void generateEntities() {
		for(int i = 0; i < this.boardSizeY; i++) {
			for(int j = 0; j < this.boardSizeX; j++) {
				if((i == 0) || (j == 0) || (i == this.boardSizeY - 1) || (j == this.boardSizeX - 1)) {
					es.addEntity(new Wall(ID.getNewID(), new XY(j, i)));
				}
			}
		}
		for(int homsCount = 0; homsCount < this.config.getNumberOfHandOperatedMasterSquirrels(); homsCount++) {
			es.addEntity(new HandOperatedMasterSquirrel(ID.getNewID(), getEmptyLocation()));
		}
		for(int masterSquirrelsCount = 0; masterSquirrelsCount < this.config.getNumberOfMasterSquirrels() - this.config.getNumberOfHandOperatedMasterSquirrels(); masterSquirrelsCount++) {
			es.addEntity(new MasterSquirrel(ID.getNewID(), getEmptyLocation()));
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