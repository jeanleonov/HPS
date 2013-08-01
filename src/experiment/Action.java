package experiment;

import java.util.Vector;

public class Action {
	
	private Vector<Integer> zonesNumbers;
	private ZoneCommand command;
	
	public Action(Vector<Integer> zonesNumbers, ZoneCommand command){
		this.zonesNumbers = zonesNumbers;
		this.command = command;
	}

	public Vector<Integer> getZonesNumbers() {
		return zonesNumbers;
	}

	public void setZonesNumbers(Vector<Integer> zonesNumbers) {
		this.zonesNumbers = zonesNumbers;
	}

	public ZoneCommand getCommand() {
		return command;
	}

	public void setCommand(ZoneCommand command) {
		this.command = command;
	}
}
