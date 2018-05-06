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

public class SinglePlayer extends Game {
	
	private final int FPS = 10;
	
	private static int processingCount = 0;		//TODO remove once down debugging

	private Command nextCommand = new Command(GameCommandType.NOTHING);
	
	public SinglePlayer(State state, Board board, UI ui) {
		super(state, board, ui);
	}
	
	public void run() {
		Timer renderTimer = new Timer();
		renderTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				render();
				setMessageToMasterEnergy();
			}
		}, 0, 1000/FPS);
		
		Timer inputTimer = new Timer();
		inputTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				processInput();
			}
		}, 0, 1000);
		
		Timer updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				update();
			}
		}, 1000, 1000);
	}
	
	protected void render() {
		this.getUI().render(this.getBoard().flatten());
	}
	
	protected void setMessageToMasterEnergy() {
		this.getUI().message("Master Energy: " + this.getBoard().getMaster().getEnergy());
	}
	
	protected void processInput() {
		nextCommand = this.getUI().getCommand();
		System.err.println(nextCommand.toString());
	}
	
	protected void update() {
		Object[] params = nextCommand.getParams();
		for(GameCommandType gct : GameCommandType.values()) {
			if(nextCommand.getCommandType().getName().equals(gct.getName())) {
				Method method = null;
				try {
					processingCount++;
					GameCommandRunner gcr = new GameCommandRunner(this.getState());
					if(params.length == 0) {		//using keyevents this probably doesnt happen, but needs more testing
						System.out.println(processingCount + " COMMAND NAME " + gct.getName());
						method = gcr.getClass().getMethod(gct.getMethodToCall());
						System.out.println(processingCount + " TRYING TO INVOKE " + method.toString());
						method.invoke(gcr);
					} else if(params.length == 1) {
						System.out.println(processingCount + " COMMAND NAME " + gct.getName() + " PARAMETER " + params[0].toString());
						method = gcr.getClass().getMethod(gct.getMethodToCall(), Object.class);
						System.out.println(processingCount + " TRYING TO INVOKE " + method.toString() + " " + params[0].toString());
						method.invoke(gcr, params[0]);
					} else {
						throw new ScanException("Wrong Number of Parameters"); //TODO think about changing this
					}
				} catch (NoSuchMethodException e) {
					if(params.length == 0) {
						System.out.println(processingCount + " Either method name or parameter wasnt correct");
					} else {
						System.out.println(processingCount + " " + params[0].toString());
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
					System.out.println(processingCount + " " + method.toString() + " parameter was null or incorrect");
					e.printStackTrace();
				}
			}
		}
	}
}
