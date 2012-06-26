package statistic;

import jade.core.Agent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import zone.ZoneBehaviour;

import jxl.write.WriteException;

public class StatisticDispatcher extends Agent{

	private String fileLocation = "statistic.csv";
	private Vector<StatisticPackage> packages = new Vector<StatisticPackage>() ;

	@Override
	protected void setup(){
		fileLocation = (String)getArguments()[0];
		addBehaviour(new StatisticDispatcherBehaviour());
	}

	void addPackage(StatisticPackage pack) {
		packages.add(pack);
	}

	void exportToFile() {
		try {
			File file = createFile();
			//	System.out.println("Statistic " + file.getAbsolutePath());		#lao
			writeStatistic(file);		
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
	
	private void writeStatistic(File file) throws IOException, WriteException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
		for (StatisticPackage pack : packages){
			pack.writeToFile(bw);
		}
		bw.flush();
		bw.close();
	}

	private File createFile() throws IOException {
		File file = new File(fileLocation);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
}