package de.hsa.games.fatsquirrel.botimpls.randombot;

import botapi.BotController;
import botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botimpls.randombot.MasterBotController;
import de.hsa.games.fatsquirrel.botimpls.randombot.MiniBotController;

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
