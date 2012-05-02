package settings;

public class PosterityParentsPair implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private genotype.Genotype male;
	private genotype.Genotype female;

	public PosterityParentsPair(genotype.Genotype female, genotype.Genotype male) {
		this.male = male;
		this.female = female;
	}

	public void setMale(genotype.Genotype genome) {
		male = genome;
	}

	public void setFemale(genotype.Genotype param) {
		female = param;
	}

	public genotype.Genotype getMale() {
		return male;
	}

	public genotype.Genotype getFemale() {
		return female;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;

		PosterityParentsPair pair = (PosterityParentsPair) obj;
		return pair.male == male && pair.female == female;
	}

	public int hashCode() {
		return male.hashCode() * 1000 + female.hashCode();
	}
}