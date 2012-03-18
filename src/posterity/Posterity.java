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
			
			String nickname = "Posterity";
	    	id = new AID(nickname,AID.ISLOCALNAME);  
	    	Object[] args = getArguments();//в аргументах должны быть 2 генотипа
	  		if (args.length < 2)
	  			return;
	  		Genotype male =  (Genotype) args[0];
	  		Genotype female = (Genotype) args[1];
	  		PosterityParentsPair parentsPair = new PosterityParentsPair(female,male);
			this.addBehaviour(new PosterityBehaviour(parentsPair));
			//получим пару родителей и образуем результирующую пару и передадим ее зоне
			
		}
	}
