package starter;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.Vector;

public class MainClass implements Pathes{

	public static void main(String[] args) {
		MainClass main = new MainClass();
		main.initContainerControllers();
		main.start();
	}
	
	Vector<ContainerController> containers;
	ContainerController mainContainer;
	
	void initContainerControllers(){
		Runtime current = Runtime.instance();
		Profile pf = new ProfileImpl(null, 8899, null);
		mainContainer = current.createMainContainer(pf);
		
		containers = new Vector<ContainerController>();
		//*** YOU SHOULD NOT TO ADD MAIN CONTAINER TO THIS VECTOR (if you run it on cluster)
		//*** BUT YOU SHOULD TO ADD CONTROLLERS OF EACH CONTAINER FROM NODES.
		containers.add(mainContainer);
	}
	
	void start(){		
		AgentController starter;
		Object[] startArgs = new Object[]{
										PROJECT_PATH + "src/starter/Viability.csv", 
										PROJECT_PATH + "src/starter/Posterity.csv", 
										PROJECT_PATH + "src/starter/Initiation.hpsi",
										containers};
		try {
			starter = mainContainer.createNewAgent("SystemStarter", "starter.SystemStarter", startArgs);
			starter.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	

}
