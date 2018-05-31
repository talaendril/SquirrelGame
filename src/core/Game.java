package core;

import entities.MasterSquirrel;
import entities.squirrelbots.MasterSquirrelBot;
import idmanager.ID;
import location.XY;
import ui.UI;

public abstract class Game {
	
	private State state;
	private Board board;
	private UI ui;

	private int currentStep = 0;
	
	private MasterSquirrel[] masters;
	
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
	
	void addMasters(MasterSquirrel[] masters) {
		this.masters = masters;
	}

	MasterSquirrel[] getMasters() {
		return this.masters;
	}

	int getCurrentStep() {
	    return this.currentStep;
    }

    void incrementCurrentStep() {
	    this.currentStep++;
    }

	public abstract void run(int steps);
	
	void setMessageToMasterEnergy() {
		StringBuilder sb = new StringBuilder();
		MasterSquirrel[] masters = this.getMasters();
		for (MasterSquirrel master : masters) {
			sb.append("Master Energy").append(master.getID()).append(": ").append(master.getEnergy()).append("\n");
		}
		this.getUI().message(sb.toString());
	}

	void setRemainingSteps() {
        this.ui.remainingSteps(this.board.getConfig().getMaximumSteps() - this.currentStep);
    }

    void resetGame() {      //nearly works, need to stop remaining threads before resetting
	    this.board = new Board(this.board.getConfig());
	    this.state = new State(this.board);
	    currentStep = 0;
	    createMasters();
	    this.run(this.board.getConfig().getMaximumSteps());
    }

    void createMasters() {
        int x, y = x = -1;
        String[] bots = this.getBoard().getConfig().getBotNames();

        MasterSquirrel master1 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--), bots[0]);
        MasterSquirrel master2 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--), bots[1]);
        MasterSquirrel master3 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--), bots[0]);
        MasterSquirrel master4 = new MasterSquirrelBot(ID.getNewID(), new XY(x--, y--), bots[1]);
        MasterSquirrel[] masters = {master1, master2, master3, master4};
        this.addMasters(masters);
        this.getBoard().generateMasterSquirrels(masters);
    }
	
	protected abstract void render();
	
	protected abstract void processInput();
	
	protected abstract void update();
}
