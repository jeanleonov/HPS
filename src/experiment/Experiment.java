package experiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import starter.Shared;
import statistic.StatisticDispatcher;
import statistic.StatisticSettings.Subiteration;
import statistic.YearStatistic;
import statistic.YearStatisticCollector;
import experiment.scenario.Action;
import experiment.scenario.Scenario;
import experiment.zone.Zone;

public class Experiment {
	
	private final Scenario scenario;
	private final Integer numberOfModelingYears;
	
	private Map<String, Zone> zones;
	private int yearCursor;
	private YearStatisticCollector collector;
	
	public Experiment(
			List<ZoneSettings> zonesSettings,
			Scenario scenario,
			int numberOfModelingYears) {
		this.scenario = scenario;
		this.numberOfModelingYears = numberOfModelingYears;
		createZones(zonesSettings);
	}
	
	private void createZones(List<ZoneSettings> zonesSettings) {
		zones = new HashMap<String, Zone>();
		for (ZoneSettings zoneSettings : zonesSettings) {
			Zone zone = new Zone(zoneSettings);
			zones.put(zoneSettings.getZoneName(), zone);
		}
	}
	
	public void runWitExperimentNumber(int experimentNumber, StatisticDispatcher statisticDispatcher) throws IOException {
		collector = new YearStatisticCollector(statisticDispatcher, zones.values());
		resetZones();
		scenario.start();
		yearCursor = 0;
		boolean isYearLast = yearCursor>=numberOfModelingYears-1;
		while (!isYearLast) {
			isYearLast = yearCursor==numberOfModelingYears-1;
			modelYear(experimentNumber, yearCursor, isYearLast);
			yearCursor++;
		}
	}
	
	public YearStatistic getLastYearStatistic() {
		return collector.getLastYearStatistic();
	}
	
	private void modelYear(int experimentNumber, int year, boolean isYearLast) throws IOException {
		Shared.debugLogger.debug("YEAR NUMBER\t" + yearCursor + "\tSTARTED IN\tEXPERIMENT_" + experimentNumber);
		collector.openNewYear(experimentNumber, yearCursor, isYearLast);
		updateListsAndIndividualSettings();
		reproductionPhaseProcessing();
		competitionPhaseProcessing();
		diePhaseProcessing();
		try {
			scenarioCommandsProcessing();
		} catch (IOException e) {e.printStackTrace();}
		movePhaseProcessing();
		collector.commitLastYearStatistic();
	}
	
	private void resetZones() {
		for (Zone zone : zones.values())
			zone.reset();
	}
	
	private void scenarioCommandsProcessing() throws IOException{
		ArrayList<Action> actions = scenario.getCommandsForNextYear(yearCursor);
		for (Action action : actions)
			for (String zoneName : action.getZonesNames())
				zones.get(zoneName).scenarioCommand(action.getCommand());
	}

	private void updateListsAndIndividualSettings() {
		for (Zone zone : zones.values())
			zone.updateListsAndIndividualSettings();
		collector.collect(Subiteration.AFTER_EVOLUTION);
	}

	private void reproductionPhaseProcessing(){
		for (Zone zone : zones.values())
			zone.reproductionProcessing();
		collector.collect(Subiteration.AFTER_REPRODACTION);
	}

	private void competitionPhaseProcessing(){
		for (Zone zone : zones.values())
			zone.competitionProcessing();
		collector.collect(Subiteration.AFTER_COMPETITION);
	}

	private void diePhaseProcessing(){
		for (Zone zone : zones.values())
			zone.dieProcessing();
		collector.collect(Subiteration.AFTER_DIEING);
	}

	private void movePhaseProcessing(){
		for (Zone zone : zones.values())
			zone.movePhase();
		collector.collect(Subiteration.AFTER_MOVE_AND_SCENARIO);
	}
	
	public Zone getZone(String zoneName) {
		return zones.get(zoneName);
	}
}
