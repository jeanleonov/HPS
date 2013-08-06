package experiment.scenario.repeaters;

public class CyclicRepeat extends ActionRepeat {
	
	private float cycleLength;
	
	public CyclicRepeat(float cycleLength) {
		this.cycleLength = cycleLength;
	}

	@Override
	public boolean shouldDoAction(int localIterationNumber) {
		// TODO test it
		return (int) ((int) (localIterationNumber/cycleLength) * cycleLength)   == localIterationNumber
			|| (int) ((int) (localIterationNumber/cycleLength) * cycleLength)+1 == localIterationNumber;
	}

}
