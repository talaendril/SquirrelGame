package ui.CommandHandle;

public enum GameCommandType implements CommandTypeInfo {
	HELP("help", "\t\t  * list all commands", "help"),
	EXIT("exit", "\t\t  * exit program", "exitSystem"),
	ALL("all" , "\t\t  * has no use yet", "all"),
	MOVE("move", " <vector>\t  * MasterSquirrels moves in the direction of vector", "move", MoveCommand.class),
	MASTER_ENERGY("master_energy", "\t  * get energy of MasterSquirrel", "getMasterSquirrelEnergy"),
	SPAWN_MINI("spawn_mini", " <E>\t  * spawn a MiniSquirrel initialized with E energy", "spawnMiniSquirrel", int.class)
	;
	
	private String name;
	private String helpText;
	private String methodToCall;
	private Class<?>[] paramTypes = null;
	
	private GameCommandType(String name, String helpText, String method, Class<?>...param) {
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
