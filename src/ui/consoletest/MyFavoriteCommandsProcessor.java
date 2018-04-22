package ui.consoletest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ui.Command;
import ui.CommandScanner;

public class MyFavoriteCommandsProcessor {
	
	private static BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
	
	public static void process() {
		 CommandScanner commandScanner = new CommandScanner(MyFavoriteCommandType.values(), inputReader);	//watchout for different type
		 Command command;
	        
		 while (true) { // the loop over all commands with one input line for every command
		     command = commandScanner.next();
		     if(command == null) {
		    	 System.out.println("Ungültiger Command");
		    	 continue;
		     }
		     Object[] params = command.getParams();

		     MyFavoriteCommandType commandType = (MyFavoriteCommandType) command.getCommandType();
		     
		     switch (commandType) {
		     case EXIT:
		         System.exit(0);
		     case HELP: 
		         help();
		         break;
		     case ADDI:
		    	 int totalInt = 0;
		    	 for(int i = 0; i < params.length; i++) {
		    		 totalInt += Integer.parseInt((String) params[i]);
		    	 }
		    	 System.out.println(totalInt);
		    	 break;
		     case ADDF:
		    	 float totalFloat = 0.f;
		    	 for(int i = 0; i < params.length; i++) {
		    		 totalFloat += Float.parseFloat((String) params[i]);
		    	 }
		    	 System.out.println(totalFloat);
		    	 break;
		     case ECHO:
		    	 for(int i = 0; i < Integer.parseInt((String) params[params.length - 1]); i++) {
		    		 for(int j = 0; j < params.length - 1; j++) {
		    			 System.out.print(params[j] + " ");
		    		 }
		    		 System.out.println("");
		    	 }
		    	 break;
		     default:
		    	 break;
		     }
		 }
	}
	
	public static void processReflection() {
		CommandScanner scanner = new CommandScanner(MyFavoriteCommandType.values(), inputReader);
		Command command;
		while(true) {
			command = scanner.next();
			if(command == null) {
		    	 System.out.println("Ungültiger Command");
		    	 continue;
		     }
			Object[] params = command.getParams();
			for (MyFavoriteCommandType mfct : MyFavoriteCommandType.values()) {
				if(command.getCommandType().getName().equals(mfct.getName())) {
					MethodClass obj = new MethodClass();
					try {
						Method method;
						if(params.length == 0) {
							method = obj.getClass().getMethod(mfct.getMethodToCall());
							method.invoke(obj);
						} else {
							method = obj.getClass().getMethod(mfct.getMethodToCall(), new Class[] {Object.class, Object.class});
							method.invoke(obj, params[0], params[1]);
						}
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void help() {
		StringBuilder sb = new StringBuilder("List of all Commands: \n");
		for(MyFavoriteCommandType gct : MyFavoriteCommandType.values()) {
			sb.append("\t" + gct.toString() + "\n");
		}
		System.out.println(sb.toString());
	}
	
	public static void main(String[] args) {
		processReflection();
	}
}