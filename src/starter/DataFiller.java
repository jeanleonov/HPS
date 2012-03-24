package starter;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import settings.PosterityParentsPair;
import settings.PosterityResultPair;
import settings.ViabilityPair;
import settings.Vocabulary.Convertor;
import settings.Vocabulary.Param;
import distribution.ExperimentDistribution;
import experiment.Scenario;

public class DataFiller {
	
	private Reader	viabilityReader,
					posterityReader, 
					scenarioReader,
					experimentInfoReader;
	private int numberOfModelingYears,
				numberOfExperiments;
	private ExperimentDistribution experimentDistribution;
	private HashMap<genotype.Genotype, ArrayList<ViabilityPair>> viabilityTable = new HashMap<genotype.Genotype, ArrayList<ViabilityPair>>();
	private HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable = new HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>>();
	private Scenario scenario = new Scenario();
	
	public DataFiller(
			Reader viabilityReader, 
			Reader posterityReader, 
			/*Reader scenarioReader,*/
			Reader experimentInfoReader){
		this.viabilityReader = viabilityReader;
		this.posterityReader = posterityReader;
	//	this.scenarioReader = scenarioReader;
		this.experimentInfoReader = experimentInfoReader;
		viabilityFill();
		posterityFill();
	//	scenarioFill();
		experimentFill();
	}
	
	public HashMap<genotype.Genotype, ArrayList<ViabilityPair>> getViabilityTable() {
		return viabilityTable;
	}

	public HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> getPosterityTable() {
		return posterityTable;
	}

	public int getNumberOfModelingYears() {
		return numberOfModelingYears;
	}

	public int getNumberOfExperiments() {
		return numberOfExperiments;
	}

	public ExperimentDistribution getExperimentDistribution() {
		return experimentDistribution;
	}

	public Scenario getScenario() {
		return scenario;
	}

	private void viabilityFill() {
		try {
			Vector<genotype.Genotype> genotypeOrder = new Vector<genotype.Genotype>();
			BufferedReader reader = new BufferedReader(viabilityReader);
			String orderLine = reader.readLine();
			orderFill(genotypeOrder, orderLine, 3);

			String str;
			while ((str = reader.readLine()) != null) {
				String[] strArr = str.split(";");
				if (strArr.length < genotypeOrder.size() + 3)
					continue;
				for (genotype.Genotype gm : genotypeOrder) {
					Param param = Convertor.keyToParam(Integer.parseInt(
							strArr[2], 10));
					ArrayList<ViabilityPair> arr = new ArrayList<ViabilityPair>();
					for (int i = 3; i < genotypeOrder.size(); i++)
						arr.add(new ViabilityPair(Float.parseFloat(strArr[i]), param));
					viabilityTable.put(gm, arr);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void posterityFill() {
		try {
			Vector<genotype.Genotype> genotypeOrder = new Vector<genotype.Genotype>();
			BufferedReader reader = new BufferedReader(posterityReader);
			String orderLine = reader.readLine();
			orderFill(genotypeOrder, orderLine, 2);

			String str;
			while ((str = reader.readLine()) != null) {
				String[] strArr = str.split(";");
				if (strArr.length < genotypeOrder.size() + 2)
					continue;
				for (genotype.Genotype gm : genotypeOrder) {
					genotype.Genotype gm1 = genotype.Genotype.getGenotype(strArr[0]),
							gm2 = genotype.Genotype.getGenotype(strArr[1]);
					for (int i = 2; i < genotypeOrder.size(); i++) {
						float value = Float.parseFloat(strArr[i]);
						if (value == 0)
							continue;

						PosterityParentsPair pair = new PosterityParentsPair(
								gm1, gm2);
						if (!posterityTable.keySet().contains(pair)) {
							posterityTable.put(new PosterityParentsPair(
									gm1, gm2),
									new ArrayList<PosterityResultPair>());
						}
						posterityTable.get(pair).add(
								new PosterityResultPair(gm, value));
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void orderFill(Vector<genotype.Genotype> arr, String str, int startFrom) {
		String[] strArr = str.split(";");
		try {
			for (int i = startFrom; i < strArr.length; i++)
				arr.add(genotype.Genotype.getGenotype(strArr[i]));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void scenarioFill(){
		try{
			BufferedReader scenarioReader = new BufferedReader(this.scenarioReader);
			String cur = scenarioReader.readLine();
			while(cur != null){
				scenario.addRule(cur);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void experimentFill(){
		try
		{
			BufferedReader experimentInfoReader = new BufferedReader(this.experimentInfoReader);
			numberOfModelingYears = Integer.parseInt(experimentInfoReader.readLine());
			numberOfExperiments = Integer.parseInt(experimentInfoReader.readLine());
			String res = new String();
			String c = new String(experimentInfoReader.readLine());
			while(c != null){
				res += c;
				c = experimentInfoReader.readLine();
			}
			experimentDistribution = ExperimentDistribution.parseExperiment(res);
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
