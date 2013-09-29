package starter.repeating;

import java.io.Reader;
import java.util.Map;

import starter.base.BaseDataFiller;
import utils.parser.Parser;
import distribution.ZoneDistribution;
import experiment.ZoneSettings;

public class RepeatingRunDataFiller extends BaseDataFiller {
	
	private Reader distributionInfoReader;
	
	public RepeatingRunDataFiller(
			Reader viabilityReader, 
			Reader posterityReader,
			Reader movePossibilitiesReader,
			Reader scenarioReader,
			Reader distributionInfoReader,
			double capacityMultilpier) throws Exception {
		super(viabilityReader, posterityReader, movePossibilitiesReader, scenarioReader, capacityMultilpier);
		this.distributionInfoReader = distributionInfoReader;
	}
	
	protected void fillStartDistribution() throws Exception {
		Parser.ReInit(distributionInfoReader);
		Map<String, ZoneDistribution> distributions = Parser.zoneDistributions();
		for (ZoneSettings zoneSettings : zones) {
			ZoneDistribution distribution = distributions.get(zoneSettings.getZoneName());
			if (distribution == null)
				throw getDistributionException();
			zoneSettings.setStartDistribution(distribution);
		}
	}
	
	private Exception getDistributionException() {
		String myMessage = "Wrong content of initiation file. \n";
		return new Exception(myMessage);
	}
}