package experiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import starter.Shared;
import statistic.StatisticDispatcher;
import statistic.StatisticSettings.Subiteration;
import distribution.ExperimentDistribution;
import distribution.ZoneDistribution;
import experiment.scenario.Action;
import experiment.scenario.Scenario;
import experiment.zone.Zone;

public class Experiment {
	
	private final Scenario scenario;
	private final Integer numberOfModelingYears;
	
	private List<Zone> zones;
	private int yearCursor;
	private YearStatisticCollector collector;
	
	public Experiment(
			ExperimentDistribution firstDistribution,
			Scenario scenario,
			int numberOfModelingYears,
			StatisticDispatcher statisticDispatcher,
			double capacityMultiplier) {
		this.scenario = scenario;
		this.numberOfModelingYears = numberOfModelingYears;
		createZones(statisticDispatcher, firstDistribution, capacityMultiplier);
		collector = new YearStatisticCollector(statisticDispatcher, zones);
	}
	
	private void createZones(
			StatisticDispatcher statisticDispatcher,
			ExperimentDistribution firstDistribution,
			double capacityMultiplier) {
		zones = new ArrayList<Zone>();
		int zoneNumber=0;
		for (ZoneDistribution firstZoneDistr : firstDistribution.getZoneDistributions()) {
			Zone zone = new Zone(firstZoneDistr, zoneNumber, capacityMultiplier);
			zones.add(zone);
			zoneNumber++;
		}
	}
	
	public void runWitExperimentNumber(int experimentNumber) {
		resetZonesTo(experimentNumber);
		scenario.start();
		yearCursor = 0;
		while (yearCursor < numberOfModelingYears) {
			modelYear(experimentNumber, yearCursor);
			yearCursor++;
		}
	}
	
	private void modelYear(int experimentNumber, int year) {
		Shared.infoLogger.info("YEAR NUMBER\t" + yearCursor + "\tSTARTED IN\tEXPERIMENT_" + experimentNumber);
		collector.openNewYear(experimentNumber, yearCursor);
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
	
	private void resetZonesTo(int experimentNumber) {
		for (Zone zone : zones)
			zone.resetTo(experimentNumber);
	}
	
	private void scenarioCommandsProcessing() throws IOException{
		ArrayList<Action> actions = scenario.getCommandsForNextYear(yearCursor);
		for (Action action : actions)
			for (Integer zoneNumber : action.getZonesNumbers())
				zones.get(zoneNumber).scenarioCommand(action.getCommand());
	}

	private void updateListsAndIndividualSettings() {
		for (Zone zone : zones)
			zone.updateListsAndIndividualSettings();
		collector.collect(Subiteration.AFTER_EVOLUTION);
	}

	private void reproductionPhaseProcessing(){
		for (Zone zone : zones)
			zone.reproductionProcessing();
		collector.collect(Subiteration.AFTER_REPRODACTION);
	}

	private void competitionPhaseProcessing(){
		for (Zone zone : zones)
			zone.competitionProcessing();
		collector.collect(Subiteration.AFTER_COMPETITION);
	}

	private void diePhaseProcessing(){
		for (Zone zone : zones)
			zone.dieProcessing();
		collector.collect(Subiteration.AFTER_DIEING);
	}

	private void movePhaseProcessing(){
		for (Zone zone : zones)
			zone.movePhase();
		collector.collect(Subiteration.AFTER_MOVE_AND_SCENARIO);
	}
	
	public Zone getZone(int number) {
		return zones.get(number);
	}
}
