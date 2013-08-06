package experiment.scenario;

import java.io.IOException;

import distribution.ZoneDistribution;
import experiment.zone.Zone;

public class ScenarioExecutor {
	Zone myZone = null;
	
	public ScenarioExecutor(Zone myZone){
		this.myZone = myZone;
	}
	
	public void action(ZoneCommand command) throws IOException{
		switch(command.getType()){
			case ADD_CAPACITY:{
				// TODO
				break;
			}
			case MULTIPLY_CAPACITY:{
				// TODO
				break;
			}
			case ADD_INDIVIDUALS:{
				myZone.createIndividuals((ZoneDistribution)command.getCommandContent());
				break;
			}
			case MULTIPLY_INDIVIDUALS:{
				// TODO
				break;
			}
			default:{
				throw new IOException("Reading command error: unknown type");
			}
		}
	}
}
