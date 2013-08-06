package experiment.scenario;


public class ZoneCommand {

	public enum Type{
		ADD_CAPACITY,
		ADD_INDIVIDUALS,
		MULTIPLY_CAPACITY,
		MULTIPLY_INDIVIDUALS;
	}

	private Type type;
	private Object commandContent;

	public ZoneCommand(Type type, Object commandContent) {
		this.type = type;
		this.commandContent = commandContent;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Object getCommandContent() {
		return commandContent;
	}

	public void setCommandContent(Object commandContent) {
		this.commandContent = commandContent;
	}
}
