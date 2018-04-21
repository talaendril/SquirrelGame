package ui;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Scanner;

public class CommandScanner {
	
	private CommandTypeInfo[] commandTypeInfos;
	private BufferedReader inputReader;
	private PrintStream outputStream;
	private Scanner scanner;
	
	public CommandScanner(CommandTypeInfo[] commandTypes, BufferedReader inputReader) {
		this.commandTypeInfos = commandTypes;
		this.inputReader = inputReader;
		this.scanner = new Scanner(this.inputReader);		//TODO closing scanner
	}
	
	public Command next() {
		System.out.println("Gib den nächsten Command ein:");
		String commandName = scanner.next();
		commandName.toLowerCase();
		switch(commandName) {
		case "help":
			return new Command(commandTypeInfos[0], null);	//instead of MyFavoriteCommandType.HELP
		case "exit":
			return new Command(commandTypeInfos[1], null);
		case "addi":
			int param1 = scanner.nextInt();
			int param2 = scanner.nextInt();
			return new Command(commandTypeInfos[2], new Object[] {param1, param2});
		case "addf":
			float parameter1 = scanner.nextFloat();
			float parameter2 = scanner.nextFloat();
			return new Command(commandTypeInfos[3], new Object[] {parameter1, parameter2});
		case "echo":
			String string = scanner.next();
			int times = scanner.nextInt();
			return new Command(commandTypeInfos[4], new Object[] {string, times});
		default:
			return null;
		}
	}
}
