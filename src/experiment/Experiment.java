package experiment;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.Vector;

import distribution.ExperimentDistribution;
import distribution.ZoneDistribution;

public class Experiment extends Agent {

	private static final long serialVersionUID = 1L;
	
	private Vector<AID> zonesAIDs;

	// TODO
	
	@Override
	protected void setup(){
		ContainerController controller = this.getContainerController();
		Vector<AgentController> zoneAgents = new Vector<AgentController>();
		zonesAIDs = new Vector<AID>();
		
		ExperimentDistribution distribution = (ExperimentDistribution)getArguments()[0];
		Scenario scenario = (Scenario)getArguments()[1];
		
		int i=0;
		for (ZoneDistribution zoneDistr : distribution.getZoneDistributions()) {
			Object[] objs = new Object[2];
			objs[0] = zoneDistr;
			objs[1] = getAID();
			String name = "" + getLocalName() + "_Zone_" + i;
			try {
				zoneAgents.add(controller.createNewAgent(name, "zone.Zone", objs));			// agent created
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			zonesAIDs.add(new AID(name, AID.ISLOCALNAME));		// agent ID saved to private list
			i++;
		}
		
		for (AgentController agent : zoneAgents){
			try {
				agent.start();									// agent behaviors started
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		addBehaviour(new ExperimentBehaviour(scenario/*,???*/));			// implement ExperimentBehaviour and define constructor args
	}
	
}
