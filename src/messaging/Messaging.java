package messaging;

public interface Messaging {

	String SCENARIO_COMMANDS= "scenarioCommands";		//0 Experiment -> Zone
//	  String OKKEY			= "ok";						//0 Zone -> Experiment
	
	String START_DIE 		= "die";					//1 Experiment -> Zone, Zone -> Individual
	  String OKKEY			= "ok";						//1 Individual -> Zone, Zone -> Experiment
	
	String START_MOVE 		= "move";					//2 Experiment -> Zone, Zone -> Individual
//	  String OKKEY			= "ok";						//2 Individual -> Zone, Zone -> Experiment
	
	String START_LAST_PHASE	= "lastPhase";				//3 Experiment -> Zone
				
//	FEMALE_SET											//3.1.1 Zone --FemaleSet--> Male
	  String I_WANT_YOU		= "iWantYou";				//3.1.2 Male -> Female  (from female set)
//    String OKKEY			= "ok";						//3.1.3 Male -> Zone
	  String YES 			= "yes";					//3.1.4 Female -> Male,  Female -> Zone,
	  													//		  Female --create--> Posterity
	  String REGISTER		= "register";				//		  Posterity --register--> Zone
	  String NO				= "no";						//3.1.5 Female -> Male, [Female -> Zone]

	
	String START_EVOLUTION	= "evolution";				//3.2 Zone -> Posterity
//	DISTRIBUTION  										//3.2 Posterity -> Zone
	
	String START_COMPETITION	= "competiotion";		//3.3 Zone -> Individual
	  String I_WANT_TO_EAT_YOU	= "iWantToEatYou";		//3.3.1 [MatureIndividual -> Yearling]
	  	String DEREGISTER		= "deregister";			//3.3.1.1 Yearling -> Zone
//	  String OKKEY				= "ok";					//3.3.2 Individual -> Zone
	
	
//	  String OKKEY			= "ok";						//3 Zone -> Experiment
}
