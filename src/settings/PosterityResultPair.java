package settings;

public class PosterityResultPair {
	private Vocabulary.Genome genome;
	private float probability;

	public PosterityResultPair(Vocabulary.Genome genome, float probability) {
		this.genome = genome;
		this.probability = probability;
	}

	public void setGenome(Vocabulary.Genome genome) {
		this.genome = genome;
	}

	public void setProbability(float probability) {
		this.probability = probability;
	}

	public Vocabulary.Genome getGenome() {
		return genome;
	}

	public float getProbability() {
		return probability;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;

		PosterityResultPair pair = (PosterityResultPair) obj;
		return pair.genome == genome && pair.probability == probability;
	}

	public int hashCode() {
		return genome.hashCode() * 1000 + (int) (probability * 100);
	}
}
