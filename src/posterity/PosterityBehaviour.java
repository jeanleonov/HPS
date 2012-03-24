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

import settings.PosterityParentsPair;
import settings.PosterityResultPair;
import starter.Pair;

public class PosterityBehaviour extends OneShotBehaviour{
	private static final long serialVersionUID = 1L;
	PosterityParentsPair parents;
	double fertilityMale, fertilityFemale;
	double thisGenotypeViability;
	ArrayList<PosterityResultPair> resultsInterbreeding;

	public PosterityBehaviour(PosterityParentsPair parents){
		this.parents = parents;
	}
	@Override
	public void action() {
		// TODO Auto-generated method stub		
		ArrayList<PosterityResultPair> resultsInterbreedingArray = getProbabilityResultPair();		
		resultsInterbreeding = resultsInterbreedingArray;
		//request to Settings of MaleProlificacy, FemaleProlificacy 		
		/*Calculating number of eggs in this posterity = (probability of appearance each egg)*
		 * (MaleProlificacy * FemaleProfilicacy) */
		
		getGenotypeViability();
		//From genotype's viability calculate number of survived berries
		
		/*for(int i=0; i<resultsInterbreedingArray.size();i++){           //may be I will need it!!!!
			if(resultsInterbreedingArray.get(i).getProbability() <= 0.5){ 
				resultsInterbreeding.remove(i);						 
			}	
		}*/
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
		survivedBerries.setContentObject(resultsInterbreeding);
		myAgent.send(survivedBerries);
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<PosterityResultPair> getProbabilityResultPair(){	
		DFAgentDescription template = new DFAgentDescription();
  		ServiceDescription templateSd = new ServiceDescription();
  		templateSd.setType("Settings");
  		template.addServices(templateSd);
  		
  		SearchConstraints sc = new SearchConstraints();
  		// We want to receive 1 result
  		sc.setMaxResults(new Long(1));
  		ArrayList<PosterityResultPair> resultsInterbreedingArray=null;
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
			resultsInterbreedingArray = (ArrayList<PosterityResultPair>) msg.getContentObject();
			return resultsInterbreedingArray;
		} catch (FIPAException e) {
			e.printStackTrace();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
  		return resultsInterbreedingArray;
  		
	}
	
	public Pair<Double,Double> getFertilityPair(){
		DFAgentDescription template = new DFAgentDescription();
  		ServiceDescription templateSd = new ServiceDescription();
  		templateSd.setType("Settings");
  		template.addServices(templateSd);
  		
  		SearchConstraints sc = new SearchConstraints();
  		// We want to receive 1 result
  		sc.setMaxResults(new Long(1));
  		Pair<Double,Double> fertilities;// = new Pair<Double, Double>();
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
			resultsInterbreedingArray = (ArrayList<PosterityResultPair>) msg.getContentObject();
			return resultsInterbreedingArray;
		} catch (FIPAException e) {
			e.printStackTrace();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
  		return resultsInterbreedingArray;
  	
	}
	
	public double getGenotypeViability(){
		return 0;
	}
}
