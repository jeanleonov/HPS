package statistic;

import java.util.HashSet;
import java.util.Set;

public class StatisticSettings {

	public enum Subiteration {
		AFTER_MOVE_AND_SCENARIO ("move"),
		AFTER_EVOLUTION ("evol"),
		AFTER_REPRODACTION ("repr"),
		AFTER_COMPETITION ("comp"),
		AFTER_DIEING ("die_");
		private String shortName;
		private Subiteration(String shortName) {
			this.shortName = shortName;
		}
		public String getShortName() {
			return shortName;
		}
	}
	
	private boolean shouldDisplayImmatures;
	private boolean shouldDistinguishAges;
	private Set<Integer> reportingSubiterationsOrdinals;
	
	private final static Integer defaultReportingSubiteration = Subiteration.AFTER_DIEING.ordinal();
	
	public StatisticSettings() {
		shouldDisplayImmatures = true;
		shouldDistinguishAges = true;
		reportingSubiterationsOrdinals = new HashSet<>();
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
	
	public Set<Integer> getReportingSubiterations() {
		if (reportingSubiterationsOrdinals.size() == 0) {
			Set<Integer> defaultSubiteration = new HashSet<>();
			defaultSubiteration.add(defaultReportingSubiteration);
			return defaultSubiteration;
		}
		return reportingSubiterationsOrdinals;
	}
	
	public void setReportingSubiterations(Set<Integer> reportingSubiterations) {
		this.reportingSubiterationsOrdinals = reportingSubiterations;
	}
	
	public void addReportingSubiteration(Integer subiterationOrdinal) {
		reportingSubiterationsOrdinals.add(subiterationOrdinal);
	}
	
	public void reportAfterEachSubiteration() {
		for (Subiteration subiteration : Subiteration.values())
			reportingSubiterationsOrdinals.add(subiteration.ordinal());
	}
}
