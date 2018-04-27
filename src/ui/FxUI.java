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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import location.XY;
import ui.UI;
import ui.CommandHandle.Command;
import ui.CommandHandle.GameCommandType;
import ui.CommandHandle.MoveCommand;

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
        Label statusLabel = new Label();
        VBox top = new VBox();
        top.getChildren().add(createMenuBar());
        top.getChildren().add(boardCanvas);
        top.getChildren().add(statusLabel);
        statusLabel.setText("Hallo Welt");
        final FxUI fxUI = new FxUI(top, boardCanvas, statusLabel); 
        fxUI.setOnKeyPressed(value -> {
        	System.out.println("Es wurde folgende Taste gedrückt: " + value.getCode() + " bitte behandeln!");
        	switch(value.getCode()) {
        	case W:
        		nextCommand = new Command(GameCommandType.MOVE, MoveCommand.UP);
        		break;
        	case A:
        		nextCommand = new Command(GameCommandType.MOVE, MoveCommand.LEFT);
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
        	default:
        		break;
        	}
        });
        return fxUI;
    }

    private static MenuBar createMenuBar() {
		MenuBar mb = new MenuBar();
		Menu file = new Menu("Game");
		MenuItem help = new MenuItem("Help");
		help.setOnAction(value -> {
			//TODO: open a new window with all commands maybe
		});
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(value -> {
			System.exit(0);
		});
		MenuItem pause = new MenuItem("Pause");
		pause.setOnAction(value -> {
			//TODO: pause the game
		});
		file.getItems().addAll(help, pause, exit);
		mb.getMenus().add(file);
		return mb;
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
        
		for(int i = 0, a = 0; i < size.getY() * CELL_SIZE; i += CELL_SIZE, a++) {
			for(int j = 0, b = 0; j < size.getX() * CELL_SIZE; j += CELL_SIZE, b++) {
				switch(view.getEntityType(b, a)) {
				case WALL:
					gc.setFill(Color.DARKSLATEGRAY);
					gc.fillRect(i, j, CELL_SIZE, CELL_SIZE);
					break;
				case BADBEAST:
					gc.setFill(Color.RED);
					gc.fillOval(i, j, CELL_SIZE, CELL_SIZE);
					break;
				case BADPLANT:
					gc.setFill(Color.RED);
					gc.fillRect(i, j, CELL_SIZE, CELL_SIZE);
					break;
				case GOODBEAST:
					gc.setFill(Color.LAWNGREEN);
					gc.fillOval(i, j, CELL_SIZE, CELL_SIZE);
					break;
				case GOODPLANT:
					gc.setFill(Color.LAWNGREEN);
					gc.fillRect(i, j, CELL_SIZE, CELL_SIZE);
					break;
				case HANDOPERATEDMASTERSQUIRREL:
					gc.setFill(Color.BLUE);
					gc.fillOval(i, j, CELL_SIZE, CELL_SIZE);
					break;
				case MASTERSQUIRREL:
					gc.setFill(Color.BLUE);
					gc.fillOval(i, j, CELL_SIZE, CELL_SIZE);
					break;
				case MINISQUIRREL:
					gc.setFill(Color.AQUA);
					gc.fillOval(i, j, CELL_SIZE, CELL_SIZE);
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                msgLabel.setText(msg);            
            }      
        });         
    }

	@Override
	public Command getCommand() {
		return nextCommand;
	}
}