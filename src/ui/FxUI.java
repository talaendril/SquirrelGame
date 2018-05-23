package ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import core.BoardView;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import location.XY;
import ui.commandhandle.Command;
import ui.commandhandle.GameCommandType;
import ui.commandhandle.MoveCommand;
import ui.windows.InputWindow;
import ui.windows.OutputWindow;

public class FxUI extends Scene implements UI {
    private static final Logger LOGGER = Logger.getLogger(FxUI.class.getName());
    private static final int CELL_SIZE = 30;

    private static Image masterImage;
    private static Image miniSquirrelImage;
    private static Image wallImage;
    private static Image badBeastImage;
    private static Image badPlantImage;
    private static Image goodBeastImage;
    private static Image goodPlantImage;

    private static Command nextCommand = new Command(GameCommandType.NOTHING);
    private static Command lastMoveCommand = new Command(GameCommandType.NOTHING);

    private Canvas boardCanvas;
    private Label msgLabel;

    private FxUI(Parent parent, Canvas boardCanvas, Label msgLabel) {
        super(parent);
        this.boardCanvas = boardCanvas;
        this.msgLabel = msgLabel;
    }

    public static FxUI createInstance(XY boardSize) {
        loadImages();
        Canvas boardCanvas = new Canvas(boardSize.x * CELL_SIZE, boardSize.y * CELL_SIZE);
        Label masterEnergy = new Label();
        AnchorPane.setBottomAnchor(masterEnergy, 10.0);
        VBox top = new VBox();
        top.getChildren().add(createMenuBar());
        top.getChildren().add(boardCanvas);
        top.getChildren().add(masterEnergy);
        masterEnergy.setText("");
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
                case I:
                    lastMoveCommand = nextCommand;
                    nextCommand = new Command(GameCommandType.IMPLODE_MINI, "3");
                    break;
                case P:
                    lastMoveCommand = nextCommand;
                    nextCommand = new Command(GameCommandType.SPAWN_MINI, "100");
                    break;
                default:
                    break;
            }
        });
        return fxUI;
    }

    private static void loadImages() {
        try {
            masterImage = new Image(new FileInputStream("images\\MasterSquirrelBlackOutline.png"), CELL_SIZE, CELL_SIZE, false, false);
            wallImage = new Image(new FileInputStream("images\\Wall.jpg"), CELL_SIZE, CELL_SIZE, false, false);
            goodBeastImage = new Image(new FileInputStream("images\\GoodBeast.png"), CELL_SIZE, CELL_SIZE, false, false);
            goodPlantImage = new Image(new FileInputStream("images\\GoodPlant.png"), CELL_SIZE, CELL_SIZE, false, false);
            badBeastImage = new Image(new FileInputStream("images\\BadBeast.png"), CELL_SIZE, CELL_SIZE, false, false);
            badPlantImage = new Image(new FileInputStream("images\\BadPlant.png"), CELL_SIZE, CELL_SIZE, false, false);
            miniSquirrelImage = new Image(new FileInputStream("images\\MiniSquirrel.png"), CELL_SIZE, CELL_SIZE, false, false);
        } catch (FileNotFoundException e) {
            LOGGER.severe("Could not find the specified image to load");
        }
    }

    private void repaintBoardCanvas(BoardView view) {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());
        XY size = view.getSize();

        for (int i = 0, a = 0; i < size.y * CELL_SIZE; i += CELL_SIZE, a++) {
            for (int j = 0, b = 0; j < size.x * CELL_SIZE; j += CELL_SIZE, b++) {
                switch (view.getEntityType(b, a)) {
                    case WALL:
                        gc.drawImage(wallImage, j, i);
                        break;
                    case BAD_BEAST:
                        gc.drawImage(badBeastImage, j, i);
                        break;
                    case BAD_PLANT:
                        gc.drawImage(badPlantImage, j, i);
                        break;
                    case GOOD_BEAST:
                        gc.drawImage(goodBeastImage, j, i);
                        break;
                    case GOOD_PLANT:
                        gc.drawImage(goodPlantImage, j, i);
                        break;
                    case MASTER_SQUIRREL:
                        gc.drawImage(masterImage, j, i);
                        gc.setFill(Color.BLACK);
                        gc.fillText(Integer.toString(view.getEntity(b, a).getID()), j, i, CELL_SIZE);
                        break;
                    case MINI_SQUIRREL:
                        gc.drawImage(miniSquirrelImage, j, i);
                        break;
                    case NONE:
                    default:
                        break;
                }
            }
        }
    }

    private static MenuBar createMenuBar() {
        MenuBar mb = new MenuBar();
        Menu file = new Menu("Game");
        Menu help = new Menu("Help");
        MenuItem helpContents = new MenuItem("Help contents");
        helpContents.setOnAction(value -> new OutputWindow(help(), "Help"));
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(value -> {
            LOGGER.log(Level.INFO, "stopped game");
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
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Didn't specify a number for the MiniSquirrel", e);
                new OutputWindow("Didn't specify a number", "Error");
            }
            spawnMS.close();
        });
    }

    private static String help() {
        return "Help: \n" + "You play the Squirrel\n" +
                "Acorns are Good Plants\n" +
                "Blue Birds are Good Beasts\n" +
                "Green Plants with Yellow Heads are Bad Plants\n" +
                "Green Snakes are Bad Beasts\n\n" +
                "Q move up left\n" +
                "W move up\n" +
                "E move up right\n" +
                "A move left\n" +
                "D move right\n" +
                "Y move down left\n" +
                "X move down\n" +
                "C move down right\n" +
                "P spawn MiniSquirrel with 100 energy\n";
    }

    @Override
    public void render(final BoardView view) {
        Platform.runLater(() -> repaintBoardCanvas(view));
    }

    @Override
    public void implode(XY location, int impactRadius) {    //TODO show for more than 1 frame
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        gc.setFill(Color.AQUA);
        gc.fillOval(location.x * CELL_SIZE, location.y * CELL_SIZE, CELL_SIZE * impactRadius, CELL_SIZE * impactRadius);
    }

    @Override
    public void message(final String msg) {
        Platform.runLater(() -> msgLabel.setText(msg));
    }

    @Override
    public Command getCommand() {
        if (nextCommand.getCommandType() != GameCommandType.MOVE) {
            Command returned = nextCommand;
            nextCommand = lastMoveCommand;
            return returned;
        }
        return nextCommand;
    }
}