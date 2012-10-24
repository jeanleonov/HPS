package statistic;

import java.io.Serializable;
import java.util.Vector;

public class GenotypeAgeDistribution implements Serializable {

	private static final long serialVersionUID = 1L;
	private Vector<GenotypeAgeCountTrioStat> gants;

	public GenotypeAgeDistribution() {
		gants = new Vector<GenotypeAgeCountTrioStat>();
	}
	
	/**
	 * Конструктор с установкой особей: {возраст, генотип, количество}
	 * Исключение, если не совпадают размеры массивов
	 * @param ages
	 * @param genotypes
	 * @param numbers
	 * @throws NotSameParametersSizeException
	 */
	public GenotypeAgeDistribution(int[] ages, int[] genotypes, int[] numbers)
			throws NotSameParametersSizeException {
		if (ages.length == genotypes.length
				&& genotypes.length == numbers.length) {
			gants = new Vector<GenotypeAgeCountTrioStat>(ages.length);
			addGants(ages, genotypes, numbers);
		} else
			throw new NotSameParametersSizeException();
	}
	
	/**
	 * Добавить особь, характеризующуюся генотипом и возрастом
	 * @param genotype
	 * @param age
	 */
	public void addToGant(int genotype, int age){
		if (tryToInsertInGant(genotype, age));
		else
			gants.add(new GenotypeAgeCountTrioStat(genotype, age, 1));
	}

	/**
	 * Увеличить количество особей данного генотипа и возраста, если присутствует
	 * @param genotype
	 * @param age
	 * @return Присутствует ли такая особь
	 */
	private boolean tryToInsertInGant(int genotype, int age) {
		for (GenotypeAgeCountTrioStat gant : gants){
			if (gant.genotype == genotype && gant.age == age){
				gant.number++;
				return true;
			}
		}
		return false;
	}

	/**
	 * Добавить набор особей: {возраст, генотип, количество}
	 * @param ages
	 * @param genotypes
	 * @param numbers
	 */
	private void addGants(int[] ages, int[] genotypes, int[] numbers) {
		for (int i = 0; i < ages.length; i++) {
			gants.add(new GenotypeAgeCountTrioStat(genotypes[i], ages[i],
					numbers[i]));
		}
	}

	/**
	 * Распечатать коллекцию особей
	 */
	public void print() {
		System.out.println("Genotype Age Distribution");
		System.out.println("Ages\tGen-s\tNumbers");

		for (int i = 0; i < gants.size(); i++) {
			gants.elementAt(i).print("\t");
		}
	}
	
	public String toString(String header) {
		StringBuffer stbuf = new StringBuffer();
		for (GenotypeAgeCountTrioStat gant : gants){
			stbuf.append(header + gant + "\r\n");
		}
		
		return stbuf.toString();
	}
	
}
