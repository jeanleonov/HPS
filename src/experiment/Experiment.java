package experiment;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.Vector;

import settings.Settings;
import distribution.ExperimentDistribution;
import distribution.ZoneDistribution;

public class Experiment extends Agent {

	private static final long serialVersionUID = 1L;
	
	private static final String ZONE_CLASS_PATH = "zone.Zone";
	
	Vector<AID> zonesAIDs;
	Integer numberOfModelingYears;
	Integer experimentNumber;
	Integer multiplier;
	Scenario scenario;
	AID myProvider;
	Integer myNodeNumber;
	
	@Override
	protected void setup(){
		zonesAIDs = new Vector<AID>();
		scenario = (Scenario)getArguments()[1];
		numberOfModelingYears = (Integer)getArguments()[2];
		multiplier = (Integer)getArguments()[3];
		experimentNumber = (Integer)getArguments()[4];
		AID statisticAID = (AID)getArguments()[5];
		myProvider = (AID)getArguments()[6];
		myNodeNumber = (Integer)getArguments()[7];
		startZones(createZones(statisticAID));
		Settings.updateZoneTable(zonesAIDs);
		addBehaviour(new ExperimentBehaviour());
	}
	
	private Vector<AgentController> createZones(AID statisticAID){
		ContainerController controller = this.getContainerController();
		Vector<AgentController> zoneAgents = new Vector<AgentController>();
		ExperimentDistribution distribution = (ExperimentDistribution)getArguments()[0];
		int i=0;
		for (ZoneDistribution zoneDistr : distribution.getZoneDistributions()) {
			try {
				zoneAgents.add(
						controller.createNewAgent(
								getZoneName(i),
								ZONE_CLASS_PATH, 
								new Object[]{
											 zoneDistr,
											 experimentNumber,
									  		 i,
									  		 multiplier, 
									  		 statisticAID
									        }
								));											// agent created
			}catch (StaleProxyException e)		{e.printStackTrace();}
			zonesAIDs.add(new AID(getZoneName(i), AID.ISLOCALNAME));		// agent ID saved to private list
			i++;
		}
		return zoneAgents;
	}
	
	private void startZones(Vector<AgentController> zoneAgents){
		for (AgentController agent : zoneAgents){
			try {
				agent.start();									// agent behaviors started
			} catch (StaleProxyException e)		{e.printStackTrace();}
		}
	}
	
	AID getZoneAID(int zoneNumber){
		return zonesAIDs.get(zoneNumber);					// if invalid zoneNumber, then ignore it.		
	}
	
	private String getZoneName(int i){
		return "" + getLocalName() + "_Zone_" + i;
	}
	
}
