package settings;

public interface Vocabulary {
	public enum Genome {
		FxRxR, MxRyR, F_xL_xR, M_xL_yR, M_yL_xR, FxL_xR_, MyL_xR_, MxL_yR_, F_xL__xR_, M_xL__yR_, M_yL__xR_, FxLxL, MxLyL
	}

	public static final int GenomeCount = 13;

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
		AmplexusRepeatFactor		// |- Коэффициент
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
