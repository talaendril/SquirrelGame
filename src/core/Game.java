package core;

import entities.MasterSquirrel;
import ui.UI;

public abstract class Game {
	
	private State state;
	private Board board;
	private UI ui;
	
	private MasterSquirrel[] masters;
	
	public Game(State state, Board board, UI ui) {
		this.state = state;
		this.board = board;
		this.ui = ui;
	}
	
	public State getState() {
		return this.state;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public UI getUI() {
		return this.ui;
	}
	
	public void addMasters(MasterSquirrel[] masters) {
		this.masters = masters;
	}
	
	public MasterSquirrel[] getMasters() {
		return this.masters;
	}
	
	public abstract void run();
	
	protected abstract void render();
	
	protected abstract void processInput();
	
	protected abstract void update();
}
