package statistic;

import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class GenotypeAgeNumberTrio {

	private int genotype; // not int ???
	private int age;
	private int number;

	public GenotypeAgeNumberTrio(int genotype, int age, int number) {
		this.genotype = genotype;
		this.age = age;
		this.number = number;
	}

	public void print(String separator) {
		System.out.println(genotype + separator + age + separator + number);
	}

	public void writeToSheet(WritableSheet sheet) throws RowsExceededException,
			WriteException {
		Number num;
		num = new Number(StatisticDispatcher.GENOTYPE_POS,
				StatisticDispatcher.currentRow, genotype);
		sheet.addCell(num);
		num = new Number(StatisticDispatcher.AGE_POS,
				StatisticDispatcher.currentRow, age);
		sheet.addCell(num);
		num = new Number(StatisticDispatcher.NUMBER_POS,
				StatisticDispatcher.currentRow, number);
		sheet.addCell(num);

		StatisticDispatcher.currentRow++;
	}
}