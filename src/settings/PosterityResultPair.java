package settings;

public class PosterityResultPair {
	private genotype.Genotype genotype;
	private float probability;

	public PosterityResultPair(genotype.Genotype genome, float probability) {
		this.genotype = genome;
		this.probability = probability;
	}

	public void setGenome(genotype.Genotype genome) {
		this.genotype = genome;
	}

	public void setProbability(float probability) {
		this.probability = probability;
	}

	public genotype.Genotype getGenotype() {
		return genotype;
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
		return pair.genotype == genotype && pair.probability == probability;
	}

	public int hashCode() {
		return genotype.hashCode() * 1000 + (int) (probability * 100);
	}
}
