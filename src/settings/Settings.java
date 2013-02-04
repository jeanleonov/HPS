package settings;

import genotype.Genotype;
import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Settings implements Vocabulary {

	static private HashMap<genotype.Genotype, ArrayList<ViabilityPair>> viabilityTable = new HashMap<genotype.Genotype, ArrayList<ViabilityPair>>();
	static private HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable = new HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>>();
	static private HashMap<Integer, HashMap<Integer, Double>> movePosibilitiesTable = new HashMap<Integer, HashMap<Integer, Double>>();
	
	static private HashMap<Integer, AID> zoneTable = new HashMap<Integer, AID>();

	public static void init(
				HashMap<genotype.Genotype, ArrayList<ViabilityPair>> viability,
				HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterity,
				HashMap<Integer, HashMap<Integer, Double>> movePosibilities) {
		viabilityTable = viability;
		posterityTable = posterity;
		movePosibilitiesTable = movePosibilities;
	}
	
	// it's for Individual
	static public ArrayList<ViabilityPair> getViabilitySettings (Genotype indivGenotype){
		// TODO process NullPointer or implement Singleton
		return viabilityTable.get(indivGenotype);
	}
	
	// it's for Female
	static public ArrayList<PosterityResultPair> getPosteritySettings (Genotype motherGenotype, Genotype fatherGenotype){
		// TODO process NullPointer or implement Singleton
		return posterityTable.get(new PosterityParentsPair(motherGenotype, fatherGenotype));
	}
	
	// it's for Experiment
	static public void updateZoneTable(Vector<AID> zonesAIDs){
		// TODO process NullPointer or implement Singleton
		zoneTable.clear();
		int i=0;
		for (AID aid : zonesAIDs)
			zoneTable.put(i++, aid);
	}

	// it's for Individual
	static public HashMap<Integer, Double> getMovePosibilitiesFrom(Integer zoneNumber){
		// TODO process NullPointer or implement Singleton
		return movePosibilitiesTable.get(zoneNumber);
	}
	
	// it's for Zone
	static public AID getZoneAID(Integer zoneNumber){
		// TODO process NullPointer or implement Singleton
		return zoneTable.get(zoneNumber);
	}
}