package botimpl;

import botapi.BotController;
import botapi.ControllerContext;
import location.XY;
import ui.commandhandle.MoveCommand;

public class MiniBotController implements BotController {

	@Override
	public void nextStep(ControllerContext view) {
		view.move(XY.getVector(MoveCommand.getRandomCommand()));
	}
}