package core;

import entities.MasterSquirrel;
import location.XY;
import ui.UI;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public abstract class Game {

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());
	
	private State state;
	private Board board;
	private UI ui;

	private Map<String, ArrayList<Integer>> highScores = new Hashtable<>();
	boolean savedHighScores = false;

	private List<MasterSquirrel> masters = new ArrayList<>();
	
	Game(State state, Board board, UI ui) {
		this.state = state;
		this.board = board;
		this.ui = ui;
	}

    State getState() {
		return this.state;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	UI getUI() {
		return this.ui;
	}

	List<MasterSquirrel> getMasters() {
	    return this.masters;
    }

    void addMasters(MasterSquirrel[] masters) {
        this.masters.addAll(Arrays.asList(masters));
    }
	
	void setMessageToMasterEnergy() {
		StringBuilder sb = new StringBuilder();
		for (MasterSquirrel master : masters) {
			sb.append("Master Energy").append(master.getID()).append(": ").append(master.getEnergy()).append("\t");
		}
		this.getUI().message(sb.toString());
	}

	void setRemainingSteps() {
        this.ui.remainingSteps(this.board.getRemainingSteps());
    }

    void resetGame() {
	    this.board = new Board(this.board.getConfig());
	    this.state = new State(this.board);
	    resetMasters();
	    MasterSquirrel[] masterSquirrels = masters.toArray(new MasterSquirrel[masters.size()]);
	    this.board.generateMasterSquirrels(masterSquirrels);
	    this.savedHighScores = false;
	    this.run(this.board.getConfig().getMaximumSteps());
    }

    private void resetMasters() {
	    List<MasterSquirrel> resetMasters = new ArrayList<>();
        for(MasterSquirrel master: masters) {
            MasterSquirrel resetMaster = new MasterSquirrel(master.getID(), new XY(0, 0), master.getName());
            resetMasters.add(resetMaster);
        }
        masters = resetMasters;
    }

    void saveHighScores() {
	    for(MasterSquirrel master : masters) {
	        String key = master.getName() + master.getID();
            ArrayList<Integer> list = highScores.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(master.getEnergy());
        }
        this.savedHighScores = true;
	    highScoresToFile();
    }

    private void highScoresToFile() {
	    try {
            FileOutputStream fileOutputStream = new FileOutputStream("highScores.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(highScores);
            out.close();
            fileOutputStream.close();
            System.out.println(highScores.toString());
        } catch (IOException e) {
            LOGGER.fine("Couldn't write to highScores.ser file.");
        }
    }

    void getHighScoresFromFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("highScores.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            highScores = (Map<String, ArrayList<Integer>>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            LOGGER.fine("Couldn't load highScores.ser file.\nMaybe not created yet");
        } catch (ClassNotFoundException e) {
            LOGGER.severe("The read map is not compatible with our highScores map");
        }
    }

    public abstract void run(int steps);

	protected abstract void render();
	
	protected abstract void processInput();
	
	protected abstract void update();
}
