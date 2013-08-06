package experiment.scenario.repeaters;

public class SingleRepeat extends ActionRepeat {
	
	private int iterationNumber;
	
	public SingleRepeat(int iterationNumber) {
		this.iterationNumber = iterationNumber;
	}

	@Override
	public boolean shouldDoAction(int localIterationNumber) {
		return iterationNumber == localIterationNumber;
	}

}
