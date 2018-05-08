
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

public class Launcher extends Application {
	
	private BoardConfig config = new BoardConfig();
	private Board board = new Board(config);
	private State state = new State(board);

	@Override
	public void start(Stage arg0) throws Exception {
		Stage primaryStage = new Stage();
		FxUI fxUI = FxUI.createInstance(config.getSize());
		
		String name = "kigame";
        final Game game = createGame(name, fxUI);
         
        primaryStage.setScene(fxUI);
        primaryStage.setTitle("Diligent Squirrel");
        primaryStage.show();   
        
        startGame(game);   
	}
	
	private Game createGame(String name, FxUI fxUI) {
		switch(name) {
		case "singleplayer":
			return new SinglePlayer(state, board, fxUI); 
		case "multiplayer":
			return new MultiPlayer(state, board, fxUI);
		case "kigame":
			return new KIGame(state, board, fxUI);
			default:
				return null;
		}
	}
	
	private void startGame(Game game) {
		game.run();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void stop() {
		System.exit(0);
	}
}
