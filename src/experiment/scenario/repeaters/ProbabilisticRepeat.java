package experiment.scenario.repeaters;

public class ProbabilisticRepeat extends ActionRepeat {
	
	private float possibility;

	public ProbabilisticRepeat(float possibility) {
		this.possibility = possibility;
	}

	@Override
	public boolean shouldDoAction(int localIterationNumber) {
		return Math.random() < possibility;
	}

}
