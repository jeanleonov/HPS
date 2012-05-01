package experiment;

public class SingleAppearance extends ActionAppearance {
	
	private static final long serialVersionUID = 1L;
	
	private int iterationNumber;
	
	public SingleAppearance(int iterationNumber) {
		this.iterationNumber = iterationNumber;
	}

	@Override
	public boolean shouldDoAction(int localIterationNumber) {
		return iterationNumber == localIterationNumber;
	}

}
