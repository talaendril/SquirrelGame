package ui;

public enum GameCommandType implements CommandTypeInfo {
	HELP("help", "  * list all commands"),
	EXIT("exit", "  * exit program"),
	ALL("all" , "  * no idea what this does yet"),
	DOWN_LEFT("down_left", "  * MasterSquirrel goes down left"),
	DOWN("down", "  * MasterSquirrel goes down"),
	DOWN_RIGHT("down_right", "  * MasterSquirrel goes down right"),
	LEFT("left", "  * MasterSquirrel goes left"),
	RIGHT("right", "  * MasterSquirrel goes right"),
	UP_LEFT("up_left", "  * MasterSquirrel goes up left"),
	UP("up", "  * MasterSquirrel goes up"),
	UP_RIGHT("up_right", "  * MasterSquirrel goes up right"),
	MASTER_ENERGY("master_energy", "  * get energy of MasterSquirrel"),
	SPAWN_MINI("spawn_mini", "  * spawn a MiniSquirrel")
	;
	
	private String name;
	private String helpText;
	
	private GameCommandType(String name, String helpText) {
		this.name = name;
		this.helpText = helpText;
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
		return null;	//nothing to return?
	}
}
