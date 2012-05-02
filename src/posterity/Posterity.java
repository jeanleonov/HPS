package posterity;	
import genotype.Genotype;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import java.io.Reader;
import java.util.HashSet;

import settings.PosterityParentsPair;
import settings.PosterityResultPair;
import settings.Vocabulary;

	public class Posterity extends Agent implements Vocabulary{
		private AID id;
		
		private static final long serialVersionUID = 1L;
				
		@SuppressWarnings("serial")
		protected void setup(){
			//i must to obtain parents. When Female create me, she must send to me Male and Female Genotype
		
	    	Object[] args = getArguments();//� ���������� ������ ���� 2 ��������
	  		if (args.length < 2)  return;
	  		Genotype male =  (Genotype) args[0];
	  		float maleFertility = (float) args[1];
	  		Genotype female = (Genotype) args[1];
	  		float femaleFertility = (float) args[3];
	  		PosterityParentsPair parentsPair = new PosterityParentsPair(female, male);
			this.addBehaviour(new PosterityBehaviour(parentsPair, maleFertility, femaleFertility));
		}
	}
