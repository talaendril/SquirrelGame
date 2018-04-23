package ui.CommandHandle;

public interface CommandTypeInfo {
	String getName();
	
	String getHelpText();
	
	String getMethodToCall();
	
	Class<?>[] getParamTypes(); 
}
