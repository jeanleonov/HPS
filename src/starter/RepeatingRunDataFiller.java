package starter;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import distribution.ZoneDistribution;
import settings.Param;
import settings.PosterityParentsPair;
import settings.PosterityResultPair;
import utils.parser.Parser;
import experiment.ZoneSettings;
import experiment.individual.genotype.Genotype;
import experiment.scenario.Rule;
import experiment.scenario.Scenario;

public class RepeatingRunDataFiller {
	
	private Reader	viabilityReader,
					posterityReader, 
					movePossibilitiesReader,
					scenarioReader,
					distributionInfoReader;
	private List<ZoneSettings> zones = new ArrayList<>();
	private List<Rule> rules;
	private double capacityMultilpier;
	
	private static final String RESOURCES = "-RESOURCES-";
	
	public RepeatingRunDataFiller(
			Reader viabilityReader, 
			Reader posterityReader,
			Reader movePossibilitiesReader,
			Reader scenarioReader,
			Reader distributionInfoReader,
			double capacityMultilpier) throws Exception {
		this.viabilityReader = viabilityReader;
		this.posterityReader = posterityReader;
		this.movePossibilitiesReader = movePossibilitiesReader;
		this.scenarioReader = scenarioReader;
		this.distributionInfoReader = distributionInfoReader;
		this.capacityMultilpier = capacityMultilpier;
		fillZonesMap();
		fillViability();
		fillPosterity();
		fillScenario();
		fillStartDistribution();
	}
	
	public List<ZoneSettings> getZonesSettings() {
		return zones;
	}

	public Scenario getScenario() {
		return new Scenario(rules);
	}

	private void fillViability() throws NumberFormatException, Exception {
		BufferedReader reader = new BufferedReader(viabilityReader);
		String headerLine = reader.readLine();
		List<Genotype> genotypes = parseGenotypesLine(headerLine, 3);
		String line;
		HashMap<Genotype, Float[]> viabilityTable = new HashMap<>();	// TODO
		for (ZoneSettings zoneSettings : zones)							// #stub
			zoneSettings.setViabilityTable(viabilityTable);				// Now all zones have same viability settings
		while ((line = reader.readLine()) != null)
			parseViabilityLine(line, genotypes, viabilityTable);
	}
	
	private void parseViabilityLine(
					String line, 
					List<Genotype> genotypes, 
					HashMap<Genotype, Float[]> viabilityTable) throws Exception
	{
		String[] lineCells = line.split(";");
		try {
			for(int i = 0; i < genotypes.size(); i++) {
				Param param = Param.getByKey(Integer.parseInt(lineCells[2], 10));
				Float[] paramValues = viabilityTable.get(genotypes.get(i));
				if(paramValues == null) {
					paramValues = new Float[Param.values().length];
					viabilityTable.put(genotypes.get(i), paramValues);
				}
				paramValues[param.ordinal()] = Float.parseFloat(lineCells[i + 3]);
			}
		} catch (NumberFormatException | IndexOutOfBoundsException exception) {
			throw getViabilityException(exception);
		}
	}

	private void fillPosterity() throws Exception {
		BufferedReader reader = new BufferedReader(posterityReader);
		String headerLine = reader.readLine();
		List<Genotype> genotypes = parseGenotypesLine(headerLine, 2);
		String line;
		HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable;
		posterityTable = new HashMap<>();						// TODO
		for (ZoneSettings zoneSettings : zones)					// #stub
			zoneSettings.setPosterityTable(posterityTable);		// Now all zones have same posterity settings
		while ((line = reader.readLine()) != null)
			parsePosterityLine(line, genotypes, posterityTable);
	}
	
	private void parsePosterityLine(
					String line, 
					List<Genotype> genotypes,
					HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable) throws Exception
	{
		String[] lineCells = line.split(";");
		try {
			for(int i = 0; i < genotypes.size(); i++) {
				Genotype mother = Genotype.getGenotype(lineCells[0]);
				Genotype father = Genotype.getGenotype(lineCells[1]);
				float value = Float.parseFloat(lineCells[i + 2]);
				if (value == 0)
					continue;
				PosterityParentsPair pair = new PosterityParentsPair(mother, father);
				ArrayList<PosterityResultPair> resultPairs = posterityTable.get(pair);
				if (!posterityTable.containsKey(pair)) {
					resultPairs = new ArrayList<>();
					posterityTable.put(pair, resultPairs);
				}
				resultPairs.add(new PosterityResultPair(genotypes.get(i), value));
			}
		} catch (NumberFormatException | IndexOutOfBoundsException exception) {
			throw getPosterityException(exception);
		}
	}

	private List<Genotype> parseGenotypesLine(String line, int startFrom) throws Exception {
		String[] lineCells = line.split(";");
		List<Genotype> genotypes = new ArrayList<>(lineCells.length-startFrom);
		for (int i = startFrom; i < lineCells.length; i++)
			genotypes.add(Genotype.getGenotype(lineCells[i]));
		return genotypes;
	}
	
	private void fillZonesMap() throws Exception {
		BufferedReader reader = new BufferedReader(movePossibilitiesReader);
		String headerLine = reader.readLine();
		List<String> zoneNames = parseZonesLine(headerLine, 1);
		initZonesSettingsSet(zoneNames);
		int zoneNumber=0;
		while (true) {
			String line = reader.readLine();
			if (line == null)
				throw new Exception("Wrong content of file with zones map description.");
			if (line.startsWith(RESOURCES)) {
				parseResourcesLine(line);
				break;
			}
			parseMovePossibilitiesLine(line, zoneNumber);
			zoneNumber++;
		}
	}
	
	private List<String> parseZonesLine(String line, int startFrom) throws Exception {
		String[] lineCells = line.split(";");
		List<String> zoneNames = new ArrayList<>(lineCells.length-startFrom);
		for (int i = startFrom; i<lineCells.length; i++)
			zoneNames.add(lineCells[i]);
		return zoneNames;
	}
	
	private void initZonesSettingsSet(List<String> zoneNames) {
		for (String name : zoneNames)
			zones.add(new ZoneSettings(name));
	}
	
	private void parseResourcesLine(String line) throws Exception {
		String[] lineCells = line.split(";");
		try {
			for (int i=1; i<lineCells.length; i++) {
				Double capacity = Double.parseDouble(lineCells[i]);
				capacity *= this.capacityMultilpier;
				ZoneSettings zoneSettings = zones.get(i-1);
				zoneSettings.setCapacity(capacity);
			}
		} catch (NumberFormatException | IndexOutOfBoundsException exception) {
			throw getMapException(exception);
		}
	}
	
	private void parseMovePossibilitiesLine(String line, int zoneNumber) throws Exception {
		String[] lineCells = line.split(";");
		try {
			String zoneName = lineCells[0];
			ZoneSettings zoneSettings = zones.get(zoneNumber);
			Map<String, Double> movePossibilitiesTable = new HashMap<>();
			zoneSettings.setMovePossibilitiesTable(movePossibilitiesTable);
			if (!zoneName.equals(zoneSettings.getZoneName()))
				throw getMapException();
			for (int i=1; i<lineCells.length; i++)
				if (i-1 != zoneNumber) {
					Double moveProbability = Double.parseDouble(lineCells[i]);
					String neighborName = zones.get(i-1).getZoneName();
					movePossibilitiesTable.put(neighborName, moveProbability);
				}
		} catch (NumberFormatException | IndexOutOfBoundsException exception) {
			throw getMapException(exception);
		}
	}
	
	private void fillScenario() throws Exception {
		Parser.ReInit(scenarioReader);
		rules = Parser.ruleList();
	}
	
	private void fillStartDistribution() throws Exception {
		Parser.ReInit(distributionInfoReader);
		Map<String, ZoneDistribution> distributions = Parser.zoneDistributions();
		for (ZoneSettings zoneSettings : zones) {
			ZoneDistribution distribution = distributions.get(zoneSettings.getZoneName());
			if (distribution == null)
				throw getDistributionException();
			zoneSettings.setStartDistribution(distribution);
		}
	}
	
	private Exception getViabilityException(Exception e) {
		String myMessage = "Wrong content of file with viability settings. \n";
		String stack = Shared.printStack(e);
		return new Exception(myMessage + stack);
	}
	
	private Exception getPosterityException(Exception e) {
		String myMessage = "Wrong content of file with posterity settings. \n";
		String stack = Shared.printStack(e);
		return new Exception(myMessage + stack);
	}
	
	private Exception getMapException(Exception e) {
		String myMessage = "Wrong content of file with zones map description. \n";
		String stack = Shared.printStack(e);
		return new Exception(myMessage + stack);
	}
	
	private Exception getMapException() {
		String myMessage = "Wrong content of file with zones map description. \n";
		return new Exception(myMessage);
	}
	
	private Exception getDistributionException() {
		String myMessage = "Wrong content of initiation file. \n";
		return new Exception(myMessage);
	}
}