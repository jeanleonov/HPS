package starter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import settings.Settings;
import statistic.StatisticDispatcher;
import statistic.visualisation.VisualisationFrame;
import distribution.ExperimentDistribution;
import experiment.Experiment;
import experiment.scenario.Scenario;

public class SystemStarter {

	private DataFiller dataFiller;
	private StatisticDispatcher statisticDispatcher;
	private String curStatisticFileURL;
	
	
	private String	viabilitySettingsPath,
					posteritySettingPath,
					movePossibilitiesPath,
					experimentInfoPath,
					scenarioPath;
	private int numberOfExperints;
	private int curExperiment;
	private int numberOfModelingYears;
	private int zoneMultiplier;
	private double capacityMultiplier;
	
	private boolean shouldDisplayDiagram;
	private boolean shouldDisplayDetailedDiagram;
	private boolean shouldDisplayImmatures;
	
	long timeOfStart;

	public SystemStarter(
			Map<SourceType,String> pathesMap,
			int zoneMultiplier,
			double capacityMultiplier,
			int curExperiment,
			int numberOfExperints,
			int numberOfYears,
			boolean shouldDisplayDiagram,
			boolean shouldDisplayDetailedDiagram,
			boolean shouldDisplayImmatures){
		this.viabilitySettingsPath = pathesMap.get(SourceType.VIABILITY);
		this.posteritySettingPath = pathesMap.get(SourceType.POSTERITY);
		this.movePossibilitiesPath = pathesMap.get(SourceType.MOVE_POSSIBILITIES);
		this.scenarioPath = pathesMap.get(SourceType.SCENARIO);
		this.experimentInfoPath = pathesMap.get(SourceType.INITIATION);
		this.zoneMultiplier = zoneMultiplier;
		this.capacityMultiplier = capacityMultiplier;
		this.curExperiment = curExperiment;
		this.numberOfExperints = numberOfExperints;
		this.numberOfModelingYears = numberOfYears;
		this.shouldDisplayDiagram = shouldDisplayDiagram || shouldDisplayDetailedDiagram || shouldDisplayImmatures; 
		this.shouldDisplayDetailedDiagram = shouldDisplayDetailedDiagram;
		this.shouldDisplayImmatures = shouldDisplayImmatures;
		createStatisticDispatcher();
		timeOfStart = System.currentTimeMillis();
	}
	
	public void startSystem() throws Exception {
		readData();
		Settings.init(dataFiller.getViabilityTable(), dataFiller.getPosterityTable(), dataFiller.getMovePosibilitiesTable());
		if (curExperiment == -1)
			curExperiment = 0;
		if (numberOfExperints == -1)
			numberOfExperints = 1;
		runExperints();
		finish();
	}
	
	private void readData() throws Exception {
		BufferedReader posteritySettingsReader;
		BufferedReader viabilitySettingsReader;
		BufferedReader movePossibilitiesReader;
		BufferedReader scenarioReader;
		BufferedReader experimentInfoReader;
		viabilitySettingsReader = new BufferedReader(new FileReader(viabilitySettingsPath));
		posteritySettingsReader = new BufferedReader(new FileReader(posteritySettingPath));
		if (movePossibilitiesPath.endsWith(Shared.DEFAULT_MAP_FILE))
			movePossibilitiesReader = null;
		else
			movePossibilitiesReader = new BufferedReader(new FileReader(movePossibilitiesPath));
		experimentInfoReader = new BufferedReader(new FileReader(experimentInfoPath));
		scenarioReader = new BufferedReader(new FileReader(scenarioPath));
		dataFiller = new DataFiller(viabilitySettingsReader, posteritySettingsReader, movePossibilitiesReader, scenarioReader, experimentInfoReader, zoneMultiplier);
	}
	
	private void createStatisticDispatcher() {
		Date d = new Date();
		curStatisticFileURL = String.format("statistics/%tY_%tm_%td %tH-%tM-%tS", d, d, d, d, d, d) + 
							  ((curExperiment==-1)?(""):(" e"+curExperiment)) + ".csv";
		statisticDispatcher	= new StatisticDispatcher(curStatisticFileURL);
	}
	
	private void runExperints() {
		ExperimentDistribution firstDistribution = dataFiller.getExperimentDistribution();
		Scenario scenario = dataFiller.getScenario();
		Experiment experiment = new Experiment(firstDistribution, scenario, numberOfModelingYears, statisticDispatcher, capacityMultiplier);
		while (curExperiment < numberOfExperints) {
			experiment.runWitExperimentNumber(curExperiment);
			curExperiment++;
		}
	}
	
	private void finish() {
		long executingTime = System.currentTimeMillis()-timeOfStart,
			 hour = executingTime/1000/60/60,
			 min = executingTime/1000/60 - hour*60,
			 sec = executingTime/1000 - min*60 - hour*3600,
			 msec = executingTime - sec*1000 - min*60000 - hour*3600000;
		Logger.getLogger("runningTimeLogger").info(String.format("Executing time:	[%2s:%2s:%2s.%3s]",hour,min,sec,msec) + "  With args: " + MainClass.getStartArgs());
		statisticDispatcher.flush();
		if (shouldDisplayDiagram)
			new VisualisationFrame(curStatisticFileURL, shouldDisplayDetailedDiagram, shouldDisplayImmatures);
	}
}