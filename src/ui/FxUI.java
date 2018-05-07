package ui;

import core.BoardView;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import location.XY;
import ui.UI;
import ui.commandhandle.Command;
import ui.commandhandle.GameCommandType;
import ui.commandhandle.MoveCommand;
import ui.windows.InputWindow;
import ui.windows.OutputWindow;

public class FxUI extends Scene implements UI {
	private static final int CELL_SIZE = 25;
	private static Command nextCommand = new Command(GameCommandType.NOTHING);

	private Canvas boardCanvas;
	private Label msgLabel;

	public FxUI(Parent parent, Canvas boardCanvas, Label msgLabel) {
		super(parent);
		this.boardCanvas = boardCanvas;
		this.msgLabel = msgLabel;
	}

	public static FxUI createInstance(XY boardSize) {
		Canvas boardCanvas = new Canvas(boardSize.getX() * CELL_SIZE, boardSize.getY() * CELL_SIZE);
		Label masterEnergy = new Label();
		VBox top = new VBox();
		top.getChildren().add(createMenuBar());
		top.getChildren().add(boardCanvas);
		top.getChildren().add(masterEnergy);
		masterEnergy.setText("Hello World");
		final FxUI fxUI = new FxUI(top, boardCanvas, masterEnergy);
		fxUI.setOnKeyPressed(value -> {
			switch (value.getCode()) {
			case W:
				nextCommand = new Command(GameCommandType.MOVE, MoveCommand.UP);
				break;
			case A:
				nextCommand = new Command(GameCommandType.MOVE, MoveCommand.LEFT);
				break;
			case S:
				if (nextCommand.getCommandType() == GameCommandType.NOTHING) {
					nextCommand = new Command(GameCommandType.MOVE, MoveCommand.NONE);
				}
				break;
			case D:
				nextCommand = new Command(GameCommandType.MOVE, MoveCommand.RIGHT);
				break;
			case X:
				nextCommand = new Command(GameCommandType.MOVE, MoveCommand.DOWN);
				break;
			case Q:
				nextCommand = new Command(GameCommandType.MOVE, MoveCommand.UP_LEFT);
				break;
			case E:
				nextCommand = new Command(GameCommandType.MOVE, MoveCommand.UP_RIGHT);
				break;
			case Y:
				nextCommand = new Command(GameCommandType.MOVE, MoveCommand.DOWN_LEFT);
				break;
			case C:
				nextCommand = new Command(GameCommandType.MOVE, MoveCommand.DOWN_RIGHT);
				break;
			case P:
				nextCommand = new Command(GameCommandType.SPAWN_MINI, "100");
				break;
			default:
				break;
			}
		});
		return fxUI;
	}

	private static MenuBar createMenuBar() {
		MenuBar mb = new MenuBar();
		Menu file = new Menu("Game");
		Menu help = new Menu("Help");
		MenuItem helpContents = new MenuItem("Help contents");
		helpContents.setOnAction(value -> {
			new OutputWindow(help(), "Help");
		});
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(value -> {
			System.exit(0);
		});
		MenuItem spawnMiniSquirrel = new MenuItem("Spawn Mini");
		spawnMiniSquirrel.setOnAction(value -> {
			InputWindow spawnMS = new InputWindow("Spawn Minisquirrel", "Specify how much energy you want to give");
			spawnMiniSquirrel(spawnMS);
		});
		help.getItems().add(helpContents);
		file.getItems().addAll(spawnMiniSquirrel, exit);
		mb.getMenus().addAll(file, help);
		return mb;
	}

	private static void spawnMiniSquirrel(InputWindow spawnMS) {
		spawnMS.getEnterButton().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			String input = spawnMS.getTextField().getText();
			try {
				nextCommand = new Command(GameCommandType.SPAWN_MINI, input);
				System.out.println(nextCommand.toString());
			} catch (NumberFormatException e) {
				new OutputWindow("Didn't specify a number", "Error");
			}
			spawnMS.close();
		});
	}

	private static String help() { // TODO change to actual help now
		StringBuilder sb = new StringBuilder("Help: \n");
		sb.append("Q move up left\n");
		sb.append("W move up\n");
		sb.append("E move up right\n");
		sb.append("A move left\n");
		sb.append("D move right\n");
		sb.append("Y move down left\n");
		sb.append("X move down\n");
		sb.append("C move down right\n");
		sb.append("P spawn MiniSquirrel with 100 energy\n");
		return sb.toString();
	}

	@Override
	public void render(final BoardView view) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				repaintBoardCanvas(view);
			}
		});
	}

	private void repaintBoardCanvas(BoardView view) {
		GraphicsContext gc = boardCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());
		XY size = view.getSize();

		for (int i = 0, a = 0; i < size.getY() * CELL_SIZE; i += CELL_SIZE, a++) {
			for (int j = 0, b = 0; j < size.getX() * CELL_SIZE; j += CELL_SIZE, b++) {
				switch (view.getEntityType(b, a)) {
				case WALL:
					gc.setFill(Color.DARKSLATEGRAY);
					gc.fillRect(j, i, CELL_SIZE, CELL_SIZE);
					break;
				case BADBEAST:
					gc.setFill(Color.RED);
					gc.fillOval(j, i, CELL_SIZE, CELL_SIZE);
					break;
				case BADPLANT:
					gc.setFill(Color.RED);
					gc.fillRect(j, i, CELL_SIZE, CELL_SIZE);
					break;
				case GOODBEAST:
					gc.setFill(Color.LAWNGREEN);
					gc.fillOval(j, i, CELL_SIZE, CELL_SIZE);
					break;
				case GOODPLANT:
					gc.setFill(Color.LAWNGREEN);
					gc.fillRect(j, i, CELL_SIZE, CELL_SIZE);
					break;
				case HANDOPERATEDMASTERSQUIRREL:
					gc.setFill(Color.BLUE);
					gc.fillOval(j, i, CELL_SIZE, CELL_SIZE);
					break;
				case MASTERSQUIRREL:
					gc.setFill(Color.BLUE);
					gc.fillOval(j, i, CELL_SIZE, CELL_SIZE);
					break;
				case MINISQUIRREL:
					gc.setFill(Color.AQUA);
					gc.fillOval(j, i, CELL_SIZE, CELL_SIZE);
					break;
				case NONE:
					break;
				default:
					break;
				}
			}
		}
	}

	public void message(final String msg) {
		Platform.runLater(() -> {
			msgLabel.setText(msg);
		});
	}

	@Override
	public Command getCommand() {
		if (nextCommand.getCommandType() != GameCommandType.MOVE) {
			Command returned = nextCommand;
			nextCommand = new Command(GameCommandType.NOTHING);
			return returned;
		}
		return nextCommand;
	}
}