package starter;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import parser.Parser;
import settings.PosterityParentsPair;
import settings.PosterityResultPair;
import settings.ViabilityPair;
import settings.Vocabulary.Convertor;
import settings.Vocabulary.Param;
import distribution.ExperimentDistribution;
import experiment.Rule;
import experiment.Scenario;

public class DataFiller {
	
	private Reader	viabilityReader,
					posterityReader, 
					movePossibilitiesReader,
					scenarioReader,
					experimentInfoReader;
	private ExperimentDistribution experimentDistribution;
	private HashMap<genotype.Genotype, ArrayList<ViabilityPair>> viabilityTable = new HashMap<genotype.Genotype, ArrayList<ViabilityPair>>();
	private HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> posterityTable = new HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>>();
	private HashMap<Integer, ArrayList<Float>> movePosibilitiesTable = new HashMap<Integer, ArrayList<Float>>();
	private Vector<Rule> rules;
	
	public DataFiller(
			Reader viabilityReader, 
			Reader posterityReader,
			Reader movePossibilitiesReader,
			Reader scenarioReader,
			Reader experimentInfoReader){
		
		this.viabilityReader = viabilityReader;
		this.posterityReader = posterityReader;
		this.movePossibilitiesReader = movePossibilitiesReader;
		this.scenarioReader = scenarioReader;
		this.experimentInfoReader = experimentInfoReader;
		viabilityFill();
		posterityFill();
		scenarioFill();
		experimentFill();
		movePossibilitiesFill();
		
	}
	
	public HashMap<genotype.Genotype, ArrayList<ViabilityPair>> getViabilityTable() {
		return viabilityTable;
	}

	public HashMap<PosterityParentsPair, ArrayList<PosterityResultPair>> getPosterityTable() {
		return posterityTable;
	}

	public HashMap<Integer, ArrayList<Float>> getMovePosibilitiesTable() {
		return movePosibilitiesTable;
	}

	public ExperimentDistribution getExperimentDistribution() {
		return experimentDistribution;
	}

	public Scenario getScenario() {
		return new Scenario(rules);
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
				
				for(int i = 0; i < genotypeOrder.size(); i++) {
					Param param = Convertor.keyToParam(Integer.parseInt(strArr[2], 10));
					
					if(!viabilityTable.containsKey(genotypeOrder.get(i))) {
						viabilityTable.put(genotypeOrder.get(i), new ArrayList<ViabilityPair>());
					}
					
					ArrayList<ViabilityPair> arr = viabilityTable.get(genotypeOrder.get(i));
					arr.add(new ViabilityPair(Float.parseFloat(strArr[i + 3]), param));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void printViabilityTable() {
		for(genotype.Genotype g : viabilityTable.keySet()) {
			System.out.print(g + " - ");
			for(ViabilityPair pair : viabilityTable.get(g)) {
				System.out.print(pair.getValue() + ", ");
			}
			System.out.println();
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
				
				for(int i = 0; i < genotypeOrder.size(); i++) {
					genotype.Genotype gm1 = genotype.Genotype.getGenotype(strArr[0]),
							gm2 = genotype.Genotype.getGenotype(strArr[1]);
					
					float value = Float.parseFloat(strArr[i + 2]);
					if (value == 0)
						continue;
					
					PosterityParentsPair pair = new PosterityParentsPair(gm1, gm2);
					if (!posterityTable.containsKey(pair)) {
						posterityTable.put(pair, new ArrayList<PosterityResultPair>());
					}
					
					posterityTable.get(pair).add(new PosterityResultPair(genotypeOrder.get(i), value));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void movePossibilitiesFill(){
		// TODO!!!!!!!!!!!!!!
		// Temporery (1.0 is anywhere):
		for (int i=0; i<getExperimentDistribution().getZoneDistributions().size(); i++){
			ArrayList<Float> possibilities = new ArrayList<Float>();
			for (int j=0; j<getExperimentDistribution().getZoneDistributions().size(); j++)
				possibilities.add(1f);
			movePosibilitiesTable.put(i, possibilities);			
		}
	}
	
	public void printPosterityTable() {
		for(PosterityParentsPair parents : posterityTable.keySet()) {
			System.out.print(parents.getMale() + "+" + parents.getFemale() + ": ");
			for(PosterityResultPair pair : posterityTable.get(parents)) {
				System.out.print(pair.getGenotype() + "(" + pair.getProbability() + ") ,");
			}
			System.out.println();
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
			Parser parser = new Parser(scenarioReader);
			rules = parser.readRules();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void experimentFill(){
		try
		{
			BufferedReader experimentInfoReader = new BufferedReader(this.experimentInfoReader);
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
