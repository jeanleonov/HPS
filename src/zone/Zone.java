package zone;

import individual.Individual;

import java.util.Vector;

import experiment.ExperimentBehaviour;

import jade.core.Agent;

public class Zone extends Agent {

	private static final long serialVersionUID = 1L;

	private Vector<Individual> males;
	private Vector<Individual> females;
	private Vector<Individual> immatures;
	private Vector<Individual> yarlings;
	
	private Vector<Individual> strangers;

	public Zone(){
		
	}
	
	@Override
	protected void setup(){
		// TODO
		addBehaviour(new ZoneBehaviour(this));
	}
	
	
	// TODO
}
