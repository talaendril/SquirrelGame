package de.hsa.games.fatsquirrel.botimpls.randombot;

import botapi.BotController;
import botapi.ControllerContext;
import location.XYSupport;
import ui.commandhandle.MoveCommand;

public class MiniBotController implements BotController {
	
	private int implodeSteps = 3;
	private int implodeRadius = 3;

	@Override
	public void nextStep(ControllerContext view) {
		view.move(XYSupport.getVector(MoveCommand.getRandomCommand()));
		if(implodeSteps == 0) {
			view.implode(this.implodeRadius);
		} else {
			implodeSteps--;
		}
	}
}