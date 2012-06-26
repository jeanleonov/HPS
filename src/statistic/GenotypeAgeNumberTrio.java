package statistic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

public class GenotypeAgeNumberTrio implements Serializable{

	int genotype;
	int age;
	int number;

	GenotypeAgeNumberTrio(int genotype, int age, int number) {
		this.genotype = genotype;
		this.age = age;
		this.number = number;
	}

	// Test version
	void print(String separator) {
		System.out.println(genotype + separator + age + separator + number);
	}
	
	void writeToFile(BufferedWriter bw, String header) throws IOException{
		String str = header + String.valueOf(age) + ";" + String.valueOf(number) +"\n";
		bw.write(str);
	}
}