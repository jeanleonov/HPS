package settings;

import java.util.ArrayList;
import java.util.HashMap;

import experiment.individual.genotype.Genotype;

public class Settings implements Vocabulary {

	static private HashMap<Genotype, Float[]> viabilityTable = new HashMap<>();
	static private HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable = new HashMap<>();
	static private HashMap<Integer, HashMap<Integer, Double>> movePosibilitiesTable = new HashMap<>();

	public static void init(
				HashMap<Genotype, Float[]> viability,
				HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterity,
				HashMap<Integer, HashMap<Integer, Double>> movePosibilities) {
		viabilityTable = viability;
		posterityTable = posterity;
		movePosibilitiesTable = movePosibilities;
	}
	
	// it's for Individual
	static public Float[] getViabilitySettings (Genotype indivGenotype) {
		return viabilityTable.get(indivGenotype);
	}
	
	// it's for Female
	static public ArrayList<PosterityResultPair> getPosteritySettings (Genotype motherGenotype, Genotype fatherGenotype) {
		return posterityTable.get(new PosterityParentsPair(motherGenotype, fatherGenotype));
	}

	// it's for Individual
	static public HashMap<Integer, Double> getMovePosibilitiesFrom(Integer zoneNumber) {
		return movePosibilitiesTable.get(zoneNumber);
	}
}