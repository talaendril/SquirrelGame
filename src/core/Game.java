package core;

import entities.MasterSquirrel;
import location.XY;
import ui.UI;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public abstract class Game {
	
	private State state;
	private Board board;
	private UI ui;

	private Map<String, Integer> highScores = new Hashtable<>();
	boolean savedHighScores = false;

	private int currentStep = 0;

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
	    for(MasterSquirrel master: masters) {
	        this.masters.add(master);
        }
    }

	int getCurrentStep() {
	    return this.currentStep;
    }

    void incrementCurrentStep() {
	    this.currentStep++;
    }
	
	void setMessageToMasterEnergy() {
		StringBuilder sb = new StringBuilder();
		for (MasterSquirrel master : masters) {
			sb.append("Master Energy").append(master.getID()).append(": ").append(master.getEnergy()).append("\t");
		}
		this.getUI().message(sb.toString());
	}

	void setRemainingSteps() {
        this.ui.remainingSteps(this.board.getConfig().getMaximumSteps() - this.currentStep);
    }

    void resetGame() {
	    this.board = new Board(this.board.getConfig());
	    this.state = new State(this.board);
	    currentStep = 0;
	    resetMasters();
	    MasterSquirrel[] masterSquirrels = masters.toArray(new MasterSquirrel[masters.size()]);
	    this.board.generateMasterSquirrels(masterSquirrels);
	    this.savedHighScores = false;
	    this.run(this.board.getConfig().getMaximumSteps());
    }

    void resetMasters() {
	    List<MasterSquirrel> resettedMasters = new ArrayList<>();
        for(MasterSquirrel master: masters) {
            MasterSquirrel resettedMaster = new MasterSquirrel(master.getID(), new XY(0, 0), master.getName());
            resettedMasters.add(resettedMaster);
        }
        masters = resettedMasters;
    }

    void saveHighScores() {
	    for(MasterSquirrel master : masters) {
            highScores.put(master.getName() + master.getID(), master.getEnergy());
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getHighScoresFromFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("highScores.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            highScores = (Map<String, Integer>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getHighScores() {
        return highScores;
    }

    public abstract void run(int steps);

	protected abstract void render();
	
	protected abstract void processInput();
	
	protected abstract void update();
}
