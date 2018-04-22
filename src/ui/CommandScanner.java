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
		System.out.println("Gib den n�chsten Command ein:");
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
				/*
				 * the next 5 lines of code are simply called to ensure that any command will at most have 2 elements
				 * this is needed to ensure that our implemented reflection works without throwing exceptions as long as the input is valid
				 */
				String s = this.combineIntoOne(parameters, 1);	
				if(s == null) {
					return new Command(commandTypeInfos[i], parameters);
				}
				Object[] returned = {parameters[0], s};
				return new Command(commandTypeInfos[i], returned);
			}
		}
		throw new ScanException("Unknown Command");
	}
	
	public String combineIntoOne(Object[] array, int startPosition) {
		StringBuilder combined = new StringBuilder("");
		if(startPosition >= array.length) {
			return null;
		}
		for(int i = startPosition; i < array.length; i++) {
			combined.append(array[i] + " ");
		}
		return combined.toString();
	}
}