package starter.repeating;

import java.io.FileReader;
import java.io.Reader;

import starter.Argument;
import starter.base.BaseDataFiller;
import starter.base.BaseSystemStarter;

public class RepeatingSystemStarter extends BaseSystemStarter {

	private String	viabilitySettingsPath,
					posteritySettingPath,
					movePossibilitiesPath,
					distributionInfoPath,
					scenarioPath;

	public RepeatingSystemStarter(String[] args) throws Exception {
		super(args);
		String projectPath = (String) Argument.PROJECT_PATH.getValue();
		this.viabilitySettingsPath = getPathOf(Argument.VIABILITY, projectPath);
		this.posteritySettingPath = getPathOf(Argument.POSTERITY, projectPath);
		this.movePossibilitiesPath = getPathOf(Argument.MOVE_POSSIBILITIES, projectPath);
		this.scenarioPath = getPathOf(Argument.SCENARIO, projectPath);
		this.distributionInfoPath = getPathOf(Argument.INITIATION, projectPath);
	}

	@Override
	protected BaseDataFiller getDataFiller() throws Exception {
		Reader viabilityReader = new FileReader(viabilitySettingsPath);
		Reader posterityReader = new FileReader(posteritySettingPath);
		Reader movePossibilitiesReader = new FileReader(movePossibilitiesPath);
		Reader scenarioReader = new FileReader(scenarioPath);
		Reader distributionInfoReader = new FileReader(distributionInfoPath);
		return new RepeatingRunDataFiller(viabilityReader, posterityReader, movePossibilitiesReader, scenarioReader, distributionInfoReader, super.capacityMultiplier);
	}

	@Override
	protected String getStatisticFileName() {
		try {
			return "statistics/" + (String) Argument.EXPERIMENTS_SERIES_NAME.getValue()
				  + ((super.curExperiment==-1)?"":(" -e " + super.curExperiment))
				  + ((super.remainingExperints==-1)?"":(" -E " + super.remainingExperints))
				  + ".csv";
		} catch (Exception e) {
			return "statistics/" + "Statistic" + ((super.curExperiment==-1)?"":(" -e " + super.curExperiment))
				  + ((super.remainingExperints==-1)?"":(" -E " + super.remainingExperints))
				  + ".csv";
		}
	}
}