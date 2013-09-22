package settings;

public enum Param {
	Lifetime,
	Spawning,
	Survival,
	SurvivalAchieveAge,
	SurvivalFactor,
	SurvivalFactorFirst,
	SurvivalFactorBeforeS,
	Competitiveness,
	CompetitivenessAchieveAge,
	CompetitivenessFactor,
	CompetitivenessFactorFirst,
	CompetitivenessFactorBeforeC,
	Reproduction,
	ReproductionFactor,
	Fertility,
	FertilityFactor,
	AmplexusRepeat,
	AmplexusRepeatFactor,
	Voracity01,
	Voracity02,
	Voracity03,
	Voracity04,
	Voracity05,
	Voracity06,
	Voracity07,
	Voracity08,
	Voracity09,
	Voracity10_N;
	
	public static Param getByKey(int key) throws Exception {
		Param[] params = Param.values();
		if (key >= params.length || key<0)
			throw new Exception("Too big or negative key for viability param ("+key+")");
		return params[key];
	}
}