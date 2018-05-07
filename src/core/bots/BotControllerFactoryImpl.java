package core.bots;

import botapi.BotController;
import botapi.BotControllerFactory;

public class BotControllerFactoryImpl implements BotControllerFactory {

	@Override
	public BotController createMasterBotController() {
		return new MasterBotController();
	}

	@Override
	public BotController createMiniBotController() {
		return new MiniBotController();
	}
}
