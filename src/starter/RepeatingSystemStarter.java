package starter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import statistic.StatisticDispatcher;
import statistic.StatisticSettings;
import utils.parser.ParseException;
import utils.parser.Parser;
import experiment.Experiment;
import experiment.ZoneSettings;
import experiment.scenario.Scenario;

public class RepeatingSystemStarter {

	private RepeatingRunDataFiller dataFiller;
	private StatisticDispatcher statisticDispatcher;
	private String curStatisticFileURL;
	
	
	private String	viabilitySettingsPath,
					posteritySettingPath,
					movePossibilitiesPath,
					distributionInfoPath,
					scenarioPath;
	private int remainingExperints;
	private int curExperiment;
	private int numberOfModelingYears;
	private double capacityMultiplier;
	private String statisticSettingsString;
	
	long timeOfStart;

	public RepeatingSystemStarter(
			Map<Input,String> pathesMap,
			double capacityMultiplier,
			int curExperiment,
			int numberOfExperints,
			int numberOfYears,
			String statisticSettings){
		this.viabilitySettingsPath = pathesMap.get(Input.VIABILITY);
		this.posteritySettingPath = pathesMap.get(Input.POSTERITY);
		this.movePossibilitiesPath = pathesMap.get(Input.MOVE_POSSIBILITIES);
		this.scenarioPath = pathesMap.get(Input.SCENARIO);
		this.distributionInfoPath = pathesMap.get(Input.INITIATION);
		this.capacityMultiplier = capacityMultiplier;
		this.curExperiment = curExperiment;
		this.remainingExperints = numberOfExperints;
		this.numberOfModelingYears = numberOfYears;
		this.statisticSettingsString = statisticSettings;
		new Parser(new StringReader(statisticSettingsString));
	}
	
	public void startSystem() throws Exception {
		readData();
		createStatisticDispatcher(statisticSettingsString);
		timeOfStart = System.currentTimeMillis();
		if (curExperiment == -1)
			curExperiment = 0;
		if (remainingExperints == -1)
			remainingExperints = 1;
		runExperints();
		finish();
	}
	
	private void readData() throws Exception {
		BufferedReader posteritySettingsReader;
		BufferedReader viabilitySettingsReader;
		BufferedReader movePossibilitiesReader;
		BufferedReader scenarioReader;
		BufferedReader distributionInfoReader;
		viabilitySettingsReader = new BufferedReader(new FileReader(viabilitySettingsPath));
		posteritySettingsReader = new BufferedReader(new FileReader(posteritySettingPath));
		movePossibilitiesReader = new BufferedReader(new FileReader(movePossibilitiesPath));
		distributionInfoReader = new BufferedReader(new FileReader(distributionInfoPath));
		scenarioReader = new BufferedReader(new FileReader(scenarioPath));
		dataFiller = new RepeatingRunDataFiller(viabilitySettingsReader, posteritySettingsReader, movePossibilitiesReader, scenarioReader, distributionInfoReader, capacityMultiplier);
	}
	
	private void createStatisticDispatcher(String statisticSettings) throws ParseException, Exception {
		Date d = new Date();
		curStatisticFileURL = String.format("statistics/%tY_%tm_%td %tH-%tM-%tS", d, d, d, d, d, d) + 
							  ((curExperiment==-1)?(""):(" e"+curExperiment)) + ".csv";
		Parser.ReInit(new StringReader(statisticSettings));
		StatisticSettings settings = Parser.statisticSettings();
		statisticDispatcher	= new StatisticDispatcher(curStatisticFileURL, settings,
				dataFiller.getZonesSettings().get(0));		// #TODO terrible stub!!!
	}
	
	private void runExperints() {
		List<ZoneSettings> zonesSettings = dataFiller.getZonesSettings();
		Scenario scenario = dataFiller.getScenario();
		Experiment experiment = new Experiment(zonesSettings, scenario, numberOfModelingYears, statisticDispatcher);
		while (remainingExperints > 0) {
			experiment.runWitExperimentNumber(curExperiment);
			remainingExperints--;
		}
	}
	
	private void finish() {
		long executingTime = System.currentTimeMillis()-timeOfStart,
			 hour = executingTime/1000/60/60,
			 min = executingTime/1000/60 - hour*60,
			 sec = executingTime/1000 - min*60 - hour*3600,
			 msec = executingTime - sec*1000 - min*60000 - hour*3600000;
		Logger.getLogger("runningTimeLogger").info(String.format("Executing time:	[%2s:%2s:%2s.%3s]",hour,min,sec,msec) + "  With args: " + RepeatingRunMainClass.getStartArgs());
		statisticDispatcher.finish();
	}
}