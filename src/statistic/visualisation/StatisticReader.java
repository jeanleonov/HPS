package statistic.visualisation;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class StatisticReader {

	private Map<String, Map<Integer, SortedMap<Integer, Integer>>> genotypeQuantityHistory;
	private BufferedReader reader;
	private int maxIteration=0;
	private int minuteness;
	private int maxQuantity=0;
	private boolean shouldReadDetailedStatistic;
	private boolean shouldDisplayImmatures;
	private String statisticFileURL;
	// | experiment | zone | year | genotype | age | quantity |
	
	public StatisticReader(String statisticFileURL, boolean shouldReadDetailedStatistic, boolean shouldDisplayImmatures) throws IOException {
		this.shouldReadDetailedStatistic = shouldReadDetailedStatistic;
		if (shouldReadDetailedStatistic)
			minuteness = 5;
		else
			minuteness = 1;
		this.shouldDisplayImmatures = shouldDisplayImmatures;
		this.statisticFileURL = statisticFileURL;
		try {
			reader = new BufferedReader(new FileReader(statisticFileURL));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		genotypeQuantityHistory = new HashMap<String, Map<Integer, SortedMap<Integer, Integer>>>();
		read();
	}
	
	public Map<String, Map<Integer, SortedMap<Integer, Integer>>> getGenotypeQuantityHistory() {
		return genotypeQuantityHistory;
	}
	
	public int getMaxQuantity() {
		return maxQuantity;
	}
	
	public int getMaxIteration() {
		return maxIteration*minuteness;
	}
	
	private void read() throws IOException {
		boolean isFileFinished;
		do {
			String line = reader.readLine();
			isFileFinished = readRowAndCheckForEnd(line);
		} while (!isFileFinished);
		reader.close();
		addEmptyIterations();
	}
	
	private boolean readRowAndCheckForEnd(String line) {
		if (line == null)
			return true;
		String[] rowElements = line.split(";");
		if (!shouldDisplayImmatures && rowElements[7].charAt(0) == '-')
			return false;
		int iteration = Integer.parseInt(rowElements[2]);
		int subiteration = Integer.parseInt(rowElements[3]);
		if (!shouldReadDetailedStatistic)
			if (subiteration != 4)
				return false;
		if (iteration > maxIteration)
			maxIteration = iteration;
		String genotype = rowElements[4];
		int age = Integer.parseInt(rowElements[5]);
		int quantity = Integer.parseInt(rowElements[6]);
		addRowToMap(iteration*minuteness + subiteration, genotype, age, quantity);
		return false;
	}
	
	private void addRowToMap(int iteration, String genotype, int age, int quantity) {
		Map<Integer, SortedMap<Integer, Integer>> genotypeHistories = genotypeQuantityHistory.get(genotype);
		if (genotypeHistories == null) {
			genotypeHistories = new HashMap<Integer, SortedMap<Integer, Integer>>();
			genotypeQuantityHistory.put(genotype, genotypeHistories);
		}
		addRowToSubMap(iteration,age,quantity,genotypeHistories);
		addRowToSubMap(iteration,-1,quantity,genotypeHistories);
	}
	
	private void addRowToSubMap(int iteration, int age, int quantity, Map<Integer, SortedMap<Integer, Integer>> genotypeHistories) {
		SortedMap<Integer, Integer> history = genotypeHistories.get(age);
		if (history == null) {
			history = new TreeMap<Integer, Integer>();
			genotypeHistories.put(age, history);
			history.put(iteration, quantity);
		}
		else {
			Integer previusQuantity = history.get(iteration);
			if (previusQuantity == null)
				previusQuantity = 0;
			quantity += previusQuantity;
			history.put(iteration, quantity);
			if (quantity > maxQuantity)
				maxQuantity = quantity;
		}
	}
	
	private void addEmptyIterations() {
		Set<String> genotypes = genotypeQuantityHistory.keySet();
		for (String genotype : genotypes) {
			Map<Integer, SortedMap<Integer, Integer>> genotypeHistory = genotypeQuantityHistory.get(genotype);
			Set<Integer> genotypeAges = genotypeHistory.keySet();
			for(Integer age : genotypeAges){
				SortedMap<Integer, Integer> history = genotypeHistory.get(age);
				for (Integer i=0; i<=maxIteration; i++)
					if (history.get(i) == null)
						history.put(i, 0);
			}
		}
	}
	
	private FileWriter writer;
	
	public void calculateAndPrintPiramid() {
		saveFields();
		shouldDisplayImmatures = false;
		shouldReadDetailedStatistic = false;
		genotypeQuantityHistory = new HashMap<String, Map<Integer, SortedMap<Integer, Integer>>>();
		try {
			reader = new BufferedReader(new FileReader(statisticFileURL));
			writer = new FileWriter(new File(statisticFileURL.substring(0,statisticFileURL.indexOf('.'))+".txt"));
			read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<String> genotypes = genotypeQuantityHistory.keySet();
		for (String genotype : genotypes) {
			SortedMap<Integer, Integer> ageCountMap = new TreeMap<Integer, Integer>();
			Map<Integer, SortedMap<Integer, Integer>> genotypeHistory = genotypeQuantityHistory.get(genotype);
			Set<Integer> genotypeAges = genotypeHistory.keySet();
			Integer summeryCount = 0;
			for(Integer age : genotypeAges){
				if (age == -1) continue;
				SortedMap<Integer, Integer> history = genotypeHistory.get(age);
				Integer ageCount = 0;
				for (Integer i=30; i<=maxIteration; i++)
					ageCount += history.get(i);
				ageCountMap.put(age, ageCount);
				summeryCount += ageCount;
			}
			printGenotypePiramid(genotype, ageCountMap, summeryCount);
		}
		restoreFields();
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printGenotypePiramid(String genotype, SortedMap<Integer, Integer> ageCountMap, Integer summeryCount) {
		StringBuilder builder = new StringBuilder();
		builder.append("\n").append(genotype).append(":\n");
		Set<Integer> ages = ageCountMap.keySet();
		for (Integer age : ages) {
			builder.append("  ").append(age).append("  -  ");
			builder.append(ageCountMap.get(age)*100.0/summeryCount).append("%\n");
		}
		builder.append("  Из половозрелых в год выживает: ");
		builder.append((summeryCount-ageCountMap.get(ageCountMap.firstKey()))*100.0/summeryCount).append("%\n");
		try {
			writer.write(builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, Map<Integer, SortedMap<Integer, Integer>>> genotypeQuantityHistoryTemp;
	private int maxIterationTemp;
	private int minutenessTemp;
	private int maxQuantityTemp;
	private boolean shouldReadDetailedStatisticTemp;
	private boolean shouldDisplayImmaturesTemp;
	
	private void saveFields() {
		genotypeQuantityHistoryTemp = genotypeQuantityHistory;
		maxIterationTemp = maxIteration;
		minutenessTemp = minuteness;
		maxQuantityTemp = maxQuantity;
		shouldReadDetailedStatisticTemp = shouldReadDetailedStatistic;
		shouldDisplayImmaturesTemp = shouldDisplayImmatures;
	}
	
	private void restoreFields() {
		genotypeQuantityHistory = genotypeQuantityHistoryTemp;
		maxIteration = maxIterationTemp;
		minuteness = minutenessTemp;
		maxQuantity = maxQuantityTemp;
		shouldReadDetailedStatistic = shouldReadDetailedStatisticTemp;
		shouldDisplayImmatures = shouldDisplayImmaturesTemp;
	}
}
