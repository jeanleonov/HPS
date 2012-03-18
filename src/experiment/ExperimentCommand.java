package experiment;

class ExperimentCommand {
	
	int[] zonesNumbers;
	ZoneCommand command;
	
	ExperimentCommand(int[] zonesNumbers, ZoneCommand command){
		this.zonesNumbers = zonesNumbers;
		this.command = command;
	}
}
