package experiment;

public class ProbabilisticAppearance extends ActionAppearance {
	
	private static final long serialVersionUID = 1L;
	
	private float possibility;

	public ProbabilisticAppearance(float possibility) {
		this.possibility = possibility;
	}

	@Override
	public boolean shouldDoAction(int localIterationNumber) {
		return Math.random() < possibility;
	}

}
