package starter;
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
			
			AgentController mainAgent = ac.createNewAgent("MainAgent", "core.Main.MainAgent", new Object[0]);
			mainAgent.start();
		} catch(Exception e) {
			e.printStackTrace();
	    }
	}
}