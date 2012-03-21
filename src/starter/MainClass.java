package starter;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.io.BufferedReader;
import java.io.FileReader;

public class MainClass implements Pathes{
	public static void main (String args[]) {
		try {
			System.out.println("Starting...");
			Runtime current = Runtime.instance();
			Profile pf = new ProfileImpl(null, 8899, null);
			AgentContainer ac = current.createMainContainer(pf);
			
		    //AgentController rma = ac.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
		    //rma.start();

//==================== Initializing scenario ================================================================
			
	/*LAO_TESTING#		Scenario s = new Scenario();
			
			try{
				BufferedReader scReader = new BufferedReader(new FileReader("scenario.hpss"));
							
				String cur = scReader.readLine();
				while(cur != null){
					s.addRule(cur);
				}
			}
			catch(FileNotFoundException e){
				System.out.println("Invalid scenario file");
			}*/
					
			
//==================== Running Settings ====================================================================			
			Object[] readers = new Object[2];
			readers[0] = new BufferedReader(new FileReader(PROJECT_PATH + "src/settings/Viability.csv"));
			readers[1] = new BufferedReader(new FileReader(PROJECT_PATH + "src/settings/Posterity.csv"));
			AgentController settingsAgent = ac.createNewAgent("Settings", "settings.Settings", readers);
			settingsAgent.start();
//==========================================================================================================			
			
			Object[] mainArgs = new Object[1];
			mainArgs[0] = PROJECT_PATH + "Initiation.hpsi" /*LAO_TESTING#s*/;
			AgentController mainAgent = ac.createNewAgent("MainAgent", "starter.MainAgent", mainArgs);
			mainAgent.start();
			
		} catch(Exception e) {
			e.printStackTrace();
	    }
	}
}