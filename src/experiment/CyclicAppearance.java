package experiment;

public class CyclicAppearance extends ActionAppearance {

	private static final long serialVersionUID = 1L;
	
	private float cycleLength;
	
	public CyclicAppearance(float cycleLength) {
		this.cycleLength = cycleLength;
	}

	@Override
	public boolean shouldDoAction(int localIterationNumber) {
		// TODO test it
		return (int) ((int) (localIterationNumber/cycleLength) * cycleLength)   == localIterationNumber
			|| (int) ((int) (localIterationNumber/cycleLength) * cycleLength)+1 == localIterationNumber;
	}

}
