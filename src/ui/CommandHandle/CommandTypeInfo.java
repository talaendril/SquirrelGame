package ui.commandhandle;

public interface CommandTypeInfo {
	String getName();
	
	String getHelpText();
	
	String getMethodToCall();
	
	Class<?>[] getParamTypes(); 
}
