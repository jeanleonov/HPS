package distribution;

import genotype.Genotype;

import java.io.Serializable;

public class GenotypeAgeNumberTrio implements Serializable {

	private static final long serialVersionUID = 1L;

	private Genotype genotype;
	private int age;
	private int number;
	
	public GenotypeAgeNumberTrio(Genotype genotype, int age, int number) {
		this.genotype = genotype;
		this.age = age;
		this.number = number;
	}

	public Genotype getGenotype() {
		return genotype;
	}

	public int getAge() {
		return age;
	}

	public int getNumber() {
		return number;
	}
}
