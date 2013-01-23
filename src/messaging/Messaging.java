package messaging;

public interface Messaging {

	String SCENARIO_COMMANDS= "scenarioCommands";		//0 Experiment -> Zone
//	  String OKKEY			= "ok";						//0 Zone -> Experiment
	
	String START_DIE 		= "Die";					//1 Experiment -> Zone, Zone -> Individual
	  String OKKEY			= "ok";						//1 Individual -> Zone, Zone -> Experiment
	
	String START_MOVE 		= "Move";					//2 Experiment -> Zone, Zone -> Individual
	String DIE				= "Die";					// Zone -> Individual (when immigrating out of Experiment)
	String MIGRATION		= "Migration";				// Zone -> Individual
	String IMMIGRATION		= "Immigration";			// Individual -> Zone
	
	//	  String OKKEY			= "ok";						//2 Individual -> Zone, Zone -> Experiment
	
	String START_FIRST_PHASE	= "LastPhase";				//3 Experiment -> Zone
				
	String CONTINUE_REPRODUCTION = "reproduction";      //3.1.1 Zone --FemaleSet--> Male or Zone--> Female
	String I_WANT_YOU       	 = "iWantYou";			//3.1.2 Male -> Female  (from female set)
//    String OKKEY	        	 = "ok";				//3.1.3 Male -> Zone
	String YES 	            	 = "yes";				//3.1.4 Female -> Male,  Female -> Zone,
	  													//		  Female --create--> Posterity
	  String REGISTER		= "register";				//		  Posterity --register--> Zone
	  String NO				= "no";						//3.1.5 Female -> Male, [Female -> Zone]

	
	String START_EVOLUTION	= "Evolution";				//3.2 Zone -> Posterity
//	DISTRIBUTION  										//3.2 Posterity -> Zone
	
	String START_COMPETITION	= "competition";		//3.3 Zone -> Individual
	  String I_WANT_TO_EAT_YOU	= "iWantToEatYou";		//3.3.1 [MatureIndividual -> Yearling]
	  	String DEREGISTER		= "deregister";			//3.3.1.1 Yearling -> Zone
//	  String OKKEY				= "ok";					//3.3.2 Individual -> Zone
	  	
	String I_FINISHED 		= "iFinished";
	
	
//	  String OKKEY			= "ok";						//3 Zone -> Experiment
	  	
	String I_KILL_YOU		= "kill";
	
	// --- by AAP ---
	String GIVE_ME_YOUR_AGE = "giveAge";				// Zone -> Individual (at the end of last phase
														// while we generating statistic)
	String GIVE_ME_YOUR_GENOTYPE = "giveGenotype";		// Zone -> Individual (-||-)
	
	String STATISTIC = "statisticPackage";				// Zone -> StatisticDispatcher
	String EXPORT = "exportCommand";					// Zone -> StatisticDispatcher
		
	String SCENARIO = "Scenario";						// Expirement -> Zone
	String SURVIVED		= "survived_berries_distribution";
}
