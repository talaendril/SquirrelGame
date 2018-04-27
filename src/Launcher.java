import java.io.BufferedReader;
import java.io.InputStreamReader;

import core.Board;
import core.BoardConfig;
import core.Game;
import core.State;
import javafx.application.Application;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import ui.ConsoleUI;
import ui.FxUI;
import ui.CommandHandle.CommandScanner;
import ui.CommandHandle.GameCommandType;

public class Launcher extends Application {
	
	private BoardConfig config = new BoardConfig();
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	private CommandScanner cs = new CommandScanner(GameCommandType.values(), input);
	private ConsoleUI cui = new ConsoleUI(cs);
	private Board board = new Board(config);
	private State state = new State(board);

	@Override
	public void start(Stage arg0) throws Exception {
		Stage primaryStage = new Stage();
		FxUI fxUI = FxUI.createInstance(config.getSize());
        final Game game = new Game(state, board, fxUI);
         
        primaryStage.setScene(fxUI);
        primaryStage.setTitle("Diligent Squirrel");
        primaryStage.show();   
        
        startGame(game);   
	}
	
	private void startGame(Game game) {
		game.run();
	}
	
	private MenuBar createMenuBar() {
		MenuBar mb = new MenuBar();
		Menu file = new Menu("Game");
		MenuItem help = new MenuItem("Help");
		MenuItem exit = new MenuItem("Exit");
		MenuItem pause = new MenuItem("Pause");
		file.getItems().addAll(help, pause, exit);
		mb.getMenus().add(file);
		return mb;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
