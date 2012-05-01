package experiment;

import java.io.Serializable;

abstract public class ActionAppearance implements Serializable {
	
	enum Type{
		CYCLIC, SINGLE, POSSIBILITY;
	}

	private static final long serialVersionUID = 1L;

	// localIterationNumber is iterationNumber in rule
	abstract public boolean shouldDoAction(int localIterationNumber);
}
