package individual;

import genotype.Genotype;
import jade.core.AID;
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

public class FemaleBehaviour extends IndividualBehaviour {

	private static final long serialVersionUID = 1L;
	PosterityParentsPair parents;
	ArrayList<PosterityResultPair> resultsInterbreeding=null;
	float maleFertility; 
	float femaleFertility;
	Individual my_mother;
	public FemaleBehaviour(Genotype male, Float maleFertility) {
		// TODO Auto-generated constructor stub
		my_mother = (Individual)myAgent;
		Genotype female = (Genotype) my_mother.getGenotype();
		this.parents = new PosterityParentsPair(male, female);
		this.maleFertility = (float)maleFertility;
		this.femaleFertility = my_mother.getSetting(settings.Vocabulary.Param.Fertility);
	}

	public void action() {
		// TODO Auto-generated method stub		
		ArrayList<PosterityResultPair> resultsInterbreeding = getResultPairs();				
		int posteritySize = (int)maleFertility * (int)femaleFertility;
		/*IMPORTANT!!!
		 * it is collection of distribution 
		(genotype, probability_of_appearance_in_this_posterity*viability
		 */
		for(int i=0; i<resultsInterbreeding.size();i++){
			resultsInterbreeding.get(i).setProbability(resultsInterbreeding.get(i).getProbability()*posteritySize);
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
		survivedBerries.addReceiver(my_mother.getAID_Zone());
		survivedBerries.setContentObject(resultsInterbreeding);
		my_mother.send(survivedBerries);
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
	
}
