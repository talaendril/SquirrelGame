package core;

import java.util.Random;

import entities.*;
import idmanager.ID;
import location.XY;
import ui.MoveCommand;

public class Board {
	
	private final int boardSizeX;
	private final int boardSizeY;
	private EntitySet es = new EntitySet(BoardConfig.totalEntities);

	public Board() {
		this.boardSizeX = BoardConfig.size.getX();
		this.boardSizeY = BoardConfig.size.getY();
		
		generateEntities();
	}
	
	public int getBoardSizeX() {
		return this.boardSizeX;
	}
	
	public int getBoardSizeY() {
		return this.boardSizeY;
	}
	
	public void generateEntities() {
		for(int i = 0; i < boardSizeY; i++) {
			for(int j = 0; j < boardSizeX; j++) {
				if((i == 0) || (j == 0) || (i == boardSizeY - 1) || (j == boardSizeX - 1)) {
					es.addEntity(new Wall(ID.getNewID(), new XY(j, i)));
				}
			}
		}
		for(int homsCount = 0; homsCount < BoardConfig.numberOfHandOperatedMasterSquirrels; homsCount++) {
			es.addEntity(new HandOperatedMasterSquirrel(ID.getNewID(), getEmptyLocation()));
		}
		for(int masterSquirrelsCount = 0; masterSquirrelsCount < BoardConfig.numberOfMasterSquirrels; masterSquirrelsCount++) {
			es.addEntity(new MasterSquirrel(ID.getNewID(), getEmptyLocation()));
		}
		for(int badBeastsCount = 0; badBeastsCount < BoardConfig.numberOfBadBeasts; badBeastsCount++) {
			es.addEntity(new BadBeast(ID.getNewID(), getEmptyLocation()));
		}
		for(int goodBeastsCount = 0; goodBeastsCount < BoardConfig.numberOfGoodBeasts; goodBeastsCount++) {
			es.addEntity(new GoodBeast(ID.getNewID(), getEmptyLocation()));
		}
		for(int badPlantsCount = 0; badPlantsCount < BoardConfig.numberOfBadPlants; badPlantsCount++) {
			es.addEntity(new BadPlant(ID.getNewID(), getEmptyLocation()));
		}
		for(int goodPlantsCount = 0; goodPlantsCount < BoardConfig.numberOfGoodPlants; goodPlantsCount++) {
			es.addEntity(new GoodPlant(ID.getNewID(), getEmptyLocation()));
		}
		for(int wallCount = 0; wallCount < BoardConfig.numberOfRandomWalls; wallCount++) {
			es.addEntity(new Wall(ID.getNewID(), getEmptyLocation()));
		}
	}
	
	public XY getEmptyLocation() {
		while (true) {
			int randomX = new Random().nextInt(boardSizeX - 1);
			int randomY = new Random().nextInt(boardSizeY - 1);
			XY check = new XY(randomX, randomY);
			if (es.getEntity(check) == null) {
				return check;
			} 
		}
	}
	
	public FlattenedBoard flatten() {
		return new FlattenedBoard(es, this);
	}
	
	public void callNextStep() {
		this.nextStep();
	}
	
	/*start of overload	this is just so a MoveCommand can be given from Game to MasterSquirrel
	public void callNextStep(EntityType type, MoveCommand command) {
		this.nextStep(type, command);
	}
	
	public void nextStep(EntityType type, MoveCommand command) {
		for(Entity e : es.getEntities()) {
			if(e != null) {
				if(EntityType.getEntityType(e) == type) {
					((HandOperatedMasterSquirrel) e).nextStep(this.flatten(), command);
				} else {
					e.nextStep(this.flatten());
				}
			}
		}
	}
	end of overload*/
	
	public void nextStep() {
		for(Entity e : es.getEntities()) {
			if(e != null) {
				e.nextStep(this.flatten());
			}
		}
	}
	
	@Override
	public String toString() {
		return es.toString();
	}
}