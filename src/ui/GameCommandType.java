package ui;

public enum GameCommandType implements CommandTypeInfo {
	/*
	 * TODO create a reflection class maybe that calls board if needed for nextStep
	 * and simply prints help() or exits the System depending in the command
	 */
	HELP("help", "  * list all commands", "help"),
	EXIT("exit", "  * exit program", "exitSystem"),
	ALL("all" , "  * no idea what this does yet", "all"),			//need to create?
	DOWN_LEFT("down_left", "  * MasterSquirrel goes down left", "nextStep"),
	DOWN("down", "  * MasterSquirrel goes down", "nextStep"),
	DOWN_RIGHT("down_right", "  * MasterSquirrel goes down right", "nextStep"),
	LEFT("left", "  * MasterSquirrel goes left", "nextStep"),
	RIGHT("right", "  * MasterSquirrel goes right", "nextStep"),
	UP_LEFT("up_left", "  * MasterSquirrel goes up left", "nextStep"),
	UP("up", "  * MasterSquirrel goes up", "nextStep"),
	UP_RIGHT("up_right", "  * MasterSquirrel goes up right", "nextStep"),
	MASTER_ENERGY("master_energy", "  * get energy of MasterSquirrel", "getMasterSquirrelEnergy"),		//need to create
	SPAWN_MINI("spawn_mini", "  * spawn a MiniSquirrel", "spawnMiniSquirrel"),
	DO_NOTHING("none", "  * MasterSquirrel doesn't move for a turn", "nextStep")
	;
	
	private String name;
	private String helpText;
	private String methodToCall;
	private Class<?>[] paramTypes = null;
	
	private GameCommandType(String name, String helpText, String method) {
		this.name = name;
		this.helpText = helpText;
		this.methodToCall = method;
	}
	
	private GameCommandType(String name, String helpText, String method, Class<?>[] param) {
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
