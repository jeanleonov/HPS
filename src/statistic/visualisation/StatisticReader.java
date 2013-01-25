package statistic.visualisation;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class StatisticReader {

	private Map<String, Map<Integer, SortedMap<Integer, Integer>>> genotypeQuantityHistory;
	private BufferedReader reader;
	private int maxYear=0;
	private int maxQuantity=0;
	private boolean isReady = false;
	// | experiment | zone | year | genotype | age | quantity |
	
	public StatisticReader(String statisticFileURL) {
		try {
			reader = new BufferedReader(new FileReader(statisticFileURL));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		genotypeQuantityHistory = new HashMap<String, Map<Integer, SortedMap<Integer, Integer>>>();
	}
	
	public Map<String, Map<Integer, SortedMap<Integer, Integer>>> getGenotypeQuantityHistory() throws IOException {
		if (!isReady)
			read();
		return genotypeQuantityHistory;
	}
	
	public int getMaxQuantity() throws IOException {
		if (!isReady)
			read();
		return maxQuantity;
	}
	
	public int getMaxYear() throws IOException {
		if (!isReady)
			read();
		return maxYear;
	}
	
	private void read() throws IOException {
		boolean isFileFinished;
		do {
			String line = reader.readLine();
			isFileFinished = readRowAndCheckForEnd(line);
		} while (!isFileFinished);
		addEmptyYears();
		isReady = true;
	}
	
	private boolean readRowAndCheckForEnd(String line) {
		if (line == null)
			return true;
		String[] rowElements = line.split(";");
		int year = Integer.parseInt(rowElements[2]);
		if (year > maxYear)
			maxYear = year;
		String genotype = rowElements[3];
		int age = Integer.parseInt(rowElements[4]);
		int quantity = Integer.parseInt(rowElements[5]);
		addRowToMap(year, genotype, age, quantity);
		return false;
	}
	
	private void addRowToMap(int year, String genotype, int age, int quantity) {
		Map<Integer, SortedMap<Integer, Integer>> genotypeHistories = genotypeQuantityHistory.get(genotype);
		if (genotypeHistories == null) {
			genotypeHistories = new HashMap<Integer, SortedMap<Integer, Integer>>();
			genotypeQuantityHistory.put(genotype, genotypeHistories);
		}
		addRowToSubMap(year,age,quantity,genotypeHistories);
		addRowToSubMap(year,-1,quantity,genotypeHistories);
	}
	
	private void addRowToSubMap(int year, int age, int quantity, Map<Integer, SortedMap<Integer, Integer>> genotypeHistories) {
		SortedMap<Integer, Integer> history = genotypeHistories.get(age);
		if (history == null) {
			history = new TreeMap<Integer, Integer>();
			genotypeHistories.put(age, history);
			history.put(year, quantity);
		}
		else {
			Integer previusQuantity = history.get(year);
			if (previusQuantity == null)
				previusQuantity = 0;
			quantity += previusQuantity;
			history.put(year, quantity);
			if (quantity > maxQuantity)
				maxQuantity = quantity;
		}
	}
	
	private void addEmptyYears() {
		Set<String> genotypes = genotypeQuantityHistory.keySet();
		for (String genotype : genotypes) {
			Map<Integer, SortedMap<Integer, Integer>> genotypeHistory = genotypeQuantityHistory.get(genotype);
			Set<Integer> genotypeAges = genotypeHistory.keySet();
			for(Integer age : genotypeAges){
				SortedMap<Integer, Integer> history = genotypeHistory.get(age);
				for (Integer i=0; i<=maxYear; i++)
					if (history.get(i) == null)
						history.put(i, 0);
			}
		}
	}
	
}
