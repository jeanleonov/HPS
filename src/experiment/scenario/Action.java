package experiment.scenario;

import java.util.List;

public class Action {
	
	private List<Integer> zonesNumbers;
	private ZoneCommand command;
	
	public Action(List<Integer> zonesNumbers, ZoneCommand command){
		this.zonesNumbers = zonesNumbers;
		this.command = command;
	}

	public List<Integer> getZonesNumbers() {
		return zonesNumbers;
	}

	public void setZonesNumbers(List<Integer> zonesNumbers) {
		this.zonesNumbers = zonesNumbers;
	}

	public ZoneCommand getCommand() {
		return command;
	}

	public void setCommand(ZoneCommand command) {
		this.command = command;
	}
}
