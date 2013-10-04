package starter;

import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

import org.apache.log4j.xml.DOMConfigurator;

import statistic.StatisticDispatcher;
import statistic.StatisticSettings;
import utils.cmd.line.parser.CmdLineParser;
import utils.parser.ParseException;
import utils.parser.Parser;
import experiment.Experiment;
import experiment.ZoneSettings;
import experiment.scenario.Scenario;

public class SystemStarter {
	
	private static String startArgs="";

	private String	viabilitySettingsPath,
					posteritySettingPath,
					movePossibilitiesPath,
					distributionInfoPath,
					scenarioPath;

	private DataFiller dataFiller;
	private StatisticDispatcher statisticDispatcher;
	private String curStatisticFileURL;
	
	protected int remainingExperints;
	protected int curExperiment;
	protected int curPoint;
	protected int numberOfModelingYears;
	private String statisticSettings;
	protected double capacityMultiplier;
	
	private long timeOfStart;

	public SystemStarter(String[] args) throws Exception {
        DOMConfigurator.configure("src/log4j.xml");
		saveStartArgs(args);
		parseArgs(args);
		this.curExperiment = (Integer) Argument.CURRENT_EXPERIMENT.getValue();
		this.curPoint = (Integer) Argument.POINT_NUMBER.getValue();
		this.remainingExperints = (Integer) Argument.NUMBER_OF_EXPERIMENTS.getValue();
		this.numberOfModelingYears = (Integer) Argument.YEARS.getValue();
		this.statisticSettings = (String) Argument.STATISTIC.getValue();
		this.capacityMultiplier = (Double) Argument.CAPACITY_MULTIPLIER.getValue();
		String projectPath = (String) Argument.PROJECT_PATH.getValue();
		this.viabilitySettingsPath = getPathOf(Argument.VIABILITY, projectPath);
		this.posteritySettingPath = getPathOf(Argument.POSTERITY, projectPath);
		this.movePossibilitiesPath = getPathOf(Argument.MOVE_POSSIBILITIES, projectPath);
		this.scenarioPath = getPathOf(Argument.SCENARIO, projectPath);
		this.distributionInfoPath = getPathOf(Argument.INITIATION, projectPath);
	}
	
	public void startSystem() throws Exception {
		new Parser(new StringReader(statisticSettings));
		this.dataFiller = getDataFiller();
		dataFiller.read();
		createStatisticDispatcher();
		timeOfStart = System.currentTimeMillis();
		if (curExperiment == -1)
			curExperiment = 0;
		if (remainingExperints == -1)
			remainingExperints = 1;
		runExperints();
		finish();
	}
	
	protected DataFiller getDataFiller() throws Exception {
		Reader viabilityReader = new FileReader(viabilitySettingsPath);
		Reader posterityReader = new FileReader(posteritySettingPath);
		Reader movePossibilitiesReader = new FileReader(movePossibilitiesPath);
		Reader scenarioReader = new FileReader(scenarioPath);
		Reader distributionInfoReader = new FileReader(distributionInfoPath);
		return new DataFiller(viabilityReader, posterityReader, movePossibilitiesReader, scenarioReader, distributionInfoReader, capacityMultiplier);
	}
	
	private void createStatisticDispatcher() throws ParseException, Exception {
		curStatisticFileURL = getStatisticFileName();
		Parser.ReInit(new StringReader(statisticSettings));
		StatisticSettings settings = Parser.statisticSettings();
		statisticDispatcher	= new StatisticDispatcher(curStatisticFileURL, settings,
				dataFiller.getZonesSettings().get(0));		// #TODO terrible stub!!!
	}

	protected String getStatisticFileName() {
		String experimentSeriesName = null;
		try {
			experimentSeriesName = (String) Argument.EXPERIMENTS_SERIES_NAME.getValue();
		} catch (Exception e) {
			experimentSeriesName = "Statistic";
		}
		StringBuilder result = new StringBuilder("statistics/");
		result.append(experimentSeriesName);
		if (curExperiment != -1)
			result.append(" -e ").append(curExperiment);
		if (curPoint != -1)
			result.append(" -p ").append(curPoint);
		if (remainingExperints != -1)
			result.append(" -E ").append(remainingExperints);
		Date d = new Date();
		result.append(String.format(" %tY_%tm_%td %tH-%tM-%tS", d, d, d, d, d, d));
		result.append(".csv");
		return result.toString();
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
		Shared.infoLogger.info(String.format("Executing time:	[%2s:%2s:%2s.%3s]",hour,min,sec,msec) + "  With args: " + getStartArgs());
		statisticDispatcher.finish();
	}
	
	static protected String getPathOf(Argument argument, String projectPath) throws Exception {
		return projectPath + '/' + (String) argument.getValue();
	}
	
	/**
	 * Saves program arguments for future logging.
	 */
	static private void saveStartArgs(String[] args){
		for (int i=0; i<args.length; i++)
			startArgs += args[i]+" ";
	}
	 /**
	  * Get program arguments which was saved on start.
	  */
	public static String getStartArgs(){
		return startArgs;
	}
		
	private static void parseArgs(String[] args) {
		try {
            Argument.parse(args);
        }
        catch(CmdLineParser.OptionException e) {
        	Shared.problemsLogger.error(e.getMessage());
            System.out.println(Shared.HELP_TEXT);
            System.exit(2);
        }
		if((Boolean) Argument.HELP.getValue()) {
            System.out.println(Shared.HELP_TEXT);
            System.exit(0);
		}
	}
}