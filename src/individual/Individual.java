package individual;

import genotype.Genotype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import settings.Settings;
import settings.ViabilityPair;
import settings.Vocabulary;
import zone.Zone;

public abstract class Individual implements Serializable {

	private static final long serialVersionUID = 1L;

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
	protected ArrayList<ViabilityPair> viabilitySettings;

	SettingsUpdater updater;

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

	protected Float getSetting(Vocabulary.Param param) {
		for (ViabilityPair pair : viabilitySettings)
			if (pair.getParam() == param)
				return pair.getValue();
		assert false : "{" + param.name()
				+ "} wasn't found in viability settings ["
				+ this.getClass().getName() + "]";
		return 0f;
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
			curSurvival = getSetting(Vocabulary.Param.SurvivalFactorFirst);
			curCompetitiveness = getSetting(Vocabulary.Param.CompetitivenessFactorFirst);
			curVoracity = getSetting(Vocabulary.Param.Voracity01);
		}

		private void updateSettingsForNotYearling() {
			if (age > getSetting(Vocabulary.Param.Lifetime))
				curSurvival = curCompetitiveness = 0f;
			else {
				updateSurvivalForNotYearling();
				updateCompetitivenessForNotYearling();
				curVoracity = getCurVoracity();
			}
		}

		private void updateSurvivalForNotYearling() {
			float aS = getSetting(Vocabulary.Param.SurvivalAchieveAge);
			float kbS = getSetting(Vocabulary.Param.SurvivalFactorBeforeS);
			if (age < aS)
				curSurvival = curSurvival
						+ (getSetting(Vocabulary.Param.Survival) - curSurvival)
						* kbS;
			else if (age == aS)
				curSurvival = getSetting(Vocabulary.Param.Survival);
			else
				curSurvival = curSurvival + (1 - curSurvival)
						* getSetting(Vocabulary.Param.SurvivalFactor);
		}

		private void updateCompetitivenessForNotYearling() {
			float aC = getSetting(Vocabulary.Param.CompetitivenessAchieveAge);
			float kbC = getSetting(Vocabulary.Param.CompetitivenessFactorBeforeC);
			if (age < aC)
				curCompetitiveness = curCompetitiveness
						+ (getSetting(Vocabulary.Param.Competitiveness) - curCompetitiveness)
						* kbC;
			else if (age == aC)
				curCompetitiveness = getSetting(Vocabulary.Param.Competitiveness);
			else
				curCompetitiveness = curCompetitiveness
						+ (1 - curCompetitiveness)
						* getSetting(Vocabulary.Param.CompetitivenessFactor);
		}

		private void updateSettingsForMature() {
			if (age == getSetting(Vocabulary.Param.Spawning)) {
				curReproduction = getSetting(Vocabulary.Param.Reproduction);
				curFertility = getSetting(Vocabulary.Param.Fertility);
				curAmplexusRepeat = getSetting(Vocabulary.Param.AmplexusRepeat);
			} else {
				curReproduction = curReproduction + (1 - curReproduction)
						* getSetting(Vocabulary.Param.ReproductionFactor);
				curFertility = curFertility + (1 - curFertility)
						* getSetting(Vocabulary.Param.FertilityFactor);
				curAmplexusRepeat = curAmplexusRepeat + (1 - curAmplexusRepeat)
						* getSetting(Vocabulary.Param.AmplexusRepeatFactor);
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
				return getSetting(Vocabulary.Param.Voracity02);
			case 3:
				return getSetting(Vocabulary.Param.Voracity03);
			case 4:
				return getSetting(Vocabulary.Param.Voracity04);
			case 5:
				return getSetting(Vocabulary.Param.Voracity05);
			case 6:
				return getSetting(Vocabulary.Param.Voracity06);
			case 7:
				return getSetting(Vocabulary.Param.Voracity07);
			case 8:
				return getSetting(Vocabulary.Param.Voracity08);
			case 9:
				return getSetting(Vocabulary.Param.Voracity09);
			case 10:
				return getSetting(Vocabulary.Param.Voracity10_N);
			default:
				return getSetting(Vocabulary.Param.Voracity10_N);
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
