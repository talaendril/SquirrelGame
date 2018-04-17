import core.Board;
import core.Game;
import ui.ConsoleUI;

public class Launcher {

	public static void main(String[] args) {
		Board board = new Board();
		ConsoleUI cui = new ConsoleUI();
		Game newGame = new Game(null, board, cui);
		newGame.run();
	}
}
