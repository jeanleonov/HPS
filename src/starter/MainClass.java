package starter;

import jade.core.NotFoundException;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.Hashtable;

import org.apache.log4j.xml.DOMConfigurator;

import utils.individuals.allocation.IndividualsManagerDispatcher;
import zone.Zone;

public class MainClass {
	
	private static Hashtable<String, ArgPair> arguments = new Hashtable<String, ArgPair>();
	private static CmdLineParser parser = new CmdLineParser();
	static Runtime runtime;
	static ContainerController container;		
	static AgentController starter;
	private static String startArgs="";
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
        DOMConfigurator.configure("src/log4j.xml");
		saveStartArgs(args);
		initArgs();
		parseArgs(args);		
		MainClass main = new MainClass();
		main.initContainerController();
		main.start();
	}
	
	static void start(){
		try {
			Zone.setCapacityMultiplier((Double)getArgument("capacity_multiplier"));
			Object[] systemStarterArgs = getSystemStarterArgs();
			IndividualsManagerDispatcher.setDispatchingMode((Integer)getArgument("object_manager"));
			starter = container.createNewAgent("SystemStarter", SystemStarter.class.getName(), systemStarterArgs);
			starter.start();
		}
		catch (StaleProxyException e) {
			Shared.problemsLogger.error(e.getMessage());
		}
		catch (NotFoundException e) {
			Shared.problemsLogger.error(e.getMessage());
		} 
	}
	
	static private Object[] getSystemStarterArgs() throws NotFoundException{
		String proj_path = (String)getArgument("project_path");
		Object[] systemStarterArgs = new Object[] {
				proj_path + '/' + (String)getArgument("viability"),
				proj_path + '/' + (String)getArgument("posterity"),
				proj_path + '/' + (String)getArgument("movePossibilities"),
				proj_path + '/' + (String)getArgument("scenario"),
				proj_path + '/' + (String)getArgument("initiation"),
				(Integer)getArgument("zone_multiplier"),
				(Integer)getArgument("cur_experiment"),
				(Boolean)getArgument("display_diagram"),
				(Boolean)getArgument("detailed_diagram"),
				(Boolean)getArgument("sniffer"),
				(Boolean)getArgument("introspector")
		};
		return systemStarterArgs;
	}
	
	static void initContainerController(){
		runtime = Runtime.instance();
		Profile pf = null;
		try {		pf = new ProfileImpl(null, (Integer)getArgument("port") + 8899, null);}
		catch 		(NotFoundException e) { Shared.problemsLogger.error(e.getMessage());}
		OSInfoOverride osio = new OSInfoOverride();
		try {		container = runtime.createMainContainer(pf);}
		finally {	osio.dispose();}
	}
	 
	
	
	static private void saveStartArgs(String[] args){
		for (int i=0; i<args.length; i++)
			startArgs += args[i];
	}
	
	public static String getStartArgs(){
		return startArgs;
	}
	
	private static class ArgPair {
		public CmdLineParser.Option option;
		public Object defaultValue;
		public ArgPair(CmdLineParser.Option opt, Object def) { this.option = opt; this.defaultValue = def; }
	}
	
	private static void initArgs(){
		arguments.put("help", new ArgPair(parser.addBooleanOption("help"), Boolean.FALSE));
		arguments.put("years", new ArgPair(parser.addIntegerOption('y', "years"), new Integer(1)));
		arguments.put("cur_experiment", new ArgPair(parser.addIntegerOption('e', "cur_experiment"), new Integer(-1)));
		arguments.put("number_of_experiments", new ArgPair(parser.addIntegerOption('E', "number_of_experiments"), new Integer(-1)));
		arguments.put("capacity_multiplier", new ArgPair(parser.addDoubleOption('M', "capacity_multiplier"), new Double(1)));
		arguments.put("zone_multiplier", new ArgPair(parser.addIntegerOption('z', "zone_multiplier"), new Integer(1)));
		arguments.put("object_manager", new ArgPair(parser.addIntegerOption('o', "object_manager"), new Integer(0)));
		arguments.put("sniffer", new ArgPair(parser.addBooleanOption("sniffer"), Boolean.FALSE));
		arguments.put("introspector", new ArgPair(parser.addBooleanOption("introspector"), Boolean.FALSE));
		arguments.put("project_path", new ArgPair(parser.addStringOption('f', "project_path"), Shared.PROJECT_PATH));
		arguments.put("viability", new ArgPair(parser.addStringOption('v', "viability"), Shared.DEFAULT_VIABILITY_FILE));
		arguments.put("posterity", new ArgPair(parser.addStringOption('p', "posterity"), Shared.DEFAULT_POSTERITY_FILE));
		arguments.put("display_diagram", new ArgPair(parser.addBooleanOption('d', "display_diagram"), Boolean.FALSE));
		arguments.put("detailed_diagram", new ArgPair(parser.addBooleanOption('D', "detailed_diagram"), Boolean.FALSE));
		//QM
		arguments.put("port", new ArgPair(parser.addIntegerOption('P', "port"), new Integer(0)));
		arguments.put("movePossibilities", new ArgPair(parser.addStringOption('m', "map"), Shared.DEFAULT_MAP_FILE));
		arguments.put("scenario", new ArgPair(parser.addStringOption('s', "scenario"), Shared.DEFAULT_SCENARIO_FILE));
		arguments.put("initiation", new ArgPair(parser.addStringOption('i', "initiation"), Shared.DEFAULT_INITIATION_FILE));
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
	
	public static Object getArgument(String name) throws NotFoundException {
		if(!arguments.containsKey(name)) throw new NotFoundException();
		ArgPair pair = arguments.get(name);
		return parser.getOptionValue(pair.option, pair.defaultValue);
	}
	
	static void shutDown(boolean shouldDisplayDiagram) {
		runtime.shutDown();
		if (!shouldDisplayDiagram)
			System.exit(0);
	}
}
