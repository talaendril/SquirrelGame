package ui;

import java.io.IOException;

import core.BoardView;
import entities.BadBeast;
import entities.BadPlant;
import entities.Entity;
import entities.GoodBeast;
import entities.GoodPlant;
import entities.HandOperatedMasterSquirrel;
import entities.MasterSquirrel;
import entities.MiniSquirrel;
import entities.Wall;

public class ConsoleUI implements UI {
	
	public ConsoleUI() {
		
	}

	@Override
	public void render(BoardView view) {
		Entity[][] entityMatrix = view.getEntityMatrix();
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
	
	@Override
	public MoveCommand getCommand() {
		System.out.println("Gib eine Zahl zwischen [1-9] (5 bedeutet:\"do nothing\"):");
		int number = 0;
		try {
			number = System.in.read();
			while (number < '0' || number > '9') {
				number = System.in.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		number -= '0';
		switch(number) {
		case 1:
			return MoveCommand.DOWN_LEFT;
		case 2:
			return MoveCommand.DOWN;
		case 3:
			return MoveCommand.DOWN_RIGHT;
		case 4:
			return MoveCommand.LEFT;
		case 6:
			return MoveCommand.RIGHT;
		case 7:
			return MoveCommand.UP_LEFT;
		case 8:
			return MoveCommand.UP;
		case 9:
			return MoveCommand.UP_RIGHT;
		default:
			return MoveCommand.NONE;
		}
	}
}
