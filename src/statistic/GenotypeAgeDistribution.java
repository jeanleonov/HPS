package statistic;

import java.util.Vector;


import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class GenotypeAgeDistribution {

	private Vector<GenotypeAgeNumberTrio> gants;

	public GenotypeAgeDistribution(Vector<GenotypeAgeNumberTrio> gants) {
		this.gants = gants;
	}

	public GenotypeAgeDistribution(int[] ages, int[] genotypes, int[] numbers)
			throws NotSameParametersSizeException {
		if (ages.length == genotypes.length
				&& genotypes.length == numbers.length) {
			gants = new Vector<GenotypeAgeNumberTrio>(ages.length);
			addGants(ages, genotypes, numbers);
		} else
			throw new NotSameParametersSizeException();
	}

	private void addGants(int[] ages, int[] genotypes, int[] numbers) {
		for (int i = 0; i < ages.length; i++) {
			gants.add(new GenotypeAgeNumberTrio(genotypes[i], ages[i],
					numbers[i]));
		}
	}

	public void print() {
		System.out.println("Genotype Age Distribution");
		System.out.println("Ages\tGen-s\tNumbers");

		for (int i = 0; i < gants.size(); i++) {
			gants.elementAt(i).print("\t");
		}
	}

	public void writeToSheet(WritableSheet sheet) throws RowsExceededException,
			WriteException {
		for (GenotypeAgeNumberTrio gant : gants) {
			gant.writeToSheet(sheet);
		}
	}
}
