package starter;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.Vector;

public class MainClass implements Pathes{

	public static void main(String[] args) {
		AgentContainer agentController;
		AgentController starter;
		
		Runtime current = Runtime.instance();
		Profile pf = new ProfileImpl(null, 8899, null);
		agentController = current.createMainContainer(pf);
		
		Object[] startArgs = new Object[]{
										PROJECT_PATH + "src/starter/Viability.csv", 
										PROJECT_PATH + "src/starter/Posterity.csv", 
										PROJECT_PATH + "src/starter/Initiation.hpsi"
										};
		
		try {
			starter = agentController.createNewAgent("SystemStarter", "starter.SystemStarter", startArgs);
			starter.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}

}
