package settings;

public class PosterityResultPair {
	private genotype.Genotype genome;
	private float probability;

	public PosterityResultPair(genotype.Genotype genome, float probability) {
		this.genome = genome;
		this.probability = probability;
	}

	public void setGenome(genotype.Genotype genome) {
		this.genome = genome;
	}

	public void setProbability(float probability) {
		this.probability = probability;
	}

	public genotype.Genotype getGenome() {
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
