
import core.Board;
import core.BoardConfig;
import core.Game;
import core.SinglePlayer;
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
        final Game game = new SinglePlayer(state, board, fxUI);
         
        primaryStage.setScene(fxUI);
        primaryStage.setTitle("Diligent Squirrel");
        primaryStage.show();   
        
        startGame(game);   
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
