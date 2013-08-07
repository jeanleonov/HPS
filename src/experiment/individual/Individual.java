package experiment.individual;

import java.util.HashMap;
import java.util.Iterator;

import settings.Settings;
import settings.Vocabulary;
import settings.Vocabulary.Param;
import experiment.individual.genotype.Genotype;
import experiment.zone.Zone;

public abstract class Individual {

	protected Genotype myGenotype;
	protected int age;
	protected Zone myZone;

	protected float curSurvival;
	protected float curCompetitiveness;
	protected float curReproduction;
	protected float curFertility;
	protected float curAmplexusRepeat;
	protected float curVoracity;
	protected boolean readyToReproduction = true;
	protected Float[] viabilitySettings;

	protected SettingsUpdater updater;

	public Individual(Genotype myGenotype, int age, Zone myZone) {
		this.myGenotype = myGenotype;
		this.myZone = myZone;
		viabilitySettings = Settings.getViabilitySettings(getGenotype());
		updater = new SettingsUpdater();
		for (this.age = 0; this.age <= age; this.age++)
			updater.updateSettings();
		this.age = age;
	}

	public void setZone(Zone newZone) {
		myZone = newZone;
	}

	public void updateSettings() {
		age++;
		readyToReproduction = true;
		updater.updateSettings();
	}

	public boolean isDead() {
		double randVal = Math.random();
		if (randVal <= curSurvival)
			return false;
		return true;
	}

	public boolean isGoingOut() {
		return Math.random() > myZone.getAttractivness();
	}

	public Integer whereDoGo() {
		HashMap<Integer, Double> neighboursTravelCost = myZone
				.getZoneTravelPossibilities();
		if (neighboursTravelCost == null)
			return -1;
		double totalWeight = myZone.getSumOfTravelPossibilities();
		double weightSum = 0;
		double point = Math.random() * totalWeight;
		Iterator<Integer> zoneNum = neighboursTravelCost.keySet().iterator();
		while (zoneNum.hasNext()) {
			Integer currentZone = zoneNum.next();
			weightSum += neighboursTravelCost.get(currentZone);
			if (point < weightSum)
				return currentZone;
		}
		return null;
	}

	public abstract boolean isFemale();

	public boolean isMature() {
		return age >= getSetting(Vocabulary.Param.Spawning);
	}

	public boolean isReadyToReproduction() {
		return readyToReproduction;
	}

	public Genotype getGenotype() {
		return myGenotype;
	}

	public int getAge() {
		return age;
	}

	public Zone getMyZone() {
		return myZone;
	}

	public float getCompetitiveness() {
		return curCompetitiveness;
	}

	public float getVoracity() {
		return curVoracity;
	}

	protected Float getSetting(Param param) {
		return viabilitySettings[param.ordinal()];
	}

	class SettingsUpdater {

		public void updateSettings() {
			if (age == 0)
				updateSettingsForYearling();
			else
				updateSettingsForNotYearling();
			if (isMature())
				updateSettingsForMature();
			else
				updateSettingsForImmature();
		}

		private void updateSettingsForYearling() {
			curSurvival = getSetting(Param.SurvivalFactorFirst);
			curCompetitiveness = getSetting(Param.CompetitivenessFactorFirst);
			curVoracity = getSetting(Param.Voracity01);
		}

		private void updateSettingsForNotYearling() {
			if (age > getSetting(Param.Lifetime))
				curSurvival = curCompetitiveness = 0f;
			else {
				updateSurvivalForNotYearling();
				updateCompetitivenessForNotYearling();
				curVoracity = getCurVoracity();
			}
		}

		private void updateSurvivalForNotYearling() {
			float aS = getSetting(Param.SurvivalAchieveAge);
			float kbS = getSetting(Param.SurvivalFactorBeforeS);
			if (age < aS)
				curSurvival = curSurvival
						+ (getSetting(Param.Survival) - curSurvival)
						* kbS;
			else if (age == aS)
				curSurvival = getSetting(Param.Survival);
			else
				curSurvival = curSurvival + (1 - curSurvival)
						* getSetting(Param.SurvivalFactor);
		}

		private void updateCompetitivenessForNotYearling() {
			float aC = getSetting(Param.CompetitivenessAchieveAge);
			float kbC = getSetting(Param.CompetitivenessFactorBeforeC);
			if (age < aC)
				curCompetitiveness = curCompetitiveness
						+ (getSetting(Param.Competitiveness) - curCompetitiveness)
						* kbC;
			else if (age == aC)
				curCompetitiveness = getSetting(Param.Competitiveness);
			else
				curCompetitiveness = curCompetitiveness
						+ (1 - curCompetitiveness)
						* getSetting(Param.CompetitivenessFactor);
		}

		private void updateSettingsForMature() {
			if (age == getSetting(Param.Spawning)) {
				curReproduction = getSetting(Param.Reproduction);
				curFertility = getSetting(Param.Fertility);
				curAmplexusRepeat = getSetting(Param.AmplexusRepeat);
			} else {
				curReproduction = curReproduction + (1 - curReproduction)
						* getSetting(Param.ReproductionFactor);
				curFertility = curFertility + (1 - curFertility)
						* getSetting(Param.FertilityFactor);
				curAmplexusRepeat = curAmplexusRepeat + (1 - curAmplexusRepeat)
						* getSetting(Param.AmplexusRepeatFactor);
			}
		}

		private void updateSettingsForImmature() {
			curReproduction = 0f;
			curFertility = 0f;
			curAmplexusRepeat = 0f;
		}

		private float getCurVoracity() {
			switch (age + 1) {
			case 2:
				return getSetting(Param.Voracity02);
			case 3:
				return getSetting(Param.Voracity03);
			case 4:
				return getSetting(Param.Voracity04);
			case 5:
				return getSetting(Param.Voracity05);
			case 6:
				return getSetting(Param.Voracity06);
			case 7:
				return getSetting(Param.Voracity07);
			case 8:
				return getSetting(Param.Voracity08);
			case 9:
				return getSetting(Param.Voracity09);
			case 10:
				return getSetting(Param.Voracity10_N);
			default:
				return getSetting(Param.Voracity10_N);
			}
		}
	}

	public String toString() {
		return myGenotype.toString() + " " + age;
	}

	public double getAntiCompetitiveness() {
		return 1 - getCompetitiveness();
	}
}
