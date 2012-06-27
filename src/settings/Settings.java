package settings;

import genotype.Genotype;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

// This agent is not really agent.
// It will be created on each node, where a experiments will be executing,
// which will can get info about viabilitySettings and posteritySettings
public class Settings extends Agent implements Vocabulary {

	private static final long serialVersionUID = 1L;

	static private HashMap<genotype.Genotype, ArrayList<ViabilityPair>> viabilityTable = new HashMap<genotype.Genotype, ArrayList<ViabilityPair>>();
	static private HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable = new HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>>();
	static private HashMap<Integer, ArrayList<Float>> movePosibilitiesTable = new HashMap<Integer, ArrayList<Float>>();
	
	static private HashMap<Integer, AID> zoneTable = new HashMap<Integer, AID>();

	@SuppressWarnings("unchecked")
	@Override
	protected void setup() {
		Object[] args = getArguments();
		viabilityTable = (HashMap<genotype.Genotype, ArrayList<ViabilityPair>>) args[0];
		posterityTable = (HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>>) args[1];
		movePosibilitiesTable = (HashMap<Integer, ArrayList<Float>>) args[2];
		confirmationOfReadiness((AID)args[3]);
	}
	
	private void confirmationOfReadiness(AID systemStarter){
		ACLMessage confirm = new ACLMessage(ACLMessage.CONFIRM);
		confirm.addReceiver(systemStarter);
		send(confirm);
	}
	
	// it's for Individual
	static public ArrayList<ViabilityPair> getViabilitySettings (Genotype indivGenotype){
		return viabilityTable.get(indivGenotype);
	}
	
	// it's for Female
	static public ArrayList<PosterityResultPair> getPosteritySettings (Genotype motherGenotype, Genotype fatherGenotype){
		return posterityTable.get(new PosterityParentsPair(motherGenotype, fatherGenotype));
	}
	
	// it's for Experiment
	static public void updateZoneTable(Vector<AID> zonesAIDs){
		zoneTable.clear();
		int i=0;
		for (AID aid : zonesAIDs)
			zoneTable.put(i++, aid);
	}

	// it's for Individual
	static public ArrayList<Float> getMovePosibilitiesFrom(Integer zoneNumber){
		return movePosibilitiesTable.get(zoneNumber);
	}
	
	// it's for Zone
	static public AID getZoneAID(Integer zoneNumber){
		return zoneTable.get(zoneNumber);
	}
}