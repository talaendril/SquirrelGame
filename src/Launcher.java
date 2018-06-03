
import java.util.logging.Level;
import java.util.logging.Logger;

import core.Board;
import core.BoardConfig;
import core.Game;
import core.KIGame;
import core.SinglePlayer;
import core.MultiPlayer;
import core.State;
import javafx.application.Application;
import javafx.stage.Stage;
import ui.FxUI;
import ui.UI;

public class Launcher extends Application {
	
	private static final Logger LOGGER = Logger.getLogger(Launcher.class.getName());
	
	private BoardConfig config = new BoardConfig();
	private Board board = new Board(config);

	@Override
	public void start(Stage arg0) {
		LOGGER.log(Level.INFO, "starting new game with JavaFX");
		
		Stage primaryStage = new Stage();
		FxUI fxUI = FxUI.createInstance(config.getSize());

        final Game game = createGame(fxUI);

        if(game == null) {
            LOGGER.severe("The input String doesn't match any game name");
            LOGGER.severe("Stopping game");
            System.exit(0);
        }
         
        primaryStage.setScene(fxUI);
        primaryStage.setTitle("Diligent Squirrel");
        primaryStage.show();

        startGame(game);
	}
	
	private Game createGame(UI ui) {
	    String name = "kigame";

		switch(name.toLowerCase()) {
		case "singleplayer":
			LOGGER.log(Level.INFO, "initialized singleplayer game");
			return new SinglePlayer(new State(board), board, ui);
		case "multiplayer":
			LOGGER.log(Level.INFO, "initialized multiplayer game");
			return new MultiPlayer(new State(board), board, ui);
		case "kigame":
			LOGGER.log(Level.INFO, "initiliazed kigame game");
			return new KIGame(new State(board), board, ui);
		default:
			return null;
		}
	}
	
	private void startGame(Game game) {
		game.run(config.getMaximumSteps());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void stop() {
		LOGGER.log(Level.INFO, "stopped game");
		System.exit(0);
	}
}
