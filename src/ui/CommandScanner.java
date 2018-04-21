package ui;

import java.io.BufferedReader;
//import java.io.PrintStream;
import java.util.Scanner;

import exceptions.ScanException;

public class CommandScanner {
	
	private CommandTypeInfo[] commandTypeInfos;
	private BufferedReader inputReader;
	private Scanner scanner;
	//private PrintStream outputStream;
	
	public CommandScanner(CommandTypeInfo[] commandTypes, BufferedReader inputReader) {
		this.commandTypeInfos = commandTypes;
		this.inputReader = inputReader;
		this.scanner = new Scanner(this.inputReader);
	}
	
	public Command next() throws ScanException {
		System.out.println("Gib den nächsten Command ein:");
		String input = scanner.nextLine();
		String[] tokens = input.split("\\s+");
		if(tokens == null) {
			return null;
		}
		for(int i = 0; i < commandTypeInfos.length; i++) {
			if(tokens[0].toLowerCase().equals(commandTypeInfos[i].getName())) {
				Object[] parameters = new Object[tokens.length - 1];
				for(int j = 1; j < tokens.length; j++) {
					parameters[j - 1] = tokens[j];
				}
				return new Command(commandTypeInfos[i], parameters);
			}
		}
		throw new ScanException("Unknown Command");
	}
}