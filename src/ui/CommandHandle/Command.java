package ui.CommandHandle;

public final class Command {	//immutable?
	
	private CommandTypeInfo commandType;
	private Object[] params;
	
	public Command(CommandTypeInfo type, Object...params) {
		this.commandType = type;
		this.params = params;
	}

	public CommandTypeInfo getCommandType() {
		return this.commandType;
	}

	public Object[] getParams() {
		return params;
	}
}
