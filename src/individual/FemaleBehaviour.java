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

import posterity.Posterity;

import settings.PosterityParentsPair;
import settings.PosterityResultPair;

public class FemaleBehaviour extends IndividualBehaviour {

	private static final long serialVersionUID = 1L;


	@Override
	protected void reproduce(Object msgContent) {
		// TODO Auto-generated method stub
		
		/*PROCESS OF REPRODUCTION
		 *male and maleFertility will be known after it;*/
		
//=============== Form posterity ==========================================		
		Individual ind = (Individual)myAgent;		
		Genotype male = null;
		Float maleFertility= (float) 0.5;
		int femaleFertility = (int)((float)(ind.getSetting(settings.Vocabulary.Param.Fertility)));
		Posterity posterity = new Posterity(ind, male,maleFertility, femaleFertility);
		posterity.sendSurvivedBerries();
	}
}
		
