package posterity;	
import genotype.Genotype;
import jade.core.AID;
import jade.core.Agent;
import settings.PosterityParentsPair;
import settings.Vocabulary;

	public class Posterity extends Agent implements Vocabulary{
		private AID id;
		
		private static final long serialVersionUID = 1L;
				
		@SuppressWarnings("serial")
		protected void setup(){
			//i must to obtain parents. When Female create me, she must send to me 
			//Male Genotype, his fertility and Female Genotype with her fertility		
	    	Object[] args = getArguments();//в аргументах должны быть 2 генотипа
	  		if (args.length < 2)  return;
	  		Genotype male =  (Genotype) args[0];
	  		Float maleFertility = (float) args[1];
	  		Genotype female = (Genotype) args[2];
	  		Float femaleFertility = (float) args[3];
	  		PosterityParentsPair parentsPair = new PosterityParentsPair(female, male);
			this.addBehaviour(new PosterityBehaviour(parentsPair, maleFertility, femaleFertility));
		}
	}
