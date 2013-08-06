package experiment.individual.genotype;

import java.io.Serializable;

public class Genome implements Serializable{

	private static final long serialVersionUID = 1L;
	
	final static public byte	X = 1,
								Y = 2,
								UNDEF = -1;
	public enum GenomeName{
		R, L;
	}
	
	byte gender;
	GenomeName name;
	
	public Genome(byte gender, GenomeName name){
		this.gender = gender;
		this.name = name;
	}

	public byte getGender() {
		return gender;
	}

	public GenomeName getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;
		return gender == ((Genome)obj).gender
			&& name == ((Genome)obj).name;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append((gender == Y) ? 'y' : 'x');
		str.append((name == GenomeName.L) ? 'L' : 'R');
		return str.toString();
	}
}
