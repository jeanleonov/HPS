package statistic;

import jade.core.Agent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import zone.ZoneBehaviour;

import jxl.*;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class StatisticDispatcher extends Agent{

	/*
	static int currentRow = 0;

	public static final int EXPERIMENT_ID_POS = 1;
	public static final int ZONE_ID_POS = 2;
	public static final int ITERATION_POS = 3;
	public static final int AGE_POS = 4;
	public static final int GENOTYPE_POS = 5;
	public static final int NUMBER_POS = 6;

	private final String FILE_LOCATION = "statistic.xls";
	*/
	private final String FILE_LOCATION = "statistic.csv";
	private Vector<StatisticPackage> packages;

	@Override
	protected void setup(){
		packages = new Vector<StatisticPackage>();
		addBehaviour(new StatisticDispatcherBehaviour());
	}

	void addPackage(StatisticPackage pack) {
		packages.add(pack);
	}

	void exportToFile() {
		try {
			File file = createFile();
			System.out.println("Statistic" + file.getAbsolutePath());
			/*
			WritableWorkbook workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet("Statistic", 0);
			*/
//			writeTemplate(sheet);
//			writeStatistic(sheet);
			writeStatistic(file);
//			workbook.write();
//			workbook.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		/*
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	*/
		}
	}
	
	private void writeStatistic(File file) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (StatisticPackage pack : packages){
				pack.writeToFile(bw);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		
	}

	/*
	private void writeStatistic(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		for (StatisticPackage pack : packages) {
			pack.writeToSheet(sheet);
		}
	}
	*/
/*
	private void writeTemplate(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		Label label;

		label = new Label(StatisticDispatcher.EXPERIMENT_ID_POS,
				StatisticDispatcher.currentRow, "Experiment Id");
		sheet.addCell(label);
		label = new Label(StatisticDispatcher.ZONE_ID_POS,
				StatisticDispatcher.currentRow, "Zone Id");
		sheet.addCell(label);
		label = new Label(StatisticDispatcher.ITERATION_POS,
				StatisticDispatcher.currentRow, "Iteration");
		sheet.addCell(label);
		label = new Label(StatisticDispatcher.AGE_POS,
				StatisticDispatcher.currentRow, "Age");
		sheet.addCell(label);
		label = new Label(StatisticDispatcher.GENOTYPE_POS,
				StatisticDispatcher.currentRow, "Genotype");
		sheet.addCell(label);
		label = new Label(StatisticDispatcher.NUMBER_POS,
				StatisticDispatcher.currentRow, "Number");
		sheet.addCell(label);

		StatisticDispatcher.currentRow++;
	}
*/
	
	private File createFile() throws IOException {
		File file = new File(FILE_LOCATION);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
}