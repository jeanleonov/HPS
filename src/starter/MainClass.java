package starter;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;

import utils.cmd.line.parser.Argument;
import utils.cmd.line.parser.CmdLineParser;
import utils.individuals.allocation.IndividualsManagerDispatcher;

public class MainClass {
	
	private static String startArgs="";
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
        DOMConfigurator.configure("src/log4j.xml");
		saveStartArgs(args);
		parseArgs(args);		
		MainClass main = new MainClass();
		try {
			main.start();
		} catch (Exception e) {
			Shared.problemsLogger.error(Shared.printStack(e));
			e.printStackTrace();
		}
	}
	
	static void start() throws Exception {
		IndividualsManagerDispatcher.setDispatchingMode((Integer) Argument.OBJECT_MANAGER.getValue());
		SystemStarter starter = new SystemStarter(
				getPathesMap(),
				(Integer) Argument.ZONE_MULTIPLIER.getValue(),
				(Double) Argument.CAPACITY_MULTIPLIER.getValue(),
				(Integer) Argument.CURRENT_EXPERIMENT.getValue(),
				(Integer) Argument.NUMBER_OF_EXPERIMENTS.getValue(),
				(Integer) Argument.YEARS.getValue(),
				(String) Argument.STATISTIC.getValue());
		starter.startSystem();
	}
	
	static private Map<Input,String> getPathesMap() throws Exception {
		String projectPath = (String) Argument.PROJECT_PATH.getValue();
		Map<Input,String> pathesMap = new HashMap<Input,String>();
		pathesMap.put(Input.VIABILITY, getPathOf(Argument.VIABILITY, projectPath));
		pathesMap.put(Input.POSTERITY, getPathOf(Argument.POSTERITY, projectPath));
		pathesMap.put(Input.MOVE_POSSIBILITIES, getPathOf(Argument.MOVE_POSSIBILITIES, projectPath));
		pathesMap.put(Input.SCENARIO, getPathOf(Argument.SCENARIO, projectPath));
		pathesMap.put(Input.INITIATION, getPathOf(Argument.INITIATION, projectPath));
		return pathesMap;
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
            System.out.println(Shared.SINGLE_RUN_HELP_TEXT);
            System.exit(2);
        }
		if((Boolean) Argument.HELP.getValue()) {
            System.out.println(Shared.SINGLE_RUN_HELP_TEXT);
            System.exit(0);
		}
	}
}
