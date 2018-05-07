package core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import core.bots.MasterSquirrelBot;
import entities.MasterSquirrel;
import exceptions.ScanException;
import idmanager.ID;
import location.XY;
import ui.UI;
import ui.commandhandle.Command;
import ui.commandhandle.GameCommandRunner;
import ui.commandhandle.GameCommandType;

public class KIGame extends Game {

private final int FPS = 10;
	
	private static int processingCount = 0;		//TODO remove once down debugging

	private Command nextCommand = new Command(GameCommandType.NOTHING);
	
	public KIGame(State state, Board board, UI ui) {
		super(state, board, ui);
		MasterSquirrel master = new MasterSquirrelBot(ID.getNewID(), new XY(-1, -1));
		MasterSquirrel[] masters = {master};
		this.addMasters(masters);
		this.getBoard().generateMasterSquirrels(masters);
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
		
		Timer updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				update();
			}
		}, 950, 1000);
	}
	
	protected void render() {
		this.getUI().render(this.getBoard().flatten());
	}
	
	protected void setMessageToMasterEnergy() {
		StringBuilder sb = new StringBuilder("");
		MasterSquirrel[] masters = this.getMasters();
		for(int i = 0; i < masters.length; i++) {
			sb.append("Master Energy" + i + ": " + masters[i].getEnergy() + "\n");
		}
		this.getUI().message(sb.toString());
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

	@Override
	protected void processInput() {
		//do nothing
	}
}