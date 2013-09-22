package experiment.scenario;

import java.util.List;

public class Action {
	
	private List<String> zonesNames;
	private ZoneCommand command;
	
	public Action(List<String> zonesNames, ZoneCommand command){
		this.zonesNames = zonesNames;
		this.command = command;
	}

	public List<String> getZonesNames() {
		return zonesNames;
	}

	public void setZonesNames(List<String> zonesNames) {
		this.zonesNames = zonesNames;
	}

	public ZoneCommand getCommand() {
		return command;
	}

	public void setCommand(ZoneCommand command) {
		this.command = command;
	}
}
