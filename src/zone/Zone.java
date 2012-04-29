package zone;

import genotype.Genome;
import genotype.Genotype;

import java.util.Vector;

import zone.Pair;
import zone.ZoneBehaviour;

import distribution.GenotypeAgeNumberTrio;
import distribution.ZoneDistribution;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Zone extends Agent {

	private static final long serialVersionUID = 1L;

	private static final String INDIVIDUAL_CLASS_PATH = "individual.Individual";
	
	// DMY: for regulating competitiveness factor in attractivness counting
	private static final double feedingCoeficient = 1;
	
	Vector<AID> males = new Vector<AID>();
	Vector<AID> females = new Vector<AID>();
	Vector<AID> immatures = new Vector<AID>();
	Vector<AID> yearlings = new Vector<AID>();
	
	AID statisticDispatcher;
	
	int experimentId;
	int zoneId;
	
	int resources;
	double totalCompetitiveness;
	int iteration = -1;
	
	private int individualCounter = 0;
	
	private Vector<Pair<AID, Double>> neighbourZones = new Vector<Pair<AID, Double>>();
	
	// private Vector<Individual> strangers;
	
	@Override
	protected void setup(){
		ZoneDistribution zoneDistribution = (ZoneDistribution)getArguments()[0];
		experimentId = (Integer)getArguments()[1];
		zoneId = (Integer)getArguments()[2];
		statisticDispatcher = (AID)getArguments()[3];
		
	//	System.out.println("Zone " + zoneId + " in Experiment " + experimentId + " ready");		#lao
		
		createIndividuals(zoneDistribution);
		individualCounter = 0;
		addBehaviour(new ZoneBehaviour());
	}
	
	public void createIndividuals(ZoneDistribution zoneDistribution) {
		Vector<GenotypeAgeNumberTrio> gants = zoneDistribution.getGenotypeDistributions();
		for (GenotypeAgeNumberTrio gant : gants){
			createIndividualsByGant(gant);
		}
	}

	private void createIndividualsByGant(GenotypeAgeNumberTrio gant) {
		for (int i = 0; i < gant.getNumber(); i++){
			createIndividual(gant.getGenotype(), gant.getAge());
		}
	}

	private void createIndividual(Genotype genotype, int age) {
		try {
			// DMY: individual must know it's zone. And anyway, it doesn't percieving attractivness now
			//Object[] parameters = {genotype, age, getAttractivness()};
			Object[] parameters = {genotype, age, this.getAID()};
			
			String agentName = getIndividualName();
			ContainerController controller = getContainerController();
			AgentController individualAgent = controller.createNewAgent(agentName, 
																		INDIVIDUAL_CLASS_PATH, 
																		parameters);
			individualAgent.start();
			addIndividualToList(agentName, genotype, age);
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	public void addIndividualToList(String agentName, Genotype genotype, int age) {
		if (age < 2) {		// TODO release check immature age from settings !!!
			immatures.add(new AID(agentName, AID.ISLOCALNAME));
		}
		else {
			if (genotype.getGender() == Genome.X){
				males.add(new AID(agentName, AID.ISLOCALNAME));
			}
			else {
				females.add(new AID(agentName, AID.ISLOCALNAME));
			}
		}
		
	}

	private String getIndividualName(){
		return "" + getLocalName() + "_Individual_" + individualCounter++;
	}
	
	int getIndividualsNumber(){
		return males.size() + females.size() + immatures.size();
	}
	
	double getAttractivness(){
		// DMY: It's desirable to return value between 0 and 1
		
		// float attractivness = (float) resources / (float) getIndividualsNumber();
		
		// DMY: my version, totalCompetitiveness is taken from individuals while feeding
		double attractivness = (double)resources / (feedingCoeficient * totalCompetitiveness); 
		if(attractivness > 1) attractivness = 1;
		
		return attractivness;
	}
	
	Vector<AID> getIndividuals(){
		Vector<AID> individuals = new Vector<AID>(getIndividualsNumber());
		individuals.addAll(males);
		individuals.addAll(females);
		individuals.addAll(immatures);
		return individuals;
	}
	
	public Vector<Pair<AID, Double>> getNeighbours(){
		return neighbourZones;
	}
	
	public void killIndividual(AID individual) {
		males.remove(individual);
		females.remove(individual);
		immatures.remove(individual);
		yearlings.remove(individual);
	}
}
