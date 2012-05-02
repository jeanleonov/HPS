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

public class PosterityBehaviour extends OneShotBehaviour{
	private static final long serialVersionUID = 1L;
	PosterityParentsPair parents;
	Float maleFertility, femaleFertility;	
	ArrayList<PosterityResultPair> resultsInterbreeding;

	public PosterityBehaviour(PosterityParentsPair parentsPair,
			Float maleFertility, Float femaleFertility) {
		// TODO Auto-generated constructor stub
		this.parents = parentsPair;
		this.maleFertility = maleFertility;
		this.femaleFertility = femaleFertility;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub		
		ArrayList<PosterityResultPair> resultsInterbreedingArray = getResultPairs();		
		resultsInterbreeding = resultsInterbreedingArray;
		/*IMPORTANT!!!
		 * it is collection of distribution 
		(Genotype, probability_of_appearance_in_this_posterity*viability
		 */
		for(int i=0; i<resultsInterbreedingArray.size();i++){
			if(maleFertility * femaleFertility * resultsInterbreedingArray.get(i).getProbability() <= Math.random())
				resultsInterbreeding.remove(i);
		}	
			
		try {
			sendSurvivedBerries();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	public void sendSurvivedBerries() throws IOException{//send to Zone survived berries
		ACLMessage survivedBerries = new ACLMessage(ACLMessage.INFORM);
		survivedBerries.addReceiver(new AID("Zone", AID.ISLOCALNAME));
		survivedBerries.setContentObject(resultsInterbreeding);
		myAgent.send(survivedBerries);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<PosterityResultPair> getResultPairs(){	
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
	
	
	//this is method of getting viabilities of appropriate genotype
	/*
	public ArrayList<Float> getGenotypeViabilities(){
		DFAgentDescription template = new DFAgentDescription();
  		ServiceDescription templateSd = new ServiceDescription();
  		templateSd.setType("Settings");
  		template.addServices(templateSd);
  		
  		SearchConstraints sc = new SearchConstraints();
  		// We want to receive 1 result
  		sc.setMaxResults(new Long(1));
  		ArrayList<Float> genotypeViabilities=null;
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
				msg.setContentObject(genotypes);//form array of genotypes if this method will exist
			}
			catch (Exception ex) { ex.printStackTrace(); }
			myAgent.send(msg);
			
			do { msg = myAgent.blockingReceive(); }
			while(msg.getPerformative() == ACLMessage.FAILURE);
			
			if(msg.getPerformative() != ACLMessage.CONFIRM) throw new NotUnderstoodException("Not understood");
			genotypeViabilities = (ArrayList<Float>) msg.getContentObject();
			return genotypeViabilities;
		} catch (FIPAException e) {
			e.printStackTrace();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
  		return genotypeViabilities;
	}
	*/
}
