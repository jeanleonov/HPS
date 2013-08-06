package starter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import settings.PosterityParentsPair;
import settings.PosterityResultPair;
import settings.ViabilityPair;
import settings.Vocabulary.Convertor;
import settings.Vocabulary.Param;
import utils.parser.Parser;
import distribution.ExperimentDistribution;
import distribution.ZoneDistribution;
import experiment.individual.genotype.Genotype;
import experiment.scenario.Rule;
import experiment.scenario.Scenario;

public class DataFiller {
	
	private Reader	viabilityReader,
					posterityReader, 
					movePossibilitiesReader,
					scenarioReader,
					experimentInfoReader;
	private ExperimentDistribution experimentDistribution;
	private HashMap<Genotype, ArrayList<ViabilityPair>> viabilityTable = new HashMap<>();
	private HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable = new HashMap<>();
	private HashMap<Integer, HashMap<Integer, Double>> movePosibilitiesTable = new HashMap<>();
	private List<Rule> rules;
	private int zoneMultiplier;
	
	public DataFiller(
			Reader viabilityReader, 
			Reader posterityReader,
			Reader movePossibilitiesReader,
			Reader scenarioReader,
			Reader experimentInfoReader,
			int zoneMultiplier) throws Exception{
		this.zoneMultiplier = zoneMultiplier;
		this.viabilityReader = viabilityReader;
		this.posterityReader = posterityReader;
		this.movePossibilitiesReader = movePossibilitiesReader;
		this.scenarioReader = scenarioReader;
		this.experimentInfoReader = experimentInfoReader;
		viabilityFill();
		posterityFill();
		scenarioFill();
		experimentFill();
		movePossibilitiesFill();
		
	}
	
	public HashMap<Genotype, ArrayList<ViabilityPair>> getViabilityTable() {
		return viabilityTable;
	}

	public HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> getPosterityTable() {
		return posterityTable;
	}

	public HashMap<Integer, HashMap<Integer, Double>> getMovePosibilitiesTable() {
		return movePosibilitiesTable;
	}

	public ExperimentDistribution getExperimentDistribution() {
		return experimentDistribution;
	}

	public Scenario getScenario() {
		return new Scenario(rules);
	}

	private void viabilityFill() throws NumberFormatException, Exception {
		List<Genotype> genotypeOrder = new LinkedList<>();
		BufferedReader reader = new BufferedReader(viabilityReader);
		String orderLine = reader.readLine();
		orderFill(genotypeOrder, orderLine, 3);
		String str;
		while ((str = reader.readLine()) != null) {
			String[] strArr = str.split(";");
			if (strArr.length < genotypeOrder.size() + 3)
				continue;
			for(int i = 0; i < genotypeOrder.size(); i++) {
				Param param = Convertor.keyToParam(Integer.parseInt(strArr[2], 10));
				if(!viabilityTable.containsKey(genotypeOrder.get(i)))
					viabilityTable.put(genotypeOrder.get(i), new ArrayList<ViabilityPair>());
				ArrayList<ViabilityPair> arr = viabilityTable.get(genotypeOrder.get(i));
				arr.add(new ViabilityPair(Float.parseFloat(strArr[i + 3]), param));
			}
		}
	}
	
	public void printViabilityTable() {
		for(Genotype g : viabilityTable.keySet()) {
			System.out.print(g + " - ");
			for(ViabilityPair pair : viabilityTable.get(g))
				System.out.print(pair.getValue() + ", ");
			System.out.println();
		}
	}

	private void posterityFill() throws Exception {
		List<Genotype> genotypeOrder = new LinkedList<>();
		BufferedReader reader = new BufferedReader(posterityReader);
		String orderLine = reader.readLine();
		orderFill(genotypeOrder, orderLine, 2);
		String str;
		while ((str = reader.readLine()) != null) {
			String[] strArr = str.split(";");
			if (strArr.length < genotypeOrder.size() + 2)
				continue;
			for(int i = 0; i < genotypeOrder.size(); i++) {
				Genotype genotype1 = Genotype.getGenotype(strArr[0]),
						 genotype2 = Genotype.getGenotype(strArr[1]);
				float value = Float.parseFloat(strArr[i + 2]);
				if (value == 0)
					continue;
				PosterityParentsPair pair = new PosterityParentsPair(genotype1, genotype2);
				if (!posterityTable.containsKey(pair))
					posterityTable.put(pair, new ArrayList<PosterityResultPair>());
				posterityTable.get(pair).add(new PosterityResultPair(genotypeOrder.get(i), value));
			}
		}
	}
	
	private void movePossibilitiesFill() throws IOException {
		if (movePossibilitiesReader==null) {
			defaultMovePossibilitiesFill();
			return;
		}
		BufferedReader reader = new BufferedReader(movePossibilitiesReader);
		String zonePossibilities;
		for(int i = 0; (zonePossibilities = reader.readLine()) != null && i < getZonesNumber(); i++) {
			String[] travelCostsString = zonePossibilities.split(" ");
			HashMap<Integer, Double> travelCosts = new HashMap<>();
			if(travelCostsString.length != 0) {			
				double travelCost = Double.parseDouble(travelCostsString[0]);
				travelCosts.put(-1, travelCost);
			}
			for(int j = 1; j < travelCostsString.length; j++) {
				double travelCost = Double.parseDouble(travelCostsString[j]);
				if((travelCost != 0) && (i != (j - 1)))
					travelCosts.put(j - 1, travelCost);
			}
			if(travelCosts.isEmpty() != true)
				movePosibilitiesTable.put(i, travelCosts);
		}
	}
	
	private int getZonesNumber() {
		return getExperimentDistribution().getZoneDistributions().size();
	}
	
	private void defaultMovePossibilitiesFill() {
		for (int i=0; i<experimentDistribution.getZoneDistributions().size(); i++) {
			HashMap<Integer, Double> travelCosts = new HashMap<Integer, Double>();
			for (int j=0; j<experimentDistribution.getZoneDistributions().size(); j++)
				travelCosts.put(j, 1d);
			movePosibilitiesTable.put(i, travelCosts);
		}
	}
	
	public void printPosterityTable() {
		for(PosterityParentsPair parents : posterityTable.keySet()) {
			System.out.print(parents.getMale() + "+" + parents.getFemale() + ": ");
			for(PosterityResultPair pair : posterityTable.get(parents))
				System.out.print(pair.getGenotype() + "(" + pair.getProbability() + ") ,");
			System.out.println();
		}
	}

	private void orderFill(List<Genotype> arr, String str, int startFrom) throws Exception {
		String[] strArr = str.split(";");
		for (int i = startFrom; i < strArr.length; i++)
			arr.add(Genotype.getGenotype(strArr[i]));
	}
	
	private void scenarioFill() {
		Parser parser = new Parser(scenarioReader);
		rules = parser.readRules();
	}
	
	private void experimentFill() throws Exception {
		BufferedReader experimentInfoReader = new BufferedReader(this.experimentInfoReader);
		String res = new String();
		String c = new String(experimentInfoReader.readLine());
		while(c != null) {
			res += c;
			c = experimentInfoReader.readLine();
		}
		experimentDistribution = ExperimentDistribution.parseExperiment(res);
		multiplyZonesInDistribution();
	}
	
	private void multiplyZonesInDistribution() {
		List<ZoneDistribution> zoneDistributions = experimentDistribution.getZoneDistributions();
		int oldNumberOfZones = zoneDistributions.size();
		if (zoneMultiplier<1)
			return;					// ignore invalid input
		for (int i=0; i<oldNumberOfZones*(zoneMultiplier-1); i++)
			experimentDistribution.addZoneDistribution(zoneDistributions.get(i%oldNumberOfZones));
	}
}