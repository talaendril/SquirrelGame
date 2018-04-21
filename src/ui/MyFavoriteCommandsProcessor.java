package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class MyFavoriteCommandsProcessor {

	//TODO REMOVE STATICS WHEN DONE TESTING
	
	private PrintStream outputStream = System.out;
	private static BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
	
	public static void process() {
		 CommandScanner commandScanner = new CommandScanner(MyFavoriteCommandType.values(), inputReader);
		 Command command;
	        
		 while (true) { // the loop over all commands with one input line for every command
		     //TODO
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
		    		 totalInt += (Integer) params[i];
		    	 }
		    	 System.out.println(totalInt);
		    	 break;
		     case ADDF:
		    	 float totalFloat = 0.f;
		    	 for(int i = 0; i < params.length; i++) {
		    		 totalFloat += (Float) params[i];
		    	 }
		    	 System.out.println(totalFloat);
		    	 break;
		     case ECHO:
		    	 for(int i = 0; i < (Integer) params[1]; i++) {		//have to assume the second parameter is a number
		    		 System.out.println(params[0]);					//and the first is a string
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
