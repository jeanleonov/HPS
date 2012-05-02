package posterity;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import settings.PosterityParentsPair;
import settings.PosterityResultPair;
import settings.ViabilityPair;

public class PosterityBehaviour extends OneShotBehaviour{

	private static final long serialVersionUID = 1L;
	
	PosterityResultPair[] resultsInterbreedingArray=null;
	PosterityParentsPair parents;//!!!!initialize parents!!!!

	ArrayList<PosterityResultPair> resultsInterbreeding;
	
	public PosterityBehaviour(PosterityParentsPair parents){
		this.parents = parents;
	}
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		resultsInterbreedingArray = getProbabilityResultPair();
		//перетаскиваю из массива в список из-за того, что Кирилл отказался передавать ArrayList, но мне это надо
		//а вообще это все равно нужно делать
		if(resultsInterbreedingArray!=null){
			resultsInterbreeding = new ArrayList<PosterityResultPair>();
			for(int i=0;i<resultsInterbreedingArray.length;i++){
				resultsInterbreeding.add(i, resultsInterbreedingArray[i]);
			}
			
		}
		
		for(int i=0; i<resultsInterbreedingArray.length;i++){//??????возможно нужно идти итератором!!!!
			if(resultsInterbreedingArray[i].getProbability() <= 0.5){//если вероятности выжить недостаточно, то 
				resultsInterbreeding.remove(i);						 //выбрасываем эту икринку
			}	
		}

		//сказать Зоне сколько у меня выживших cеголеток
		try {
			sendSurvivedBerries();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	public boolean sendSurvivedBerries() throws IOException{//send to Zone survived berries
		ACLMessage survivedBerries = new ACLMessage(ACLMessage.INFORM);
		survivedBerries.addReceiver(new AID("Zone", AID.ISLOCALNAME));
		survivedBerries.setLanguage("English");
		survivedBerries.setOntology("Pairs of survived berries");
		survivedBerries.setContentObject(resultsInterbreeding);
		myAgent.send(survivedBerries);
		return false;
	}
	
	public PosterityResultPair[] getProbabilityResultPair(){	
		DFAgentDescription template = new DFAgentDescription();
  		ServiceDescription templateSd = new ServiceDescription();
  		templateSd.setType("Settings");
  		template.addServices(templateSd);
  		
  		SearchConstraints sc = new SearchConstraints();
  		// We want to receive 1 result
  		sc.setMaxResults(new Long(1));
  		
  		try {
  			DFAgentDescription[] results = null;
  			while(results == null || results.length < 1) {
  				results = DFService.search(myAgent, template, sc);
  			}
  			DFAgentDescription dfd = results[0];
  			AID provider = dfd.getName();
  			
  			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
			msg.addReceiver(provider);
			try {
				msg.setContentObject(parents);
			}
			catch (Exception ex) { ex.printStackTrace(); }
			myAgent.send(msg);
			
			do { msg = myAgent.blockingReceive(); }
			while(msg.getPerformative() == ACLMessage.FAILURE);
			
			if(msg.getPerformative() != ACLMessage.CONFIRM) throw new NotUnderstoodException("Not understood");
			
			resultsInterbreedingArray = (PosterityResultPair[]) msg.getContentObject();
			
		} catch (FIPAException e) {
			e.printStackTrace();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		
  		return resultsInterbreedingArray;
	}

}
