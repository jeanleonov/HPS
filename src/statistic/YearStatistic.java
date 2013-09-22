package statistic;

import java.util.Map;

public class YearStatistic {
	
	private int experimentNumber;
	private Integer year;
	private Map<Integer, Map<String, Map<Integer, Map<Integer, Integer>>>> yearStatistic;
	
	public YearStatistic(
			int experimentNumber, Integer year,
			Map<Integer, Map<String, Map<Integer, Map<Integer, Integer>>>> yearStatistic) {
		this.yearStatistic = yearStatistic;
		this.experimentNumber = experimentNumber;
		this.year = year;
	}

	public Map<Integer, Map<String, Map<Integer, Map<Integer, Integer>>>> getYearStatistic() {
		return yearStatistic;
	}
	
	public void setYearStatistic(
			Map<Integer, Map<String, Map<Integer, Map<Integer, Integer>>>> yearStatistic) {
		this.yearStatistic = yearStatistic;
	}
	
	public int getExperimentNumber() {
		return experimentNumber;
	}
	
	public void setExperimentNumber(int experimentNumber) {
		this.experimentNumber = experimentNumber;
	}
	
	public Integer getYear() {
		return year;
	}
	
	public void setYear(Integer year) {
		this.year = year;
	}
	
}
