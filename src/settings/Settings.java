package settings;

import experiment.individual.genotype.Genotype;

import java.util.ArrayList;
import java.util.HashMap;

public class Settings implements Vocabulary {

	static private HashMap<Genotype, ArrayList<ViabilityPair>> viabilityTable = new HashMap<>();
	static private HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable = new HashMap<>();
	static private HashMap<Integer, HashMap<Integer, Double>> movePosibilitiesTable = new HashMap<>();

	public static void init(
				HashMap<Genotype, ArrayList<ViabilityPair>> viability,
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

	// it's for Individual
	static public HashMap<Integer, Double> getMovePosibilitiesFrom(Integer zoneNumber){
		// TODO process NullPointer or implement Singleton
		return movePosibilitiesTable.get(zoneNumber);
	}
}