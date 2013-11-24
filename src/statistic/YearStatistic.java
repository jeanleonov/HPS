package statistic;

import java.util.Map;

public class YearStatistic {
	
	private Integer year;
	private Map<Integer, Map<String, Map<Integer, Map<Integer, Integer>>>> yearStatistic;
	
	public YearStatistic(
			Integer year,
			Map<Integer, Map<String, Map<Integer, Map<Integer, Integer>>>> yearStatistic) {
		this.yearStatistic = yearStatistic;
		this.year = year;
	}

	public Map<Integer, Map<String, Map<Integer, Map<Integer, Integer>>>> getYearStatistic() {
		return yearStatistic;
	}
	
	public Integer getYear() {
		return year;
	}
	
}
