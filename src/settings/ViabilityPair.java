package settings;

public class ViabilityPair implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private genotype.Genotype genome;
	private Vocabulary.Param param;

	public ViabilityPair(genotype.Genotype genome, Vocabulary.Param param) {
		this.genome = genome;
		this.param = param;
	}

	public void setGenome(genotype.Genotype genome) {
		this.genome = genome;
	}

	public void setParam(Vocabulary.Param param) {
		this.param = param;
	}

	public genotype.Genotype getGenome() {
		return genome;
	}

	public Vocabulary.Param getParam() {
		return param;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;

		ViabilityPair pair = (ViabilityPair) obj;
		return pair.genome == genome && pair.param == param;
	}

	public int hashCode() {
		return genome.hashCode() * 1000 + param.hashCode();
	}
}