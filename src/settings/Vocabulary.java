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
		public static Genome keyToGenome(int key) throws Exception {
			/*
			 * Calculating main key 2 XLC XRC 3 XLC XRR 6 XLC YRC 7 XLC YRR 9
			 * XLR XLR 10 XLR XRC 13 XLR YLR 14 XLR YRC 20 YLC XRC 21 YLR XRC 27
			 * XRR XRR 28 YLC XRR 31 XRR YRR
			 */
			switch (key) {
			case 2:
				return Genome.F_xL__xR_;
			case 3:
				return Genome.F_xL_xR;
			case 6:
				return Genome.M_xL__yR_;
			case 7:
				return Genome.M_xL_yR;
			case 9:
				return Genome.FxLxL;
			case 10:
				return Genome.FxL_xR_;
			case 13:
				return Genome.MxLyL;
			case 14:
				return Genome.MxL_yR_;
			case 20:
				return Genome.M_yL__xR_;
			case 21:
				return Genome.MyL_xR_;
			case 27:
				return Genome.FxRxR;
			case 28:
				return Genome.M_yL_xR;
			case 31:
				return Genome.MxRyR;
			default:
				throw new Exception("Invalid key");
			}

		}

		public static int genotypeToKey(Genome g) {
			switch (g) {
			case F_xL__xR_:
				return 2;
			case F_xL_xR:
				return 3;
			case M_xL__yR_:
				return 6;
			case M_xL_yR:
				return 7;
			case FxLxL:
				return 9;
			case FxL_xR_:
				return 10;
			case MxLyL:
				return 13;
			case MxL_yR_:
				return 14;
			case M_yL__xR_:
				return 20;
			case MyL_xR_:
				return 21;
			case FxRxR:
				return 27;
			case M_yL_xR:
				return 28;
			case MxRyR:
				return 31;
			default:
				return 0;
			}
		}

		public static boolean getGender(int key) {
			if ((key == 2) || (key == 3) || (key == 9) || (key == 27)
					|| (key == 31))
				return true;
			else
				return false;
		}

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
