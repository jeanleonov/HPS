package starter;

import org.apache.log4j.Logger;

public final class Shared {
	
	// Just for ban on the creation of objects.
	// We cann't use keyword 'abstract' with 'final'
	private Shared(){}

	public final static String
	PROJECT_PATH = getProjectPath(),
	DEFAULT_VIABILITY_FILE = "Viability.csv",
	DEFAULT_POSTERITY_FILE = "Posterity.csv",
	DEFAULT_SCENARIO_FILE = "Scenario.scn",
	DEFAULT_INITIATION_FILE = "Initiation.csv",
	DEFAULT_MAP_FILE = "Map.csv",
	DEFAULT_DIMENSIONS_TO_TEST = "Dimensions.csv",
	DEFAULT_STATISTIC_MODE = "ages with_immatures after_each",
	DEFAULT_NAME = "modeling",
	DEFAULT_OUTPUTS_FOLDER = "",
	DEFAULT_LOG4J_FOLDER = "log4j.xml",
	LOGS_FOLDER = "log",
	SETTINGS_FOLDER = "settings",
	STATISTICS_FOLDER = "statistics",
			
	RESOURCES = "-RESOURCES-",
	
	HELP_TEXT = "Usage:\n" +
				" 	[--help] show list of expected arguments. \n\n" +
				"	[{--name} string]  name of experiments series | DEFAULT \"modeling\"\n"	+ 
				"			Examples:\n" +
				"			--name \"some modeling\"\n" +
				"			--name some_modeling\n\n" +

				"	[{-y, --years} int]  number of simulated years | DEFAULT 1\n" + 
				"			Examples:\n" +
				"			-y 200\n" +
				"			--years 200\n\n" +

				"	[{-E, --number_of_experiments} int]  number of simulated experiments | DEFAULT -1\n" + 
				"			Examples:\n" +
				"			-E 100\n" +
				"			--number_of_experiments 100\n\n" +

				"	[{-p, --point} int]  number of point to test | DEFAULT -1\n" + 
				"			Examples:\n" +
				"			-p 17\n" +
				"			--point 17\n\n" +

				"   [{-S, --statistic} string] statistic collecting properties which match to regexp:\n"+
				"                              ( ages\n"+
				"                               |genotypes\n"+
				"                               |with_immatures\n"+
				"                               |without_immatures\n"+
				"                               |after_each\n"+
				"                               |after_move_and_scenario\n"+
				"                               |after_evolution\n"+
				"                               |after_reproduction\n"+
				"                               |after_competition\n"+
				"                               |after_dieing\n"+
				"                              )*\n" + 
				"			Examples:\n" +
				"			-S \"ages with_immatures after_reproduction after_competition\"\n" +
				"			-S \"ages without_immatures after_each\"\n" +
				"			-S \"ageswithout_immatures after_dieing\"\n" +
				"			-S \"genotypes with_immatures after_dieing\"\n" +
				"			--statistic \"genotypes with_immatures after_dieing\"\n" +
				"			--statistic \"ageswithout_immatures after_dieing\"\n\n\n" +

				
				"	[{--project_path} string]  directory for files with settings | DEFAULT user.dir\n" +
				"	[{--viability} string]  name of the file with viability settings | DEFAULT \'Viability.csv\'\n" +
				"	[{--posterity} string]  name of the file with posterity settings | DEFAULT \'Posterity.csv\'\n" +
				"	[{--scenario} string]  name of the file with scenario settings | DEFAULT \'Scenario.scn\'\n" +
				"	[{--initiation} string]  name of the file with initiation settings | DEFAULT \'Initiation.hpsi\'\n"+
				"	[{--map} string]  name of the file with map settings | DEFAULT \'Map.csv\'\n"+
				"	[{--dimensions} string]  name of the file with dimensions to test | DEFAULT \'Map.csv\'\n\n"+
				
				"	[{-e, --cur_experiment} int]  current experiment number | DEFAULT -1" +
				"	[{-M, --capacity_multiplier} double]  argument for multipling capacity of zones | DEFAULT 1\n\n";
	
	public final static 
	int	DEFAULT_MAX_SIZE_OF_LIST_OF_FEMALES = 10,
		DEFAULT_MIN_NUMBER_OF_MALES_FOR_CONTINUE = 3,
		MAX_NUMBER_OF_REPRODUCTION_CIRCLES = 10;
	
	public final static 
	Logger	problemsLogger = Logger.getLogger("problemsLogger"),
			debugLogger = Logger.getLogger("debugLogger"),
			infoLogger = Logger.getLogger("infoLogger");
	
	public static String printStack(Throwable throwable) {
		StringBuffer stackTrace = new StringBuffer();
		stackTrace.append(throwable.getMessage());
		stackTrace.append('\n');
		StackTraceElement[] stack = throwable.getStackTrace();
		for (int i=0; i<stack.length; i++)
			stackTrace.append(stack[i].toString()+"\n");
		return stackTrace.toString();
	}
		
	private final static
	String getProjectPath(){
		String path = System.getProperty("user.dir");
		return path;
	}
}