package experiment;

import jade.core.Agent;

public class Experiment extends Agent {

	private static final long serialVersionUID = 1L;
	
	private Scenario scenario;

	// TODO
	
	@Override
	protected void setup(){
		// TODO
		addBehaviour(new ExperimentBehaviour(/*???*/));			// implement ExperimentBehaviour and define constructor args
	}
	
}
