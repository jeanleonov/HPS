package settings;

public class PosterityParentsPair implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Vocabulary.Genome male;
	private Vocabulary.Genome female;

	public PosterityParentsPair(Vocabulary.Genome female, Vocabulary.Genome male) {
		this.male = male;
		this.female = female;
	}

	public void setMale(Vocabulary.Genome genome) {
		male = genome;
	}

	public void setFemale(Vocabulary.Genome param) {
		female = param;
	}

	public Vocabulary.Genome getMale() {
		return male;
	}

	public Vocabulary.Genome getFemale() {
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