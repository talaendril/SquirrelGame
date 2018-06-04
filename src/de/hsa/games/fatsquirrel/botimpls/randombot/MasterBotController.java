package de.hsa.games.fatsquirrel.botimpls.randombot;

import botapi.BotController;
import botapi.ControllerContext;
import location.XYSupport;
import ui.commandhandle.MoveCommand;

public class MasterBotController implements BotController {

	@Override
	public void nextStep(ControllerContext view) {
		view.move(XYSupport.getVector(MoveCommand.getRandomCommand()));
		view.spawnMiniBot(XYSupport.getVector(MoveCommand.getRandomCommand()), 100);
	}
}