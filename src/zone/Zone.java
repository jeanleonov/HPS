package zone;

import individual.Individual;

import java.util.Vector;

import experiment.ExperimentBehaviour;

import jade.core.AID;
import jade.core.Agent;

public class Zone extends Agent {

	private static final long serialVersionUID = 1L;

	Vector<AID> males;
	Vector<AID> females;
	Vector<AID> immatures;
	Vector<AID> yarlings;
	
	int resources;
	
	private Vector<Individual> strangers;

	public Zone(){
		
	}
	
	@Override
	protected void setup(){
		// TODO
		addBehaviour(new ZoneBehaviour());
	}
	
	int getIndividualsNumber(){
		return males.size() + females.size() + immatures.size();
	}
	
	float getAttractivness(){
		
		return 0;
	}
	
	Vector<AID> getIndividuals(){
		Vector<AID> individuals = new Vector<AID>(getIndividualsNumber());
		individuals.addAll(males);
		individuals.addAll(females);
		individuals.addAll(immatures);
		return individuals;
	}
	// TODO
}
