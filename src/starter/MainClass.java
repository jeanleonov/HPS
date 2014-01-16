package starter;

import org.apache.log4j.xml.DOMConfigurator;

import utils.MemoryLogger;
import utils.cmd.line.parser.CmdLineParser;

public class MainClass {
	
	private static String startArgs="";

	public static void main(String[] args) {
		try {
			saveStartArgs(args);
			parseArgs(args);
			DOMConfigurator.configure((String)Argument.LOG4J_FOLDER.getValue());
			new SystemStarter().startSystem();
			MemoryLogger.get().finish();
		} catch (Exception e) {
			Shared.problemsLogger.error(Shared.printStack(e));
			e.printStackTrace();
		}
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
