package statistic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;

import settings.Param;
import starter.Shared;
import statistic.StatisticSettings.Subiteration;
import experiment.ZoneSettings;
import experiment.individual.genotype.Genotype;

public class StatisticWriter implements Runnable {

	private String fileLocation;
	private BufferedWriter writer;
	private boolean hasToFinish = false;
	private ZoneSettings zoneSettingsStub;
	private ArrayList<Genotype> columnGenotypes = new ArrayList<>();
	private ArrayList<Integer> columnAges = new ArrayList<>();
	private StatisticSettings settings;

	private Queue<YearStatistic> queue = new LinkedList<>();
	private Lock queueLock;
	
	public StatisticWriter(String curStatisticFileURL,
						   ZoneSettings zoneSettingsStub,
						   StatisticSettings settings,
						   Queue<YearStatistic> queue,
						   Lock queueLock) {
		this.fileLocation = curStatisticFileURL;
		this.zoneSettingsStub = zoneSettingsStub;
		this.settings = settings;
		this.queue = queue;
		this.queueLock = queueLock;
	}
	
	public void finish() {
		hasToFinish = true;
	}
	
	@Override
	public void run() {
		try {
			while (!hasToFinish) {
				flushQueue();
				Thread.sleep(200);
			}
			flushQueue();
			flushWriter();
		} catch (Exception e) {
			Shared.problemsLogger.error(Shared.printStack(e));
			System.exit(1);
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				Shared.problemsLogger.error(Shared.printStack(e));
				System.exit(1);
			}
		}
	}
	
	private void initFile() throws IOException {
		File file = createFile();
		writer = new BufferedWriter(new FileWriter(file,true));
		writer.write(getTableHeader());
	}
	
	private File createFile() throws IOException {
		File file = new File(fileLocation);
		if (!file.exists()) {
			String folderPath = fileLocation.substring(0, fileLocation.lastIndexOf('/'));
			File parentFolder = new File(folderPath);
			parentFolder.mkdir();
			file.createNewFile();
		}
		else
			throw new IOException("Can not create statistic file (\""+fileLocation+"\") because it exists.");
		return file;
	}
	
	private void flushQueue() throws IOException {
		while (queue.size() != 0) {
			queueLock.lock();
			YearStatistic yearStatistic = queue.poll();
			queueLock.unlock();
			if (writer == null)
				initFile();
			writeYearStatistic(yearStatistic);
		}
	}
	
	private String getTableHeader() {
		StringBuilder header = new StringBuilder();
		header.append("Year#;AfterSubiteration;Zone#;");
		if (settings.shouldDistinguishAges())
			if (settings.shouldDisplayImmatures())
				header.append(getAllAgesColumns());
			else
				header.append(getMatureAgesColumns());
		else
			header.append(getGenotypesColumns());
		return header.toString();
	}
	
	private String getAllAgesColumns() {
		StringBuilder columns = new StringBuilder();
		for (Genotype genotype : Genotype.getAll())
			for (int age=0; age<getMaxAgeFor(genotype); age++) {
				columns.append(genotype.toString()).append('-').append(age).append(';');
				columnGenotypes.add(genotype);
				columnAges.add(age);
			}
		columns.deleteCharAt(columns.length()-1);
		return columns.toString();
	}
	
	private Float getMaxAgeFor(Genotype genotype) {
		return zoneSettingsStub.getViabilityTable().get(genotype)[Param.Lifetime.ordinal()];
	}
	
	private String getMatureAgesColumns() {
		StringBuilder columns = new StringBuilder();
		for (Genotype genotype : Genotype.getAll())
			for (int age=getMatureAge(genotype); age<getMaxAgeFor(genotype); age++) {
				columns.append(genotype.toString()).append('-').append(age).append(';');
				columnGenotypes.add(genotype);
				columnAges.add(age);
			}
		columns.deleteCharAt(columns.length()-1);
		return columns.toString();
	}
	
	private int getMatureAge(Genotype genotype) {
		return Math.round(zoneSettingsStub.getViabilityTable().get(genotype)[Param.Spawning.ordinal()]+0.0001f);
	}
	
	private String getGenotypesColumns() {
		StringBuilder columns = new StringBuilder();
		for (Genotype genotype : Genotype.getAll()) {
			columns.append(genotype.toString()).append(';');
			columnGenotypes.add(genotype);
			columnAges.add(YearStatisticCollector.TOTAL_AGE);
		}
		columns.deleteCharAt(columns.length()-1);
		return columns.toString();
	}
	
	private void writeYearStatistic(YearStatistic yearStatistic) throws IOException {
		String yearStat = renderYear(yearStatistic);
		writer.write(yearStat);
	}
	
	
	private int curYear;
	private int curSubiteration;
	
	private String renderYear(YearStatistic statistic) {
		StringBuilder buffer = new StringBuilder();
		curYear = statistic.getYear();
		for (Integer subiteration : statistic.getYearStatistic().keySet()) {
			curSubiteration = subiteration;
			buffer.append(renderSubiteration(statistic.getYearStatistic().get(subiteration)));
		}
		return buffer.toString();
	}
	
	private StringBuilder renderSubiteration(Map<String, Map<Integer, Map<Integer, Integer>>> subiterationStat) {
		StringBuilder buffer = new StringBuilder();
		for (String zone : subiterationStat.keySet()) {
			buffer.append('\n').append(curYear).append(';');
			buffer.append(Subiteration.values()[curSubiteration].getShortName()).append(';');
			buffer.append(zone).append(';');
			buffer.append(renderZone(subiterationStat.get(zone)));
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer;
	}
	
	private StringBuilder renderZone(Map<Integer, Map<Integer, Integer>> zoneStat) {
		StringBuilder buffer = new StringBuilder();
		for (int i=0; i<columnGenotypes.size(); i++) {
			int genotypeId = columnGenotypes.get(i).getId();
			Map<Integer, Integer> genotypeStat = zoneStat.get(genotypeId);
			if (genotypeStat == null)
				buffer.append("0;");
			else {
				int age = columnAges.get(i);
				Integer indivsNumber = genotypeStat.remove(age);
				if (indivsNumber == null)
					buffer.append("0;");
				else
					buffer.append(indivsNumber).append(';');
			}
		}
		return buffer;
	}
	
	private void flushWriter() throws IOException {
		if (writer != null)
			writer.flush();
	}
}
