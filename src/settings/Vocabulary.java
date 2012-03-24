package settings;

public interface Vocabulary {

	public enum Param {
		Lifetime, 					// ����������������� �����
		Spawning, 					// ������� ������� �������
		Survival, 					// ������������
		SurvivalFactor, 			// |- �����������
		SurvivalFactorFirst, 		// |- ������� ����
		Competitiveness, 			// ���������������������
		CompetitivenessFactor, 		// |- �����������
		CompetitivenessFactorFirst,	// |- ������� ����
		Reproduction, 				// ���������� �����������
		ReproductionFactor,			// |- �����������
		Fertility,					// ������������
		FertilityFactor,			// |- �����������
		AmplexusRepeat,				// ����������� ���������� ����������
		AmplexusRepeatFactor		// |- �����������
	}

	// This is the temporary fix used for Posterity
	// Maybe in future it will be unnecessary
	public class Convertor {
		public static Param keyToParam(int key) throws Exception {
			switch (key) {
			case 1:
				return Param.Lifetime;
			case 2:
				return Param.Spawning;
			case 3:
				return Param.Survival;
			case 4:
				return Param.SurvivalFactor;
			case 5:
				return Param.SurvivalFactorFirst;
			case 6:
				return Param.Competitiveness;
			case 7:
				return Param.CompetitivenessFactor;
			case 8:
				return Param.CompetitivenessFactorFirst;
			case 9:
				return Param.Reproduction;
			case 10:
				return Param.ReproductionFactor;
			case 11:
				return Param.Fertility;
			case 12:
				return Param.FertilityFactor;
			case 13:
				return Param.AmplexusRepeat;
			case 14:
				return Param.AmplexusRepeatFactor;
			default:
				throw new Exception("Invalid key");
			}
		}
	}
}
