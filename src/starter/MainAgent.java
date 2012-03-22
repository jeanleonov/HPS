package starter;

import experiment.*;
import jade.core.Agent;

public class MainAgent extends Agent{
	
	private static final long serialVersionUID = 1L;
	
	// TODO

	@Override
	protected void setup(){
		// TODO
		addBehaviour(new Initiator());	
	}
	
	public String getSource(){
		return (String)getArguments()[0];
	}
	
	public Scenario getScenario(){
		return (Scenario)getArguments()[1];
	}
}
