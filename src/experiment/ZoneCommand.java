package experiment;

import java.io.Serializable;

public class ZoneCommand implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public enum Type{
		ADD_RESOURCES,
		ADD_INDIVIDUALS,
		MULTIPLY_RESOURCES,
		MULTIPLY_INDIVIDUALS;
	}

	Type type;
	Object commandContent;

	public ZoneCommand(Type type, Object commandContent) {
		this.type = type;
		this.commandContent = commandContent;
	}

	public Type getType() {
		return type;
	}

	public Object getCommandContent() {
		return commandContent;
	}
}
