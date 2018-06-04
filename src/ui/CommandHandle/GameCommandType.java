package ui.commandhandle;

public enum GameCommandType implements CommandTypeInfo {
	MOVE("move", " <vector>\t  * MasterSquirrels moves in the direction of vector", "move", MoveCommand.class),
	SPAWN_MINI("spawn_mini", " <E>\t  * spawn a MiniSquirrel initialized with E energy", "spawnMiniSquirrel", int.class),
	IMPLODE_MINI("implode_mini", "<E>\t implodes the specified MiniSquirrel with radius E", "implode"),
	NOTHING("nothing", "\t  * do nothing", "doNothing")
	;
	
	private String name;
	private String helpText;
	private String methodToCall;
	private Class<?>[] paramTypes = null;
	
	GameCommandType(String name, String helpText, String method, Class<?>...param) {
		this.name = name;
		this.helpText = helpText;
		this.methodToCall = method;
		this.paramTypes = param;
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
	public String getMethodToCall() {
		return this.methodToCall;
	}
	
	@Override
	public String toString() {
		return this.name + this.helpText;
		//TODO add paramtypes if ever needed
	}
}
