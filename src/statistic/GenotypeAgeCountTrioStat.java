package statistic;

import java.io.Serializable;
import genotype.Genotype;

public class GenotypeAgeCountTrioStat implements Serializable{

	private static final long serialVersionUID = 1L;
	int genotype;
	int age;
	boolean isMature;
	int number;
	int difference;

	GenotypeAgeCountTrioStat(int genotype, int age, boolean isMature, int number) {
		this.genotype = genotype;
		this.age = age;
		this.isMature = isMature;
		this.number = number;
	}

	void print(String separator) {
		System.out.println(genotype + separator + age + separator + number);
	}
	
	public String toString() {
		return	Genotype.getGenotypeById(genotype) + ";" + 
				age + ";" + 
				number + ";" + 
				(isMature? "+" : "-") + ";" + 
				difference;
	}
}