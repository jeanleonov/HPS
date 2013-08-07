package starter;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;

import utils.individuals.allocation.IndividualsManagerDispatcher;

public class MainClass {
	
	private static Hashtable<String, ArgPair> arguments = new Hashtable<String, ArgPair>();
	private static CmdLineParser parser = new CmdLineParser();
	private static String startArgs="";
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
        DOMConfigurator.configure("src/log4j.xml");
		saveStartArgs(args);
		initArgs();
		parseArgs(args);		
		MainClass main = new MainClass();
		try {
			main.start();
		} catch (Exception exception) {
			Shared.problemsLogger.error(Shared.printStack(exception));
		}
	}
	
	static void start() throws Exception {
		IndividualsManagerDispatcher.setDispatchingMode((Integer)getArgument("object_manager"));
		SystemStarter starter = new SystemStarter(
				getPathesMap(),
				(Integer)getArgument("zone_multiplier"),
				(Double)getArgument("capacity_multiplier"),
				(Integer)getArgument("cur_experiment"),
				(Integer)MainClass.getArgument("number_of_experiments"),
				(Integer)MainClass.getArgument("years"),
				(String)getArgument("statistic"));
		starter.startSystem();
	}
	
	static private Map<SourceType,String> getPathesMap() throws Exception {
		String projectPath = (String)getArgument("project_path");
		Map<SourceType,String> pathesMap = new HashMap<SourceType,String>();
		pathesMap.put(SourceType.VIABILITY, getPathOf("viability", projectPath));
		pathesMap.put(SourceType.POSTERITY, getPathOf("posterity", projectPath));
		pathesMap.put(SourceType.MOVE_POSSIBILITIES, getPathOf("movePossibilities", projectPath));
		pathesMap.put(SourceType.SCENARIO, getPathOf("scenario", projectPath));
		pathesMap.put(SourceType.INITIATION, getPathOf("initiation", projectPath));
		return pathesMap;
	}
	
	static private String getPathOf(String argName, String projectPath) throws Exception {
		return projectPath + '/' + (String)getArgument(argName);
	}
	
	/**
	 * Saves program arguments for future logging.
	 */
	static private void saveStartArgs(String[] args){
		for (int i=0; i<args.length; i++)
			startArgs += args[i];
	}
	 /**
	  * Get program arguments which was saved on start.
	  */
	public static String getStartArgs(){
		return startArgs;
	}
	
	/**
	 * Initialize a set of program arguments. It means "what do we expect to read from console arguments" 
	 */
	private static void initArgs(){
		arguments.put("help", new ArgPair(parser.addBooleanOption("help"), Boolean.FALSE));
		arguments.put("years", new ArgPair(parser.addIntegerOption('y', "years"), new Integer(1)));
		arguments.put("cur_experiment", new ArgPair(parser.addIntegerOption('e', "cur_experiment"), new Integer(-1)));
		arguments.put("number_of_experiments", new ArgPair(parser.addIntegerOption('E', "number_of_experiments"), new Integer(-1)));
		arguments.put("capacity_multiplier", new ArgPair(parser.addDoubleOption('M', "capacity_multiplier"), new Double(1)));
		arguments.put("zone_multiplier", new ArgPair(parser.addIntegerOption('z', "zone_multiplier"), new Integer(1)));
		arguments.put("object_manager", new ArgPair(parser.addIntegerOption('o', "object_manager"), new Integer(0)));
		arguments.put("project_path", new ArgPair(parser.addStringOption('f', "project_path"), Shared.PROJECT_PATH));
		arguments.put("viability", new ArgPair(parser.addStringOption('v', "viability"), Shared.DEFAULT_VIABILITY_FILE));
		arguments.put("posterity", new ArgPair(parser.addStringOption('p', "posterity"), Shared.DEFAULT_POSTERITY_FILE));
		arguments.put("movePossibilities", new ArgPair(parser.addStringOption('m', "map"), Shared.DEFAULT_MAP_FILE));
		arguments.put("scenario", new ArgPair(parser.addStringOption('s', "scenario"), Shared.DEFAULT_SCENARIO_FILE));
		arguments.put("initiation", new ArgPair(parser.addStringOption('i', "initiation"), Shared.DEFAULT_INITIATION_FILE));
		arguments.put("statistic", new ArgPair(parser.addStringOption('S', "statistic"), Shared.DEFAULT_STATISTIC_MODE));
	}
	
	
	/**
	 * Ancillary class which helps to simplify creating of console (program) arguments
	 */
	private static class ArgPair {
		public CmdLineParser.Option option;
		public Object defaultValue;
		public ArgPair(CmdLineParser.Option opt, Object def) {
			this.option = opt;
			this.defaultValue = def;
		}
	}
	
	private static void parseArgs(String[] args) {
		try {
            parser.parse(args);
        }
        catch(CmdLineParser.OptionException e) {
        	Shared.problemsLogger.error(e.getMessage());
            System.out.println(Shared.HELP_TEXT);
            System.exit(2);
        }
		if(parser.getOptionValue(arguments.get("help").option) != null) {
            System.out.println(Shared.HELP_TEXT);
            System.exit(0);
		}
	}
	
	public static Object getArgument(String name) throws Exception {
		if(!arguments.containsKey(name))
			throw new Exception("An argument with name\""+name+"\"was not found!");
		ArgPair pair = arguments.get(name);
		return parser.getOptionValue(pair.option, pair.defaultValue);
	}
}
