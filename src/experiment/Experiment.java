package experiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import starter.Shared;
import statistic.StatisticDispatcher;
import zone.Zone;
import distribution.ExperimentDistribution;
import distribution.ZoneDistribution;

public class Experiment {
	
	private final Scenario scenario;
	private final Integer numberOfModelingYears;
	
	private List<Zone> zones;
	private int yearCursore;
	
	public Experiment(
			ExperimentDistribution firstDistribution,
			Scenario scenario,
			int numberOfModelingYears,
			StatisticDispatcher statisticDispatcher,
			double capacityMultiplier) {
		this.scenario = scenario;
		this.numberOfModelingYears = numberOfModelingYears;
		createZones(statisticDispatcher, firstDistribution, capacityMultiplier);
	}
	
	private void createZones(
			StatisticDispatcher statisticDispatcher,
			ExperimentDistribution firstDistribution,
			double capacityMultiplier) {
		zones = new ArrayList<Zone>();
		int zoneNumber=0;
		for (ZoneDistribution firstZoneDistr : firstDistribution.getZoneDistributions()) {
			Zone zone = new Zone(firstZoneDistr, zoneNumber, statisticDispatcher, capacityMultiplier, this);
			zones.add(zone);
			zoneNumber++;
		}
	}
	
	public void runWitExperimentNumber(int experimentNumber) {
		resetZonesTo(experimentNumber);
		scenario.start();
		yearCursore = 0;
		while (yearCursore < numberOfModelingYears) {
			Shared.infoLogger.info("YEAR NUMBER\t" + yearCursore + "\tSTARTED IN\tEXPERIMENT_" + experimentNumber);
			firstPhaseProcessing();
			try {
				scenarioCommandsProcessing();
			} catch (IOException e) {e.printStackTrace();}
			movePhaseProcessing();
			yearCursore++;
		}
	}
	
	private void resetZonesTo(int experimentNumber) {
		for (Zone zone : zones)
			zone.resetTo(experimentNumber);
	}
	
	private void scenarioCommandsProcessing() throws IOException{
		ArrayList<Action> actions = scenario.getCommandsForNextYear(yearCursore);
		for (Action action : actions)
			for (Integer zoneNumber : action.getZonesNumbers())
				zones.get(zoneNumber).scenarioCommand(action.getCommand());
	}

	private void firstPhaseProcessing(){
		for (Zone zone : zones)
			zone.firstPhase();
	}

	private void movePhaseProcessing(){
		for (Zone zone : zones)
			zone.movePhase();
	}
	
	public Zone getZone(int number) {
		return zones.get(number);
	}
}
