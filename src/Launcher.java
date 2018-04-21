import java.io.BufferedReader;
import java.io.InputStreamReader;

import core.Board;
import core.BoardConfig;
import core.Game;
import core.State;
import ui.CommandScanner;
import ui.ConsoleUI;
import ui.GameCommandType;

public class Launcher {

	public static void main(String[] args) {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		CommandScanner cs = new CommandScanner(GameCommandType.values(), input);
		ConsoleUI cui = new ConsoleUI(cs);
		BoardConfig config = new BoardConfig();
		Board board = new Board(config);
		State state = new State(board);
		Game newGame = new Game(state, board, cui);
		newGame.run();
	}
}
