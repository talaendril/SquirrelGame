package core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import exceptions.ScanException;
import ui.UI;
import ui.CommandHandle.Command;
import ui.CommandHandle.GameCommandRunner;
import ui.CommandHandle.GameCommandType;

public class Game {
	
	private State state;
	private Board board;
	private UI ui;
	private Command nextCommand = new Command(GameCommandType.NOTHING);
	
	public Game(State state, Board board, UI ui) {
		this.state = state;
		this.board = board;
		this.ui = ui;
	}
	
	public void run() {
		Timer rendering = new Timer();
		rendering.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				render();
			}
		}, 0, 100);
		
		Timer inputTimer = new Timer();
		inputTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				processInput();
			}
		}, 0, 500);
		
		Timer updating = new Timer();
		updating.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				update();
			}
		}, 1000, 1000);
	}
	
	protected void render() {
		ui.render(board.flatten());
	}
	
	protected void processInput() {
		nextCommand = this.ui.getCommand();
		System.err.println(nextCommand.toString());
	}
	
	protected void update() {
		Object[] params = nextCommand.getParams();
		for(GameCommandType gct : GameCommandType.values()) {
			if(nextCommand.getCommandType().getName().equals(gct.getName())) {
				Method method = null;
				try {
					GameCommandRunner gcr = new GameCommandRunner(this.state);
					if(params.length == 0) {		//using keyevents this probably doesnt happen, but needs more testing
						method = gcr.getClass().getMethod(gct.getMethodToCall());
						method.invoke(gcr);
					} else if(params.length == 1) {
						method = gcr.getClass().getMethod(gct.getMethodToCall(), Object.class);
						System.out.println("PROCESSING COMMAND " + method.toString() + " " + params[0].toString());
						method.invoke(gcr, params[0]);
					} else {
						throw new ScanException("Wrong Number of Parameters"); //TODO think about changing this
					}
				} catch (NoSuchMethodException e) {
					if(params.length == 0) {
						System.out.println("Either method name or parameter wasnt correct");
					} else {
						System.out.println(params[0].toString());
					}
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					System.out.println(method.toString() + " parameter was null");
					e.printStackTrace();
				}
			}
		}
	}
}
