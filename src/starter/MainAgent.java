package starter;

import experiment.*;
import jade.core.Agent;

public class MainAgent extends Agent{
	
	private static final long serialVersionUID = 1L;
	
	// TODO

	@Override
	protected void setup(){
		// TODO
		
		Object[] args = getArguments();
		addBehaviour(new Initiator((String)args[0]));			// implement Initiator and define constructor args
	}
	

}
