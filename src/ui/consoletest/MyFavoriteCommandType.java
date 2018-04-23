package ui.consoletest;

import ui.CommandHandle.CommandTypeInfo;

public enum MyFavoriteCommandType implements CommandTypeInfo {
	HELP("help", "  * list all commands", "help"),
	EXIT("exit", "  * exit program", "exitSystem"),
	ADDI("addi", "<param1>  <param2>   * simple integer add", Integer.class, Integer.class, "addIntegers"),
	ADDF("addf", "<param1>  <param2>   * simple float add", float.class, float.class, "addFloats"),
	ECHO("echo", "<param1>  <param2>   * echos param1 string param2 times", String.class, int.class, "echo")
	;
	
	private String name;
	private String helpText;
	private String methodToCall;
	private Class<?> [] paramTypes;
	
	private MyFavoriteCommandType(String name, String helpText, String methodToCall) {
		this.name = name;
		this.helpText = helpText;
		this.methodToCall = methodToCall;
	}
	
	private MyFavoriteCommandType(String name, String helpText, Class<?> param1, Class<?> param2, String methodToCall) {
		this.name = name;
		this.helpText = helpText;
		this.paramTypes = new Class<?>[] {param1, param2};
		this.methodToCall = methodToCall;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getHelpText() {
		return this.helpText;
	}
	
	@Override
	public String getMethodToCall() {
		return this.methodToCall;
	}

	@Override
	public Class<?>[] getParamTypes() {
		return this.paramTypes;
	}
	
	@Override
	public String toString() {
		if(paramTypes == null) {
			return this.name + this.helpText;
		} else {
			return this.name + " " + this.helpText + " param1 type: " + this.paramTypes[0] + " param2 type: " + this.paramTypes[1];
		}
	}
}
