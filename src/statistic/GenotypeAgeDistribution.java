package statistic;

import java.io.Serializable;
import java.util.ArrayList;

public class GenotypeAgeDistribution implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<GenotypeAgeCountTrioStat> gants;

	public GenotypeAgeDistribution() {
		gants = new ArrayList<GenotypeAgeCountTrioStat>();
	}
	
	/**
	 * ����������� � ���������� ������: {�������, �������, ����������}
	 * ����������, ���� �� ��������� ������� ��������
	 * @param ages
	 * @param genotypes
	 * @param numbers
	 * @throws NotSameParametersSizeException
	 */
	public GenotypeAgeDistribution(int[] ages, int[] genotypes, boolean[] isMature, int[] numbers)
			throws NotSameParametersSizeException {
		if (ages.length == genotypes.length && genotypes.length == numbers.length) {
			gants = new ArrayList<GenotypeAgeCountTrioStat>(ages.length);
			addGants(ages, genotypes, isMature, numbers);
		} else
			throw new NotSameParametersSizeException();
	}
	
	/**
	 * �������� �����, ����������������� ��������� � ���������
	 * @param genotype
	 * @param age
	 */
	public void addToGant(int genotype, int age, boolean isMature){
		if (tryToInsertInGant(genotype, age, isMature));
		else
			gants.add(new GenotypeAgeCountTrioStat(genotype, age, isMature, 1));
	}

	/**
	 * ��������� ���������� ������ ������� �������� � ��������, ���� ������������
	 * @param genotype
	 * @param age
	 * @return ������������ �� ����� �����
	 */
	private boolean tryToInsertInGant(int genotype, int age, boolean isMature) {
		for (GenotypeAgeCountTrioStat gant : gants)
			if (gant.genotype == genotype && gant.age == age){
				gant.number++;
				return true;
			}
		return false;
	}

	/**
	 * �������� ����� ������: {�������, �������, ����������}
	 * @param ages
	 * @param genotypes
	 * @param numbers
	 */
	private void addGants(int[] ages, int[] genotypes, boolean[] isMature, int[] numbers) {
		for (int i = 0; i < ages.length; i++)
			gants.add(new GenotypeAgeCountTrioStat(genotypes[i], ages[i], isMature[i],
					numbers[i]));
	}
	
	public void setDifferencesWith(GenotypeAgeDistribution previousGAD) {
		if (previousGAD == null) {
			for (GenotypeAgeCountTrioStat gact : gants)
				gact.difference = 0;
			return;
		}
		for (GenotypeAgeCountTrioStat gact : gants) {
			boolean isPrevGactFound = false;
			for (GenotypeAgeCountTrioStat prevGACT : previousGAD.gants)
				if (prevGACT.genotype == gact.genotype && prevGACT.age == gact.age) {
					gact.difference = gact.number - prevGACT.number;
					isPrevGactFound = true;
					break;
				}
			if (!isPrevGactFound)
				gact.difference = gact.number;
		}
	}

	/**
	 * ����������� ��������� ������
	 */
	public void print() {
		System.out.println("Genotype Age Distribution");
		System.out.println("Ages\tGen-s\tNumbers");
		for (int i = 0; i < gants.size(); i++)
			gants.get(i).print("\t");
	}
	
	public String toString(String header) {
		StringBuffer stbuf = new StringBuffer();
		for (GenotypeAgeCountTrioStat gant : gants)
			stbuf.append(header + gant + "\r\n");
		return stbuf.toString();
	}
	
}
