package zone;

import genotype.Genotype;

import java.util.HashMap;
import java.util.Vector;

import distribution.GenotypeDistribution;
import distribution.ZoneDistribution;

import jade.core.AID;
import jade.core.Agent;

public class Zone extends Agent {

	private static final long serialVersionUID = 1L;

	Vector<AID> males;
	Vector<AID> females;
	Vector<AID> immatures;
	Vector<AID> yarlings;
	
	int resources;
	
	
	// private Vector<Individual> strangers;
	
	@Override
	protected void setup(){
		ZoneDistribution zoneDistribution = (ZoneDistribution)getArguments()[0];
		createIndividuals(zoneDistribution);
		addBehaviour(new ZoneBehaviour());
	}
	
	private void createIndividuals(ZoneDistribution zoneDistribution) {
		HashMap<Genotype, GenotypeDistribution> genotypeDistribution = zoneDistribution.getGenotypeDistributions();
		//genotypeDistribution.
	}

	int getIndividualsNumber(){
		return males.size() + females.size() + immatures.size();
	}
	
	float getAttractivness(){
		float attractivness = (float) resources / (float) getIndividualsNumber(); 
		return attractivness;
	}
	
	Vector<AID> getIndividuals(){
		Vector<AID> individuals = new Vector<AID>(getIndividualsNumber());
		individuals.addAll(males);
		individuals.addAll(females);
		individuals.addAll(immatures);
		return individuals;
	}
}
