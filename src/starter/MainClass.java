package starter;
import java.io.*;

import experiment.Scenario;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class MainClass {
	public static void main (String args[]) {
		try {
			System.out.println("Starting...");
			Runtime current = Runtime.instance();
			Profile pf = new ProfileImpl(null, 8888, null);
			AgentContainer ac = current.createMainContainer(pf);
			
		    //AgentController rma = ac.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
		    //rma.start();

//==================== Initializing scenario ================================================================
			
			Scenario s = new Scenario();
			
			try{
				BufferedReader scReader = new BufferedReader(new FileReader("scenario.hpss"));
							
				String cur = scReader.readLine();
				while(cur != null){
					s.addRule(cur);
				}
			}
			catch(FileNotFoundException e){
				System.out.println("Invalid scenario file");
			}
					
			
//==================== Running Settings ====================================================================			
			Object[] readers = new Object[2];
			readers[0] = new BufferedReader(new FileReader("../settings/Viability.csv"));
			readers[1] = new BufferedReader(new FileReader("../settings/Posterity.csv"));
			AgentController settingsAgent = ac.createNewAgent("Settings", "settings.Settings", readers);
			settingsAgent.start();
//==========================================================================================================			
			
			Object[] mainArgs = new Object[1];
			mainArgs[0] = s;
			AgentController mainAgent = ac.createNewAgent("MainAgent", "starter.MainAgent", mainArgs);
			mainAgent.start();
			
		} catch(Exception e) {
			e.printStackTrace();
	    }
	}
}