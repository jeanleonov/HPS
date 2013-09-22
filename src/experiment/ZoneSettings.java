package experiment;

import java.util.ArrayList;
import java.util.Map;

import settings.PosterityParentsPair;
import settings.PosterityResultPair;
import distribution.ZoneDistribution;
import experiment.individual.genotype.Genotype;

public class ZoneSettings {

	private Map<Genotype, Float[]> viabilityTable;
	private Map<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable;
	private String zoneName;
	private Double capacity;
	private Map<String, Double> movePossibilitiesTable;
	private ZoneDistribution startDistribution;
	
	public ZoneSettings(String name) {
		zoneName = name;
	}
	
	public Map<Genotype, Float[]> getViabilityTable() {
		return viabilityTable;
	}
	
	public void setViabilityTable(Map<Genotype, Float[]> viabilityTable) {
		this.viabilityTable = viabilityTable;
	}
	
	public Map<PosterityParentsPair, ArrayList<PosterityResultPair>> getPosterityTable() {
		return posterityTable;
	}
	
	public void setPosterityTable(
			Map<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable) {
		this.posterityTable = posterityTable;
	}
	
	public String getZoneName() {
		return zoneName;
	}
	
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	
	public Double getCapacity() {
		return capacity;
	}
	
	public void setCapacity(Double capacity) {
		this.capacity = capacity;
	}
	
	public Map<String, Double> getMovePossibilitiesTable() {
		return movePossibilitiesTable;
	}
	
	public void setMovePossibilitiesTable(
			Map<String, Double> movePossibilitiesTable) {
		this.movePossibilitiesTable = movePossibilitiesTable;
	}
	
	public ZoneDistribution getStartDistribution() {
		return startDistribution;
	}
	
	public void setStartDistribution(ZoneDistribution startDistribution) {
		this.startDistribution = startDistribution;
	}
}
