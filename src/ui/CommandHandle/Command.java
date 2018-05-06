package ui.commandhandle;

public final class Command {	//immutable?
	
	private final CommandTypeInfo commandType;
	private final Object[] params;
	
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
	
	@Override
	public String toString() {
		if(params.length == 0) {
			return this.commandType.getName();
		} else {
			StringBuilder s = new StringBuilder("");
			for(int i = 0; i < params.length; i++) {
				s.append(params[i].toString() + " ");
			}
			return this.commandType.getName() + " " + s.toString();
		}
	}
}
