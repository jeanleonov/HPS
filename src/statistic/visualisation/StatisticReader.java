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
	private int maxIteration=0;
	private int minuteness;
	private int maxQuantity=0;
	private boolean isReady = false;
	private boolean shouldReadDetailedStatistic;
	private boolean shouldDisplayImmatures;
	// | experiment | zone | year | genotype | age | quantity |
	
	public StatisticReader(String statisticFileURL, boolean shouldReadDetailedStatistic, boolean shouldDisplayImmatures) {
		this.shouldReadDetailedStatistic = shouldReadDetailedStatistic;
		if (shouldReadDetailedStatistic)
			minuteness = 5;
		else
			minuteness = 1;
		this.shouldDisplayImmatures = shouldDisplayImmatures;
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
	
	public int getMaxIteration() throws IOException {
		if (!isReady)
			read();
		return maxIteration*minuteness;
	}
	
	private void read() throws IOException {
		boolean isFileFinished;
		do {
			String line = reader.readLine();
			isFileFinished = readRowAndCheckForEnd(line);
		} while (!isFileFinished);
		addEmptyIterations();
		isReady = true;
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
			if (subiteration != 4)					// 3 - magic number)) it is a number of sub iteration between competition and dying
				return false;						// 5 - magic number)) it is a number of sub iterations in year (see ZoneBehaviour)
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
	
}
