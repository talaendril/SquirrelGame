package ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import core.State;
import exceptions.ScanException;

public class GameCommandProcessor {
	
	private State state;
	
	public GameCommandProcessor(State state) {
		this.state = state;
	}
	
	public void processReflection(Command command) {
		Object[] params = command.getParams();
		for(GameCommandType gct : GameCommandType.values()) {
			if(command.getCommandType().getName().equals(gct.getName())) {
				Method method;
				try {
					GameCommandRunner gcr = new GameCommandRunner(this.state);
					if(params.length == 0) {
						method = gcr.getClass().getMethod(gct.getMethodToCall());
						method.invoke(gcr);
					} else if(params.length == 1) {
						method = gcr.getClass().getMethod(gct.getMethodToCall(), Object.class);
						method.invoke(gcr, params[0]);
					} else {
						throw new ScanException("Wrong Number of Parameters"); //TODO think about changing this
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void help() {
		StringBuilder sb = new StringBuilder("List of all Commands: \n");
		for(GameCommandType gct : GameCommandType.values()) {
			sb.append("\t" + gct.toString() + "\n");
		}
		System.out.println(sb.toString());
	}
}
