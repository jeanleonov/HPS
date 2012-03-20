package experiment;

import java.io.Serializable;

abstract public class ActionApperance implements Serializable {

	private static final long serialVersionUID = 1L;
	
	abstract void parseActionApperance(String str);

	// localIterationNumber is iterationNumber in rule
	abstract public boolean shouldDoAction(int localIterationNumber);
}
