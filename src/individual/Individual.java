package individual;

import java.util.ArrayList;
import settings.ViabilityPair;
import genotype.*;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.lang.acl.MessageTemplate;

public class Individual extends Agent {

	private static final long serialVersionUID = 1L;
	
	private Genotype myGenotype;
	protected int age;
	ArrayList<ViabilityPair> settings;
	
	@Override
	protected void setup() {
		Object[] args = getArguments();
		if (args.length < 2)
			return;
		myGenotype = (Genotype) args[0];
		age = (Integer) args[1];

	/*	GetSettings();#temporary*/
		BehaviourRegister();
	}
	
	private void GetSettings() {
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
  				try {
  					results = DFService.search(this, template, sc);
  				}
  				catch(FailureException e) {
  					System.out.println("-_-");
  				}
  			}
  			DFAgentDescription dfd = results[0];
  			AID provider = dfd.getName();
  			
  			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
			msg.addReceiver(provider);
			try {
				msg.setContentObject(myGenotype);
			}
			catch (Exception ex) { ex.printStackTrace(); }
			send(msg);
			
			do { msg = blockingReceive(MessageTemplate.MatchSender( provider )); }
			while(msg.getPerformative() == ACLMessage.FAILURE);
			
			if(msg.getPerformative() != ACLMessage.CONFIRM) {
				throw new NotUnderstoodException("Not understood");
			}
			
			settings = (ArrayList<ViabilityPair>) msg.getContentObject();
			
		} catch (FIPAException e) {
			e.printStackTrace();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}
	
	protected Float getSetting(settings.Vocabulary.Param param) {
		for(settings.ViabilityPair pair : settings) {
			if(pair.getParam() == param) return pair.getValue();
		}
		return 0f;
	}
	
	private void BehaviourRegister() {
		if (myGenotype.getGender() == Genome.X)
			addBehaviour(new FemaleBehaviour());
		else addBehaviour(new MaleBehaviour());
	}
}
