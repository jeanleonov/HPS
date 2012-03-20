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
	
	// by DMY
	public static GenotypeAgeNumberTrio parseGenotype(String resource) throws NumberFormatException{
		// Later I'm plan to throw my own exception, if it will be necessary 
		
		String[] t = resource.split(" \n");
		
		if(t.length % 3 != 0){
			throw new NumberFormatException();
		}
		
		return new GenotypeAgeNumberTrio(Genotype.getGenotype(t[0]), Integer.parseInt(t[1]), Integer.parseInt(t[2]));
	}
}
