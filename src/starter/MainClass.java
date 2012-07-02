package starter;

import jade.core.NotFoundException;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;

public class MainClass {
	
	private static Hashtable<String, ArgPair> arguments = new Hashtable<String, ArgPair>();
	private static CmdLineParser parser = new CmdLineParser();
	static Runtime runtime;
	
	private static class ArgPair {
		public CmdLineParser.Option option;
		public Object defaultValue;
		public ArgPair(CmdLineParser.Option opt, Object def) { this.option = opt; this.defaultValue = def; }
	}
	
	public static void main(String[] args) {
		parseArgs(args);
		
		MainClass main = new MainClass();
		main.initContainerControllers();
		main.start();
	}
	
	private static void printUsage() {
        System.out.println(
		"Usage: [--help] [{-y, --years} integer] [{-e, --experiments} integer] [{-M, --multiplier} integer]\n" +
			"       [{-f, --project_path} string] [{-v, --viability} string] [{-m, --map} string]\n" +
			"       [{-p, --posterity} string] [{-s, --scenario} string]\n" +
			"       [{-i, --initiation} string] [{-S, --statistic} string]");
    }
	
	private static void parseArgs(String[] args) {
		arguments.put("help",
				new ArgPair(parser.addBooleanOption("help"), Boolean.FALSE));
		
		arguments.put("years",
				new ArgPair(parser.addIntegerOption('y', "years"), new Integer(100)));
		arguments.put("experiments",
				new ArgPair(parser.addIntegerOption('e', "experiments"), new Integer(10)));
		arguments.put("multiplier",
				new ArgPair(parser.addIntegerOption('M', "multiplier"), new Integer(10)));
		
		arguments.put("project_path",
				new ArgPair(parser.addStringOption('f', "project_path"), Pathes.PROJECT_PATH));
		arguments.put("viability",
				new ArgPair(parser.addStringOption('v', "viability"), "src/starter/Viability.csv"));
		arguments.put("posterity",
				new ArgPair(parser.addStringOption('p', "posterity"), "src/starter/Posterity.csv"));
		arguments.put("movePossibilities",
				new ArgPair(parser.addStringOption('m', "map"), "src/starter/Map.csv"));			// movePossibilities
		arguments.put("scenario",
				new ArgPair(parser.addStringOption('s', "scenario"), "src/starter/Scenario.scn"));
		arguments.put("initiation",
				new ArgPair(parser.addStringOption('i', "initiation"), "src/starter/Initiation.hpsi"));
		arguments.put("statistic",
				new ArgPair(parser.addStringOption('S', "statistic"), "statistic.csv"));
		try {
            parser.parse(args);
        }
        catch(CmdLineParser.OptionException e) {
            System.out.println(e.getMessage());
            printUsage();
            System.exit(2);
        }
		
		if(parser.getOptionValue(arguments.get("help").option) != null) {
			printUsage();
            System.exit(0);
		}
	}
	
	public static Object getArgument(String name) throws NotFoundException {
		if(!arguments.containsKey(name)) throw new NotFoundException();
		ArgPair pair = arguments.get(name);
		return parser.getOptionValue(pair.option, pair.defaultValue);
	}
	
	Vector<ContainerController> containers;
	ContainerController mainContainer;
	
	void initContainerControllers(){
		runtime = Runtime.instance();
		Profile pf = new ProfileImpl(null, 8899, null);
		
		OSInfoOverride osio = new OSInfoOverride();
		try {
			mainContainer = runtime.createMainContainer(pf);
		}
		finally {
			osio.dispose();
		}		
		
		containers = new Vector<ContainerController>();
		//*** YOU SHOULD NOT TO ADD MAIN CONTAINER TO THIS VECTOR (if you run it on cluster)
		//*** BUT YOU SHOULD TO ADD CONTROLLERS OF EACH CONTAINER FROM NODES.
		containers.add(mainContainer);
	}
	
	void start(){		
		AgentController starter;

		try {
			String proj_path = (String)getArgument("project_path");
			
			Object[] startArgs = new Object[] {
					proj_path + (String)getArgument("viability"),
					proj_path + (String)getArgument("posterity"),
					proj_path + (String)getArgument("movePossibilities"),
					proj_path + (String)getArgument("scenario"),
					proj_path + (String)getArgument("initiation"),
					proj_path + (String)getArgument("statistic"),
					(Integer)getArgument("multiplier"),
					containers
			};
			
			starter = mainContainer.createNewAgent("SystemStarter", "starter.SystemStarter", startArgs);
			starter.start();
		}
		catch (StaleProxyException e) {
			e.printStackTrace();
		}
		catch (NotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	

}
