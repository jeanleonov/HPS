package starter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

import statistic.LastYearStatisticWriter;
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
					scenarioPath,
					dimensionsConfPath;
	private String	viabilitySettingsContent,
					posteritySettingContent,
					movePossibilitiesContent,
					distributionInfoContent,
					scenarioContent;

	private DataFiller dataFiller;
	private StatisticDispatcher statisticDispatcher;
	private LastYearStatisticWriter lastYearStatisticWriter;
	private String curStatisticFileURL;
	private InputsPreparer inputsPreparer;
	private StringBuilder settingsStatistic = new StringBuilder();
	
	private int numberOfModelingExperints;
	private int curExperiment;
	private int curPoint;
	private int numberOfPoints;
	private int numberOfModelingYears;
	private String statisticSettings;
	private double capacityMultiplier;
	
	private long timeOfStart;

	public SystemStarter(String[] args) throws Exception {
		saveStartArgs(args);
		parseArgs(args);
		createFolders();
		this.curExperiment = (Integer) Argument.CURRENT_EXPERIMENT.getValue();
		this.curPoint = (Integer) Argument.POINT_NUMBER.getValue();
		this.numberOfModelingExperints = (Integer) Argument.NUMBER_OF_EXPERIMENTS.getValue();
		this.numberOfModelingYears = (Integer) Argument.YEARS.getValue();
		this.statisticSettings = (String) Argument.STATISTIC.getValue();
		this.capacityMultiplier = (Double) Argument.CAPACITY_MULTIPLIER.getValue();
		String projectPath = (String) Argument.PROJECT_PATH.getValue();
		this.viabilitySettingsPath = getPathOf(Argument.VIABILITY, projectPath);
		this.posteritySettingPath = getPathOf(Argument.POSTERITY, projectPath);
		this.movePossibilitiesPath = getPathOf(Argument.MOVE_POSSIBILITIES, projectPath);
		this.scenarioPath = getPathOf(Argument.SCENARIO, projectPath);
		this.distributionInfoPath = getPathOf(Argument.INITIATION, projectPath);
		this.dimensionsConfPath = (String) Argument.DIMENSIONS_TO_TEST.getValue();
	}
	
	private void createFolders() {
		createLogsFolder();
		createSettingsFolder();
		createStatisticFolder();
	}
	
	public void startSystem() throws Exception {
		new Parser(new StringReader(statisticSettings));
		inputsPreparer = new InputsPreparer(dimensionsConfPath);
		lastYearStatisticWriter = new LastYearStatisticWriter(getShortStatisticFileName());
		readSettingsFiles();
		numberOfPoints = inputsPreparer.maxPointNumber()+1;
		if (curExperiment == -1)
			curExperiment = 0;
		if (numberOfModelingExperints == -1)
			numberOfModelingExperints = 1;
		if (curPoint == -1)
			curPoint = 0;
		else
			numberOfPoints = 1;
		runPoints();
		lastYearStatisticWriter.finish();
	}
	
	private void readSettingsFiles() throws IOException {
		viabilitySettingsContent = getFullFileContent(viabilitySettingsPath);
		posteritySettingContent = getFullFileContent(posteritySettingPath);
		movePossibilitiesContent = getFullFileContent(movePossibilitiesPath);
		distributionInfoContent = getFullFileContent(distributionInfoPath);
		scenarioContent = getFullFileContent(scenarioPath);
	}
	
	private String getFullFileContent(String inputPath) throws IOException {
		BufferedReader inputReader = null;
		try {
			inputReader = new BufferedReader(new FileReader(inputPath));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = inputReader.readLine()) != null)
				builder.append(line).append('\n');
			return builder.toString();
		} finally {
			if (inputReader != null)
				inputReader.close();
		}
	}
	
	private void runPoints() throws Exception {
		while (curPoint < numberOfPoints) {
			this.dataFiller = getDataFiller();
			dataFiller.read();
			saveSettingsPack();
			runExperints();
			curPoint++;
			curExperiment = 0;
		}
	}
	
	private DataFiller getDataFiller() throws Exception {
		inputsPreparer.setPoint(curPoint);
		String viability = inputsPreparer.getPreparedContent(viabilitySettingsContent);
		String posterity = inputsPreparer.getPreparedContent(posteritySettingContent);
		String movePossibility = inputsPreparer.getPreparedContent(movePossibilitiesContent);
		String scenario = inputsPreparer.getPreparedContent(scenarioContent);
		String distributionInfo = inputsPreparer.getPreparedContent(distributionInfoContent);
		settingsStatistic.setLength(0);
		settingsStatistic.append(viability).append("\n\n\n");
		settingsStatistic.append(posterity).append("\n\n\n");
		settingsStatistic.append(movePossibility).append("\n\n\n");
		settingsStatistic.append(scenario).append("\n\n\n");
		settingsStatistic.append(distributionInfo);
		return new DataFiller(viability, posterity, movePossibility, scenario, distributionInfo, capacityMultiplier);
	}
	
	private void createStatisticDispatcher() throws ParseException, Exception {
		curStatisticFileURL = getStatisticFileName();
		Parser.ReInit(new StringReader(statisticSettings));
		StatisticSettings settings = Parser.statisticSettings();
		statisticDispatcher	= new StatisticDispatcher(curStatisticFileURL, settings,
				dataFiller.getZonesSettings().get(0));		// #TODO terrible stub!!!
	}
	
	private void saveSettingsPack() throws IOException {
		BufferedWriter settingsStatisticWriter = null;
		try {
			FileWriter fileWriter = new FileWriter(getSettingsStatisticFileName());
			settingsStatisticWriter = new BufferedWriter(fileWriter);
			settingsStatisticWriter.write(settingsStatistic.toString());
		}catch (IOException e) {
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append("Writting of settings pack was failed!\n");
			errorMsg.append(Shared.printStack(e));
			Shared.problemsLogger.error(errorMsg.toString());
		}finally {
			if (settingsStatisticWriter != null)
				settingsStatisticWriter.close();
		}
	}

	private String getStatisticFileName() {
		return getStatisticFilePrefix(Shared.STATISTICS_FOLDER).append(".csv").toString();
	}
	
	private String getSettingsStatisticFileName() {
		return getStatisticFilePrefix(Shared.SETTINGS_FOLDER).append(" settings.csv").toString();
	}
	
	private String getShortStatisticFileName() {
		return getStatisticFileShortPrefix(Shared.STATISTICS_FOLDER).append(".csv").toString();
	}
	
	private StringBuilder getStatisticFilePrefix(String folderName) {
		String experimentSeriesName = null;
		try {
			experimentSeriesName = (String) Argument.EXPERIMENTS_SERIES_NAME.getValue();
		} catch (Exception e) {
			experimentSeriesName = "Statistic";
		}
		StringBuilder result = new StringBuilder(folderName).append("/");
		result.append(experimentSeriesName);
		result.append(" -e ").append(curExperiment);
		result.append(" -p ").append(curPoint);
		Date d = new Date();
		result.append(String.format(" %tY_%tm_%td %tH-%tM-%tS", d, d, d, d, d, d));
		return result;
	}
	
	private StringBuilder getStatisticFileShortPrefix(String folderName) {
		String experimentSeriesName = null;
		try {
			experimentSeriesName = (String) Argument.EXPERIMENTS_SERIES_NAME.getValue();
		} catch (Exception e) {
			experimentSeriesName = "Statistic";
		}
		StringBuilder result = new StringBuilder(folderName).append("/");
		result.append(experimentSeriesName);
		return result;
	}
	
	private void runExperints() throws ParseException, Exception {
		List<ZoneSettings> zonesSettings = dataFiller.getZonesSettings();
		Scenario scenario = dataFiller.getScenario();
		Experiment experiment = new Experiment(zonesSettings, scenario, numberOfModelingYears);
		lastYearStatisticWriter.openNewPoint(inputsPreparer.getPrevPointValuesMap(), curPoint);
		int remainingExperints = numberOfModelingExperints;
		while (remainingExperints > 0) {
			timeOfStart = System.currentTimeMillis();
			createStatisticDispatcher();
			experiment.runWitExperimentNumber(curExperiment++, statisticDispatcher);
			lastYearStatisticWriter.write(experiment.getLastYearStatistic());
			remainingExperints--;
			finish();
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
	
	static private String getPathOf(Argument argument, String projectPath) throws Exception {
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
	
	private static void createLogsFolder() {
		File logsFolder = new File(Shared.LOGS_FOLDER+"/");
		if (!logsFolder.exists())
			logsFolder.mkdir();
	}
	
	private static void createSettingsFolder() {
		File settingsFolder = new File(Shared.SETTINGS_FOLDER+"/");
		if (!settingsFolder.exists())
			settingsFolder.mkdir();
	}
	
	private static void createStatisticFolder() {
		File statisticsFolder = new File(Shared.STATISTICS_FOLDER+"/");
		if (!statisticsFolder.exists())
			statisticsFolder.mkdir();
	}
}