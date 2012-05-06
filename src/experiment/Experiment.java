package experiment;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.Vector;

import zone.Pair;

import distribution.ExperimentDistribution;
import distribution.ZoneDistribution;

public class Experiment extends Agent {

	private static final long serialVersionUID = 1L;
	
	private static final String ZONE_CLASS_PATH = "zone.Zone";
	
	Vector<AID> zonesAIDs;
	Integer numberOfModelingYears;
	Integer experimentNumber;
	Scenario scenario;
	AID myProvider;
	
	@Override
	protected void setup(){
		zonesAIDs = new Vector<AID>();
		scenario = (Scenario)getArguments()[1];
		numberOfModelingYears = (Integer)getArguments()[2];
		experimentNumber = (Integer)getArguments()[3];
		AID statisticAID = (AID)getArguments()[4];
		myProvider = (AID)getArguments()[5];
		startZones(createZones(statisticAID));
		addBehaviour(new ExperimentBehaviour());				// implement ExperimentBehaviour and define constructor args
	}
	
	private Vector<AgentController> createZones(AID statisticAID){
		ContainerController controller = this.getContainerController();
		Vector<AgentController> zoneAgents = new Vector<AgentController>();
		ExperimentDistribution distribution = (ExperimentDistribution)getArguments()[0];
		int i=0;
		for (ZoneDistribution zoneDistr : distribution.getZoneDistributions()) {
			try {
				
				// DMY: stub: really neighbours must have been received from initial information
				Vector<Pair<AID, Double>> neighbours = new Vector<Pair<AID, Double>>();
				for(int j = 0; j < i; j++){
					// 1 is also stub
					neighbours.add(new Pair<AID, Double>(new AID(getZoneName(j), AID.ISLOCALNAME), 1.0));
				}
				
				zoneAgents.add(
						controller.createNewAgent(
								getZoneName(i),
								ZONE_CLASS_PATH, 
								new Object[]{
											 zoneDistr,
									  experimentNumber,
									  				 i, 
									  	  statisticAID, 
									         neighbours
									         }
								));											// agent created
			} catch (StaleProxyException e) {
				e.printStackTrace();// TODO Auto-generated catch block
			}
			zonesAIDs.add(new AID(getZoneName(i), AID.ISLOCALNAME));		// agent ID saved to private list
			i++;
		}
		return zoneAgents;
	}
	
	private void startZones(Vector<AgentController> zoneAgents){
		for (AgentController agent : zoneAgents){
			try {
				agent.start();									// agent behaviors started
			} catch (StaleProxyException e) {
				e.printStackTrace();// TODO Auto-generated catch block
			}
		}
	}
	
	AID getZoneAID(int zoneNumber){
		return zonesAIDs.get(zoneNumber);					// if invalid zoneNumber, then ignore it.		
	}
	
	private String getZoneName(int i){
		return "" + getLocalName() + "_Zone_" + i;
	}
	
}
