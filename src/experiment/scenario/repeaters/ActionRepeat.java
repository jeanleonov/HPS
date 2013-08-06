package experiment.scenario.repeaters;


abstract public class ActionRepeat {
	
	enum Type{
		CYCLIC,
		SINGLE,
		POSSIBILITY;
	}

	// localIterationNumber is iterationNumber in rule
	abstract public boolean shouldDoAction(int localIterationNumber);
}
