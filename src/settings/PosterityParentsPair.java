package settings;

import experiment.individual.genotype.Genotype;

public class PosterityParentsPair {
	
	private Genotype male;
	private Genotype female;

	public PosterityParentsPair(Genotype female, Genotype male) {
		this.male = male;
		this.female = female;
	}

	public void setMale(Genotype genome) {
		male = genome;
	}

	public void setFemale(Genotype param) {
		female = param;
	}

	public Genotype getMale() {
		return male;
	}

	public Genotype getFemale() {
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