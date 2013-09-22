package experiment;

import java.util.Map;
import java.util.TreeMap;

import statistic.StatisticDispatcher;
import statistic.StatisticSettings.Subiteration;
import statistic.YearStatistic;
import experiment.individual.Individual;
import experiment.zone.Zone;

public class YearStatisticCollector {

	private StatisticDispatcher dispatcher;
	private Iterable<Zone> zones;
	private Map<Integer, Map<String, Map<Integer, Map<Integer, Integer>>>> yearStatistic;
	private int experimentNumber;
	private Integer year;
	private Integer subiteration;
	private String zoneName;
	public final static Integer TOTAL_AGE = 999;
	
	public YearStatisticCollector(StatisticDispatcher dispatcher, Iterable<Zone> zones) {
		this.dispatcher = dispatcher;
		this.zones = zones;
	}
	
	public void openNewYear(int experimentNumber, int year) {
		this.experimentNumber = experimentNumber;
		this.year = year;
		yearStatistic = new TreeMap<>();
	}
	
	public void collect(Subiteration subiteration) {
		this.subiteration = subiteration.ordinal();
		if (!isDispatcherInterestedIn())
			return;
		if (dispatcher.getSettings().shouldDisplayImmatures())
			if (dispatcher.getSettings().shouldDistinguishAges())
				collectWithImmatures();
			else
				sumWithImmatures();
		else
			if (dispatcher.getSettings().shouldDistinguishAges())
				collectWithoutImmatures();
			else
				sumWithoutImmatures();
	}
	
	private boolean isDispatcherInterestedIn() {
		return dispatcher.getSettings().getReportingSubiterations().contains(subiteration);
	}
	
	private void collectWithImmatures() {
		for (Zone zone : zones) {
			zoneName = zone.getZoneName();
			for (Individual indiv : zone.getMales())
				add(indiv);
			for (Individual indiv : zone.getFemales())
				add(indiv);
			for (Individual indiv : zone.getYearlings())
				add(indiv);
			for (Individual indiv : zone.getOtherImmatures())
				add(indiv);
		}
	}
	
	private void sumWithImmatures() {
		for (Zone zone : zones) {
			zoneName = zone.getZoneName();
			for (Individual indiv : zone.getMales())
				addToTotal(indiv);
			for (Individual indiv : zone.getFemales())
				addToTotal(indiv);
			for (Individual indiv : zone.getYearlings())
				addToTotal(indiv);
			for (Individual indiv : zone.getOtherImmatures())
				addToTotal(indiv);
		}
	}
	
	private void collectWithoutImmatures() {
		for (Zone zone : zones) {
			zoneName = zone.getZoneName();
			for (Individual indiv : zone.getMales())
				add(indiv);
			for (Individual indiv : zone.getFemales())
				add(indiv);
		}
	}
	
	private void sumWithoutImmatures() {
		for (Zone zone : zones) {
			zoneName = zone.getZoneName();
			for (Individual indiv : zone.getMales())
				addToTotal(indiv);
			for (Individual indiv : zone.getFemales())
				addToTotal(indiv);
		}
	}
	
	private void add(Individual individual) {
		Map<String, Map<Integer, Map<Integer, Integer>>> subiterationStat = getSubiterationStatMap();
		Map<Integer, Map<Integer, Integer>> zoneStat = getZoneStatMap(subiterationStat);
		Map<Integer, Integer> genotypeStat = getGenotypeStatMap(zoneStat, individual.getGenotype().getId());
		increment(genotypeStat, individual.getAge());
	}
	
	private void addToTotal(Individual individual) {
		Map<String, Map<Integer, Map<Integer, Integer>>> subiterationStat = getSubiterationStatMap();
		Map<Integer, Map<Integer, Integer>> zoneStat = getZoneStatMap(subiterationStat);
		Map<Integer, Integer> genotypeStat = getGenotypeStatMap(zoneStat, individual.getGenotype().getId());
		incrementTotal(genotypeStat);
	}
	
	public void commitLastYearStatistic() {
		dispatcher.addPackage(new YearStatistic(experimentNumber, year, yearStatistic));
	}
	
	private Map<String, Map<Integer, Map<Integer, Integer>>> getSubiterationStatMap() {
		Map<String, Map<Integer, Map<Integer, Integer>>> map = yearStatistic.get(subiteration);
		if (map == null) {
			map = new TreeMap<>();
			yearStatistic.put(subiteration, map);
		}
		return map;
	}
	
	private Map<Integer, Map<Integer, Integer>> getZoneStatMap(Map<String, Map<Integer, Map<Integer, Integer>>> subiterationStatMap) {
		Map<Integer, Map<Integer, Integer>> map = subiterationStatMap.get(zoneName);
		if (map == null) {
			map = new TreeMap<>();
			subiterationStatMap.put(zoneName, map);
		}
		return map;
	}
	
	private Map<Integer, Integer> getGenotypeStatMap(Map<Integer, Map<Integer, Integer>> zoneStatMap, Integer genotypeId) {
		Map<Integer, Integer> map = zoneStatMap.get(genotypeId);
		if (map == null) {
			map = new TreeMap<>();
			zoneStatMap.put(genotypeId, map);
		}
		return map;
	}
	
	private void increment(Map<Integer, Integer> genotypeStat, Integer age) {
		Integer number = genotypeStat.get(age);
		if (number == null)
			number = 0;
		number++;
		genotypeStat.put(age, number);
	}
	
	private void incrementTotal(Map<Integer, Integer> genotypeStat) {
		Integer number = genotypeStat.get(TOTAL_AGE);
		if (number == null)
			number = 0;
		number++;
		genotypeStat.put(TOTAL_AGE, number);
	}
}
