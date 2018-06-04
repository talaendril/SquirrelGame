package ui;

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
import ui.commandhandle.Command;
import ui.commandhandle.CommandScanner;

@Deprecated
public class ConsoleUI implements UI{
	
	private CommandScanner scanner;
	
	public ConsoleUI(CommandScanner cscanner) {
		this.scanner = cscanner;
	}

	@Override
	public void render(BoardView view) {
		Entity[][] entityMatrix = view.getEntityMatrix();
		for (Entity[] anEntityMatrix : entityMatrix) {
			for (int j = 0; j < entityMatrix[0].length; j++) {
				if (anEntityMatrix[j] instanceof Wall) {
					System.out.print("W\t");
				} else if (anEntityMatrix[j] instanceof BadBeast) {
					System.out.print("BB\t");
				} else if (anEntityMatrix[j] instanceof GoodBeast) {
					System.out.print("GB\t");
				} else if (anEntityMatrix[j] instanceof BadPlant) {
					System.out.print("BP\t");
				} else if (anEntityMatrix[j] instanceof GoodPlant) {
					System.out.print("GP\t");
				} else if (anEntityMatrix[j] instanceof HandOperatedMasterSquirrel) {
					HandOperatedMasterSquirrel homs = (HandOperatedMasterSquirrel) anEntityMatrix[j];
					System.out.print("OS" + homs.getID() + "\t");
				} else if (anEntityMatrix[j] instanceof MasterSquirrel) {
					MasterSquirrel ms = (MasterSquirrel) anEntityMatrix[j];
					System.out.print("S" + ms.getID() + "\t");
				} else if (anEntityMatrix[j] instanceof MiniSquirrel) {
					MiniSquirrel ms = (MiniSquirrel) anEntityMatrix[j];
					System.out.print("s" + ms.getMaster().getID() + "\t");
				} else {
					System.out.print(".\t");
				}
			}
			System.out.println();
		}
	}
	
	@Override
	public Command getCommand() {
		return this.scanner.next();
	}

	@Override
	public void message(String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void remainingSteps(int steps) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean checkResetCalled() {
		return false;
	}

	@Override
	public void changeResetCalled(boolean bool) {
		// TODO Auto-generated method stub
	}
}
