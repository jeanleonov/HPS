package posterity;

import genotype.Genotype;
import individual.Individual;
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

import messaging.Messaging;
import settings.PosterityParentsPair;
import settings.PosterityResultPair;

public class Posterity implements Messaging{
	private Individual my_mother;
	private PosterityParentsPair parents;
	private float maleFertility;
	private int femaleFertility;
	
	public Posterity(Individual myAgent, Genotype male, Float maleFertility, int femaleFertility){
		my_mother = myAgent;
		Genotype female = my_mother.getGenotype();
		parents = new PosterityParentsPair(female, male);
		this.maleFertility = (float)maleFertility;
		this.femaleFertility = femaleFertility;		
	}
	public void sendSurvivedBerries() {
				ArrayList<PosterityResultPair> survivedBerriesDistribution = calculateSurvivedBerries();
				ACLMessage survivedBerries = new ACLMessage(ACLMessage.INFORM);
				survivedBerries.addReceiver(my_mother.getAID_Zone());
				survivedBerries.setLanguage(SURVIVED);
				try {
					survivedBerries.setContentObject(survivedBerriesDistribution);
				} catch (IOException e) {
					e.printStackTrace();
				}
				my_mother.send(survivedBerries);
	}
	public ArrayList<PosterityResultPair> calculateSurvivedBerries(){
		int posteritySize = (int)(maleFertility * femaleFertility);
		ArrayList<PosterityResultPair> resultsInterbreeding = getResultPairs();		
		for(int i=0; i<resultsInterbreeding.size();i++){
			resultsInterbreeding.get(i).setProbability(resultsInterbreeding.get(i).getProbability()*posteritySize);
		}				
		return resultsInterbreeding;
	}
	
	public ArrayList<PosterityResultPair> getResultPairs(){	
		DFAgentDescription template = new DFAgentDescription();
  		ServiceDescription templateSd = new ServiceDescription();
  		templateSd.setType("Settings");
  		template.addServices(templateSd); 		
  		SearchConstraints sc = new SearchConstraints();
  		sc.setMaxResults(new Long(1));
  		ArrayList<PosterityResultPair> resultsInterbreedingArray=null;

		/*IMPORTANT!!!
		 * it is collection of distribution 
		(genotype, probability_of_appearance_in_this_posterity*viability
		 */
		
  		try {
  			DFAgentDescription[] results = null;
  			while(results == null || results.length < 1) {
  				results = DFService.search(my_mother, template, sc);
  			}
  			DFAgentDescription dfd = results[0];
  			AID provider = dfd.getName();
  			
  			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
			msg.addReceiver(provider);
			try {
				msg.setContentObject(parents);
			}
			catch (Exception ex) { ex.printStackTrace(); }
			my_mother.send(msg);
			
			do { msg = my_mother.blockingReceive(); }
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