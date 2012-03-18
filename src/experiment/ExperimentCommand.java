package experiment;

import java.io.Serializable;

class ExperimentCommand implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	int[] zonesNumbers;
	ZoneCommand command;
	
	ExperimentCommand(int[] zonesNumbers, ZoneCommand command){
		this.zonesNumbers = zonesNumbers;
		this.command = command;
	}
}
