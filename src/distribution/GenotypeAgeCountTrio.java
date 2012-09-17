package distribution;

import genotype.Genotype;

import java.io.Serializable;

public class GenotypeAgeCountTrio implements Serializable {

	private static final long serialVersionUID = 1L;

	private Genotype genotype;
	private int age;
	private int number;
	
	public GenotypeAgeCountTrio(Genotype genotype, int age, int number) {
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
	public static GenotypeAgeCountTrio parseGenotype(String resource) throws NumberFormatException{
		// Later I'm plan to throw my own exception, if it will be necessary 
		
		String[] t = resource.split(" ");
		
		for(int i = 0; i < t.length; i++){
			if((t[i] == null) || (t[i].equals("")))
				for(int j = i; j < t.length - 1; j++ ){
					t[j] = t[j + 1]; 
				}
		}
		if(t.length < 3){
			throw new NumberFormatException();
		}
		
		
		return new GenotypeAgeCountTrio(Genotype.getGenotype(t[0]), Integer.parseInt(t[1]), Integer.parseInt(t[2]));
	}
	
	public String toString(){
		return genotype.toString() + " " + age + " " + number;
	}
}
