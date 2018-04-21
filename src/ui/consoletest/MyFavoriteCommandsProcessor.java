package ui.consoletest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
//import java.io.PrintStream;

import ui.Command;
import ui.CommandScanner;

public class MyFavoriteCommandsProcessor {

	//TODO REMOVE STATICS WHEN DONE TESTING
	
	//private PrintStream outputStream = System.out;
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
	
	private static void help() {
		System.out.println("List of all Commands: ");
		System.out.println("\t" + MyFavoriteCommandType.EXIT.toString());
		System.out.println("\t" + MyFavoriteCommandType.HELP.toString());
		System.out.println("\t" + MyFavoriteCommandType.ADDI.toString());
		System.out.println("\t" + MyFavoriteCommandType.ADDF.toString());
		System.out.println("\t" + MyFavoriteCommandType.ECHO.toString());
	}
	
	public static void main(String[] args) {
		process();
	}
}
