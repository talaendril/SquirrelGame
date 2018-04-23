package ui;

public enum GameCommandType implements CommandTypeInfo {
	/*
	 * TODO create a reflection class maybe that calls board if needed for nextStep
	 * and simply prints help() or exits the System depending in the command
	 */
	HELP("help", "  * list all commands", "help"),
	EXIT("exit", "  * exit program", "exitSystem"),
	ALL("all" , "  * no idea what this does yet", "all"),			//need to create?
	MOVE("move", "<param1>  * MasterSquirrels moves in the direction of param1", "move", MoveCommand.class),
	MASTER_ENERGY("master_energy", "  * get energy of MasterSquirrel", "getMasterSquirrelEnergy"),		//need to create
	SPAWN_MINI("spawn_mini", "  * spawn a MiniSquirrel", "spawnMiniSquirrel", int.class)
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
