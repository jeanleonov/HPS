package settings;

public interface Vocabulary {

	public enum Param {
		Lifetime, 						// ����������������� �����
		Spawning, 						// ������� ������� �������
		Survival, 						// ������������
		SurvivalAchieveAge,				// |- ������� ���������� ������������ S
		SurvivalFactor, 				// |- �����������
		SurvivalFactorFirst, 			// |- ������� ����
		SurvivalFactorBeforeS,			// |- ����������� �� ���������� S
		Competitiveness, 				// ���������������������
		CompetitivenessAchieveAge,		// |- ������� ���������� C
		CompetitivenessFactor, 			// |- �����������
		CompetitivenessFactorFirst,		// |- ������� ����
		CompetitivenessFactorBeforeC,	// |- ����������� �� ���������� C
		Reproduction, 					// ���������� �����������
		ReproductionFactor,				// |- �����������
		Fertility,						// ������������
		FertilityFactor,				// |- �����������
		AmplexusRepeat,					// ����������� ���������� ����������
		AmplexusRepeatFactor,			// |- �����������
		Voracity01,						// ������������� � ������ ��� �����
		Voracity02,						// ������������� �� ������ ��� �����
		Voracity03,						// ...
		Voracity04,
		Voracity05,
		Voracity06,
		Voracity07,
		Voracity08,
		Voracity09,						// ...
		Voracity10_N					// ������������� � 10-� ���� � �����
	}

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
				return Param.SurvivalAchieveAge;
			case 5:
				return Param.SurvivalFactor;
			case 6:
				return Param.SurvivalFactorFirst;
			case 7:
				return Param.SurvivalFactorBeforeS;
			case 8:
				return Param.Competitiveness;
			case 9:
				return Param.CompetitivenessAchieveAge;
			case 10:
				return Param.CompetitivenessFactor;
			case 11:
				return Param.CompetitivenessFactorFirst;
			case 12:
				return Param.CompetitivenessFactorBeforeC;
			case 13:
				return Param.Reproduction;
			case 14:
				return Param.ReproductionFactor;
			case 15:
				return Param.Fertility;
			case 16:
				return Param.FertilityFactor;
			case 17:
				return Param.AmplexusRepeat;
			case 18:
				return Param.AmplexusRepeatFactor;
			case 19:
				return Param.Voracity01;
			case 20:
				return Param.Voracity02;
			case 21:
				return Param.Voracity03;
			case 22:
				return Param.Voracity04;
			case 23:
				return Param.Voracity05;
			case 24:
				return Param.Voracity06;
			case 25:
				return Param.Voracity07;
			case 26:
				return Param.Voracity08;
			case 27:
				return Param.Voracity09;
			case 28:
				return Param.Voracity10_N;
			default:
				throw new Exception("Invalid key");
			}
		}
	}
}
