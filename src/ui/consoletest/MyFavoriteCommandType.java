package ui.consoletest;

import ui.CommandTypeInfo;

public enum MyFavoriteCommandType implements CommandTypeInfo {
	HELP("help", "  * list all commands"),
	EXIT("exit", "  * exit program"),
	ADDI("addi", "<param1>  <param2>   * simple integer add", int.class, int.class),
	ADDF("addf", "<param1>  <param2>   * simple float add", float.class, float.class),
	ECHO("echo", "<param1>  <param2>   * echos param1 string param2 times", String.class, int.class)
	;
	
	private String name;
	private String helpText;
	private Class<?> [] paramTypes;
	
	private MyFavoriteCommandType(String name, String helpText) {
		this.name = name;
		this.helpText = helpText;
	}
	
	private MyFavoriteCommandType(String name, String helpText, Class<?> param1, Class<?> param2) {
		this.name = name;
		this.helpText = helpText;
		this.paramTypes = new Class<?>[] {param1, param2};
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
