package individual;

import zone.Zone;
import genotype.Genotype;

public abstract class IndividualsManager {
	
	static private IndividualsManager manager=null;
	
	static public IndividualsManager getManager(){
		if (manager == null)
			manager = new DefaultManager();
		return manager;
	}
	
	static public void setManager(IndividualsManager newManager){
		manager = newManager;
	}

	abstract public Male getMale(Genotype genotype, int age, Zone zone);
	abstract public Female getFemale(Genotype genotype, int age, Zone zone);
	
	abstract public void killMale(Male male);
	abstract public void killFemale(Female female);
}
