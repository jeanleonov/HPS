package starter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import settings.Param;
import settings.PosterityParentsPair;
import settings.PosterityResultPair;
import utils.parser.ParseException;
import utils.parser.Parser;
import distribution.GenotypeAgeCountTrio;
import distribution.ZoneDistribution;
import experiment.ZoneSettings;
import experiment.individual.genotype.Genotype;
import experiment.scenario.Rule;
import experiment.scenario.Scenario;

public class DataFiller {
	
	enum InputKind {
		VIABILITY, POSTERITY, ZONES_MAP, SCENARIO, INITIATION;
	}
	
	private String	viabilityFileContent,
					posterityFileContent, 
					movePossibilityFileContent,
					scenarioFileContent,
					distributionInfoFileContent;
	private List<ZoneSettings> zones = new ArrayList<>();
	private List<Rule> rules;
	private double capacityMultilpier;
	
	public DataFiller(
			String viabilityFileContent, 
			String posterityFileContent,
			String movePossibilityFileContent,
			String scenarioFileContent,
			String distributionInfoFileContent,
			double capacityMultilpier) {
		this.viabilityFileContent = viabilityFileContent;
		this.posterityFileContent = posterityFileContent;
		this.movePossibilityFileContent = movePossibilityFileContent;
		this.scenarioFileContent = scenarioFileContent;
		this.distributionInfoFileContent = distributionInfoFileContent;
		this.capacityMultilpier = capacityMultilpier;
	}
	
	public List<ZoneSettings> getZonesSettings() {
		return zones;
	}

	public Scenario getScenario() {
		return new Scenario(rules);
	}
	
	public void read() throws IOException, ParseException {
		fillZonesMap();
		fillViability();
		fillPosterity();
		fillScenario();
		fillStartDistribution();
	}

	private void fillViability() throws NumberFormatException, IOException {
		String[] lines = viabilityFileContent.split("\n");
		String headerLine = lines[0];
		List<Genotype> genotypes = parseGenotypesLine(headerLine, 3);
		HashMap<Genotype, Float[]> viabilityTable = new HashMap<>();	// TODO
		for (ZoneSettings zoneSettings : zones)							// #stub
			zoneSettings.setViabilityTable(viabilityTable);				// Now all zones have same viability settings
		for (int i=1; i<lines.length; i++)
			parseViabilityLine(lines[i], genotypes, viabilityTable);
	}
	
	private void parseViabilityLine(
					String line, 
					List<Genotype> genotypes, 
					HashMap<Genotype, Float[]> viabilityTable) throws IOException
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

	private void fillPosterity() throws IOException {
		String[] lines = posterityFileContent.split("\n");
		String headerLine = lines[0];
		List<Genotype> genotypes = parseGenotypesLine(headerLine, 2);
		HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable;
		posterityTable = new HashMap<>();						// TODO
		for (ZoneSettings zoneSettings : zones)					// #stub
			zoneSettings.setPosterityTable(posterityTable);		// Now all zones have same posterity settings
		for (int i=1; i<lines.length; i++)
			parsePosterityLine(lines[i], genotypes, posterityTable);
	}
	
	private void parsePosterityLine(
					String line, 
					List<Genotype> genotypes,
					HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable) throws IOException
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

	private List<Genotype> parseGenotypesLine(String line, int startFrom) throws IOException {
		String[] lineCells = line.split(";");
		List<Genotype> genotypes = new ArrayList<>(lineCells.length-startFrom);
		for (int i = startFrom; i < lineCells.length; i++)
			genotypes.add(Genotype.getGenotype(lineCells[i]));
		return genotypes;
	}
	
	private void fillZonesMap() throws IOException {
		String[] lines = movePossibilityFileContent.split("\n");
		String headerLine = lines[0];
		List<String> zoneNames = parseZonesLine(headerLine, 1);
		initZonesSettingsSet(zoneNames);
		int zoneNumber=0;
		while (true) {
			if (zoneNumber+1 > lines.length)
				throw new IOException("Wrong content of file with zones map description.");
			if (lines[zoneNumber+1].startsWith(Shared.RESOURCES)) {
				parseResourcesLine(lines[zoneNumber+1]);
				break;
			}
			parseMovePossibilitiesLine(lines[zoneNumber+1], zoneNumber);
			zoneNumber++;
		}
	}
	
	private List<String> parseZonesLine(String line, int startFrom) throws IOException {
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
	
	private void parseResourcesLine(String line) throws IOException {
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
	
	private void parseMovePossibilitiesLine(String line, int zoneNumber) throws IOException {
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
	
	private void fillScenario() throws IOException, ParseException {
		Parser.ReInit(new StringReader(scenarioFileContent));
		rules = Parser.ruleList();
	}
	
	private void fillStartDistribution() throws IOException {
		try {
			String[] lines = distributionInfoFileContent.split("\n");
			String headerLine = lines[0];
			List<Genotype> genotypes = new ArrayList<>();
			List<Integer> ages = new ArrayList<>();
			fillGenotypesAndAgesHeader(headerLine, genotypes, ages);
			for (int i=1; i<lines.length; i++)
				parseDistributionLine(lines[i], genotypes, ages);
		} catch (IOException e) {
			throw getDistributionException();
		}
	}
	
	private void fillGenotypesAndAgesHeader(String line, List<Genotype> genotypes, List<Integer> ages) throws IOException {
		String[] cells = line.split(";");
		for (int i=1; i<cells.length; i++) {
			String[] subStrings = cells[i].split("-");
			genotypes.add(Genotype.getGenotype(subStrings[0]));
			ages.add(Integer.parseInt(subStrings[1]));
		}
	}
	
	private void parseDistributionLine(String line, List<Genotype> genotypes, List<Integer> ages) throws IOException {
		String[] cells = line.split(";");
		ZoneSettings zoneSettings = getZoneSettings(cells[0]);
		for (int i=1; i<cells.length; i++)
			zoneSettings.setStartDistribution(getDistribution(cells, genotypes, ages));
	}
	
	private ZoneSettings getZoneSettings(String zoneName) throws IOException {
		for (ZoneSettings settings : zones)
			if (zoneName.equals(settings.getZoneName()))
				return settings;
		throw new IOException("Wrong content of file with start distribution. \n");
	}
	
	private ZoneDistribution getDistribution(String[] lineCells, List<Genotype> genotypes, List<Integer> ages) {
		ZoneDistribution distribution = new ZoneDistribution();
		for (int i=1; i<lineCells.length; i++) {
			Genotype genotype = genotypes.get(i-1);
			Integer age = ages.get(i-1);
			Integer number = Integer.parseInt(lineCells[i]);
			GenotypeAgeCountTrio gact = new GenotypeAgeCountTrio(genotype, age, number);
			distribution.addGenotypeDistribution(gact);
		}
		return distribution;
	}
	
	private IOException getViabilityException(RuntimeException exception) {
		String myMessage = "Wrong content of file with viability settings. \n";
		String stack = Shared.printStack(exception);
		return new IOException(myMessage + stack);
	}
	
	private IOException getPosterityException(RuntimeException e) {
		String myMessage = "Wrong content of file with posterity settings. \n";
		String stack = Shared.printStack(e);
		return new IOException(myMessage + stack);
	}
	
	private IOException getMapException(RuntimeException exception) {
		String myMessage = "Wrong content of file with zones map description. \n";
		String stack = Shared.printStack(exception);
		return new IOException(myMessage + stack);
	}
	
	private IOException getMapException() {
		String myMessage = "Wrong content of file with zones map description. \n";
		return new IOException(myMessage);
	}
	
	private IOException getDistributionException() {
		String myMessage = "Wrong content of initiation file. \n";
		return new IOException(myMessage);
	}
}