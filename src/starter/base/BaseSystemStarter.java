package starter.base;

import java.io.StringReader;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import starter.Argument;
import starter.Shared;
import statistic.StatisticDispatcher;
import statistic.StatisticSettings;
import utils.cmd.line.parser.CmdLineParser;
import utils.parser.ParseException;
import utils.parser.Parser;
import experiment.Experiment;
import experiment.ZoneSettings;
import experiment.scenario.Scenario;

public abstract class BaseSystemStarter {
	
	private static String startArgs="";

	private BaseDataFiller dataFiller;
	private StatisticDispatcher statisticDispatcher;
	private String curStatisticFileURL;
	
	protected int remainingExperints;
	protected int curExperiment;
	protected int numberOfModelingYears;
	private String statisticSettings;
	protected double capacityMultiplier;
	
	private long timeOfStart;

	public BaseSystemStarter(String[] args) {
        DOMConfigurator.configure("src/log4j.xml");
		saveStartArgs(args);
		parseArgs(args);
		this.curExperiment = (Integer) Argument.CURRENT_EXPERIMENT.getValue();
		this.remainingExperints = (Integer) Argument.NUMBER_OF_EXPERIMENTS.getValue();
		this.numberOfModelingYears = (Integer) Argument.YEARS.getValue();
		this.statisticSettings = (String) Argument.STATISTIC.getValue();
		this.capacityMultiplier = (Double) Argument.CAPACITY_MULTIPLIER.getValue();
	}
	
	
	
	protected abstract BaseDataFiller getDataFiller() throws Exception;
	protected abstract String getStatisticFileName();
	
	
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
	
	private void createStatisticDispatcher() throws ParseException, Exception {
		curStatisticFileURL = getStatisticFileName();
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
		Logger.getLogger("runningTimeLogger").info(String.format("Executing time:	[%2s:%2s:%2s.%3s]",hour,min,sec,msec) + "  With args: " + getStartArgs());
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
            System.out.println(Shared.SINGLE_RUN_HELP_TEXT);
            System.exit(2);
        }
		if((Boolean) Argument.HELP.getValue()) {
            System.out.println(Shared.SINGLE_RUN_HELP_TEXT);
            System.exit(0);
		}
	}
}