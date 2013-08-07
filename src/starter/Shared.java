package starter;

import org.apache.log4j.Logger;

public final class Shared {
	
	// Just for ban on the creation of objects.
	// We cann't use keyword 'abstract' with 'final'
	private Shared(){}

	public final static String
			PROJECT_PATH = getProjectPath(),
			DEFAULT_MAP_FILE = "*",
			DEFAULT_VIABILITY_FILE = "Viability.csv",
			DEFAULT_POSTERITY_FILE = "Posterity.csv",
			DEFAULT_SCENARIO_FILE = "Scenario.scn",
			DEFAULT_INITIATION_FILE = "Initiation.hpsi",
			DEFAULT_STATISTIC_MODE = "ages with_immatures after_each",
			
			HELP_TEXT = "Usage: [--help] " +
						"	[{-y, --years} int]  number of simulated years | DEFAULT 1\n" +
						"	[{-e, --cur_experiment} int]  curent experiment number | DEFAULT -1\n" +
						"	                              (!= -1: for runing on cluster,\n" +
						"	                              DON'T use this argument whit -E)\n" +
						"	[{-E, --number_of_experiments} int]  number of simulated experiments | DEFAULT -1\n" +
						"	                              (== -1: for runing on cluster,\n" +
						"	                              DON'T use this argument whit -e)\n" +
						"	[{-M, --capacity_multiplier} double]  argument for multipling capacity of zones | DEFAULT 1\n" +
						"	[{-z, --zone_multiplier} int]  temporary argument for multipling number of zones | DEFAULT 1\n" +
						"	[{-f, --project_path} string]  directory for files with settings | DEFAULT user.dir\n" +
						"	[{-v, --viability} string]  name of file with viability settings | DEFAULT \'Viability.csv\'\n" +
						"	[{-m, --map} string]  name of file with map settings | DEFAULT \'DEFAULT_MAP\'\n" +
						"	[{-p, --posterity} string]  name of file with posterity settings | DEFAULT \'Posterity.csv\'\n" +
						"	[{-s, --scenario} string]  name of file with scenario settings | DEFAULT \'Scenario.scn\'\n" +
						"	[{-i, --initiation} string]  name of file with initiation settings | DEFAULT \'Initiation.hpsi\'\n" +
						"	[{-o, --object_manager} int(0|1|2)]  0 - simple creation of individuals,\n" +
						"	                                     1 - creation of individuals with using of object pull,\n" +
						"	                                     2 - creation of individuals with using of object pulls for each zone\n" +
						"	                                     | DEFAULT 0\n" +
						"	[{-d, --display_diagram} boolean] display diagrams after modeling process | DEFAULT false\n" +
						"	[{-D, --detailed_diagram} boolean] display immatures too on diagrams after modeling process | DEFAULT false";
	
	public final static 
	int	DEFAULT_PACKAGE_BUFFER = 100,
		DEFAULT_MAX_SIZE_OF_LIST_OF_FEMALES = 10,
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