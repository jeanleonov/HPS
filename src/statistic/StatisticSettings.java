package statistic;

import java.util.HashSet;
import java.util.Set;

public class StatisticSettings {

	public enum Subiteration {
		AFTER_MOVE_AND_SCENARIO,
		AFTER_EVOLUTION,
		AFTER_REPRODACTION,
		AFTER_COMPETITION,
		AFTER_DIEING;
	}
	
	private boolean shouldDisplayImmatures;
	private boolean shouldDistinguishAges;
	private Set<Subiteration> reportingSubiterations;
	
	private final static Subiteration defaultReportingSubiteration = Subiteration.AFTER_DIEING;
	
	public StatisticSettings() {
		shouldDisplayImmatures = true;
		shouldDistinguishAges = true;
		reportingSubiterations = new HashSet<>();
	}
	
	public boolean shouldDisplayImmatures() {
		return shouldDisplayImmatures;
	}
	
	public void setShouldDisplayImmatures(boolean shouldDisplayImmatures) {
		this.shouldDisplayImmatures = shouldDisplayImmatures;
	}
	
	public boolean shouldDistinguishAges() {
		return shouldDistinguishAges;
	}
	
	public void setShouldDistinguishAges(boolean shouldDistinguishAges) {
		this.shouldDistinguishAges = shouldDistinguishAges;
	}
	
	public Set<Subiteration> getReportingSubiterations() {
		if (reportingSubiterations.size() == 0) {
			Set<Subiteration> defaultSubiteration = new HashSet<>();
			defaultSubiteration.add(defaultReportingSubiteration);
			return defaultSubiteration;
		}
		return reportingSubiterations;
	}
	
	public void setReportingSubiterations(Set<Subiteration> reportingSubiterations) {
		this.reportingSubiterations = reportingSubiterations;
	}
	
	public void addReportingSubiteration(Subiteration subiteration) {
		reportingSubiterations.add(subiteration);
	}
	
	public void reportAfterEachSubiteration() {
		for (Subiteration subiteration : Subiteration.values())
			reportingSubiterations.add(subiteration);
	}
}
