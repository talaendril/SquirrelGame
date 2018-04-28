package core;

import java.util.Timer;
import java.util.TimerTask;

import ui.UI;
import ui.CommandHandle.Command;
import ui.CommandHandle.GameCommandProcessor;

public class Game {
	
	private State state;
	private GameCommandProcessor gcp = new GameCommandProcessor(this.state);
	private Board board;
	private UI ui;
	private Command nextCommand;
	
	public Game(State state, Board board, UI ui) {
		this.state = state;
		this.board = board;
		this.ui = ui;
	}
	
	public void run() {
//		while(true) {
//			render();
//			processInput();
//			update();
//		}
		render();
	}
	
	protected void render() {
		System.out.println("");
		ui.render(board.flatten());
		System.out.println(board.toString());
	}
	
	protected void processInput() {
		nextCommand = this.ui.getCommand();
	}
	
	protected void update() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//        		gcp.process(nextCommand);
//            }
//        }, 0, 1000);
        try {
			Thread.sleep(1000);
			gcp.process(nextCommand);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
