package settings;

public interface Vocabulary {

	public enum Param {
		Lifetime, 					// Продолжительность жизни
		Spawning, 					// Возраст первого нереста
		Survival, 					// Выживаемость
		SurvivalFactor, 			// |- Коэффициент
		SurvivalFactorFirst, 		// |- Первого года
		Competitiveness, 			// Конкурентоспособность
		CompetitivenessFactor, 		// |- Коэффициент
		CompetitivenessFactorFirst,	// |- Первого года
		Reproduction, 				// Вероятость размножения
		ReproductionFactor,			// |- Коэффициент
		Fertility,					// Плодовитость
		FertilityFactor,			// |- Коэффициент
		AmplexusRepeat,				// Вероятность повторения аплексусов
		AmplexusRepeatFactor,		// |- Коэффициент
		Voracity01,					// прожорливость в первый год жизни
		Voracity02,					// прожорливость во второй год жизни
		Voracity03,					// ...
		Voracity04,
		Voracity05,
		Voracity06,
		Voracity07,
		Voracity08,
		Voracity09,					// ...
		Voracity10_N				// прожорливость в 10-м году и позже
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
			case 15:
				return Param.Voracity01;
			case 16:
				return Param.Voracity02;
			case 17:
				return Param.Voracity03;
			case 18:
				return Param.Voracity04;
			case 19:
				return Param.Voracity05;
			case 20:
				return Param.Voracity06;
			case 21:
				return Param.Voracity07;
			case 22:
				return Param.Voracity08;
			case 23:
				return Param.Voracity09;
			case 24:
				return Param.Voracity10_N;
			default:
				throw new Exception("Invalid key");
			}
		}
	}
}
