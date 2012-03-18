package experiment;

public class ZoneCommand {
	
	final static public short 	ADD_RESOURCES = 1,
								ADD_INDIVIDUALS = 2,
								MULTIPLY_RESOURCES = 3,
								MULTIPLY_INDIVIDUALS = 4;

	short type;
	Object commandContent;

	public ZoneCommand(short type, Object commandContent) {
		super();
		this.type = type;
		this.commandContent = commandContent;
	}

	public short getType() {
		return type;
	}

	public Object getCommandContent() {
		return commandContent;
	}
}
