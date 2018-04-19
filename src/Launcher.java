import core.Board;
import core.BoardConfig;
import core.Game;
import core.State;
import ui.ConsoleUI;

public class Launcher {

	public static void main(String[] args) {
		BoardConfig config = new BoardConfig();
		Board board = new Board(config);
		ConsoleUI cui = new ConsoleUI();
		State state = new State(board);
		Game newGame = new Game(state, board, cui);
		newGame.run();
	}
}
