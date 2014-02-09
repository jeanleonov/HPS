package statistic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import experiment.individual.genotype.Genotype;
import starter.Shared;
import statistic.StatisticSettings.Subiteration;

public class LastYearStatisticWriter {
	
	private Map<String, String> pointValues;
	private int pointNumber;
	private BufferedWriter shortStatisticWriter = null;

	private ArrayList<Genotype> columnGenotypes = new ArrayList<>();
	private ArrayList<Integer> columnAges = new ArrayList<>();

	public LastYearStatisticWriter(String fileURL) throws IOException {
		FileWriter fileWriter = new FileWriter(fileURL, true);
		shortStatisticWriter = new BufferedWriter(fileWriter);
	}
	
	public void write(YearStatistic statistic) throws IOException {
		String rendered = renderYear(statistic);
		shortStatisticWriter.write(rendered);
		shortStatisticWriter.flush();
	}
	
	public void openNewPoint(Map<String, String> pointValues, int pointNumber) {
		this.pointValues = pointValues;
		this.pointNumber = pointNumber;
		try {
			shortStatisticWriter.write(getPointHeader());
		}catch (IOException e) {
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append("Writting of point header was failed!\n");
			errorMsg.append(Shared.printStack(e));
			Shared.problemsLogger.error(errorMsg.toString());
		}
	}
	
	private String getPointHeader() {
		StringBuilder result = new StringBuilder();
		result.append(renderPointValues());
		result.append(getGenotypesColumns());
		return result.toString();
	}
	
	private String renderPointValues() {
		StringBuilder result = new StringBuilder("\n\nPoint:;").append(pointNumber);
		result.append('\n');
		for (String name : pointValues.keySet())
			result.append(name).append(';');
		result.append('\n');
		for (String name : pointValues.keySet())
			result.append(pointValues.get(name)).append(';');
		return result.toString();
	}
	
	private String getGenotypesColumns() {
		StringBuilder columns = new StringBuilder();
		columnGenotypes.clear();
		columnAges.clear();
		columns.append("\nZone;");
		for (Genotype genotype : Genotype.getAll()) {
			columns.append(genotype.toString()).append(';');
			columnGenotypes.add(genotype);
			columnAges.add(YearStatisticCollector.TOTAL_AGE);
		}
		columns.deleteCharAt(columns.length()-1);
		return columns.toString();
	}
	
	private String renderYear(YearStatistic statistic) {
		StringBuilder buffer = new StringBuilder();
		int subiteration = Subiteration.AFTER_MOVE_AND_SCENARIO.ordinal();
		buffer.append(renderSubiteration(statistic.getYearStatistic().get(subiteration)));
		return buffer.toString();
	}
	
	private StringBuilder renderSubiteration(Map<String, Map<Integer, Map<Integer, Integer>>> subiterationStat) {
		StringBuilder buffer = new StringBuilder();
		for (String zone : subiterationStat.keySet()) {
			buffer.append('\n');
			buffer.append(zone).append(';');
			buffer.append(renderZone(subiterationStat.get(zone)));
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer;
	}
	
	private StringBuilder renderZone(Map<Integer, Map<Integer, Integer>> zoneStat) {
		StringBuilder buffer = new StringBuilder();
		for (int i=0; i<columnGenotypes.size(); i++) {
			int genotypeId = columnGenotypes.get(i).getId();
			Map<Integer, Integer> genotypeStat = zoneStat.get(genotypeId);
			if (genotypeStat == null)
				buffer.append("0;");
			else {
				int age = columnAges.get(i);
				Integer indivsNumber = genotypeStat.remove(age);
				if (indivsNumber == null)
					buffer.append("0;");
				else
					buffer.append(indivsNumber).append(';');
			}
		}
		return buffer;
	}
	
	public void finish() throws IOException {
		shortStatisticWriter.flush();
		if (shortStatisticWriter == null)
			shortStatisticWriter.close();
	}
}
