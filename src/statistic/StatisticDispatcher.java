package statistic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import settings.Settings;
import settings.Vocabulary.Param;
import starter.Shared;
import experiment.individual.genotype.Genotype;

public class StatisticDispatcher {
	
	private String fileLocation;
	private StatisticSettings settings;
	private Queue<YearStatistic> queue = new LinkedList<>();
	private boolean isWritingThreadStarted = false;
	private boolean hasToFinish = false;
	private boolean finished = false;
	private BufferedWriter writer = null;
	private Lock queueLock = new ReentrantLock();

	public StatisticDispatcher(String curStatisticFileURL, StatisticSettings settings) {
		fileLocation = curStatisticFileURL;
		this.settings = settings;
	}

	public StatisticSettings getSettings() {
		return settings;
	}

	public void addPackage(YearStatistic yearStatistic) {
		queueLock.lock();
		queue.add(yearStatistic);
		queueLock.unlock();
		if (!isWritingThreadStarted)
			startWritingThread();
	}
	
	public void finish() {
		hasToFinish = true;
		while (!finished)
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				Shared.problemsLogger.error(Shared.printStack(e));
			}
	}
	
	private void startWritingThread() {
		new Thread(new StatisticWriter()).start();
		isWritingThreadStarted = true;
	}
	
	private class StatisticWriter implements Runnable {
		@Override
		public void run() {
			try {
				while (!hasToFinish) {
						flushQueue();
						Thread.sleep(200);
				}
				flushWriter();
			} catch (Exception e) {
				Shared.problemsLogger.error(Shared.printStack(e));
				System.exit(1);
			} finally {
				try {
					writer.close();
				} catch (IOException e) {
					Shared.problemsLogger.error(Shared.printStack(e));
					System.exit(1);
				}
			}
		}
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
	
	private void initFile() throws IOException {
		File file = createFile();
		writer = new BufferedWriter(new FileWriter(file,true));
		writer.write(getTableHeader());
	}
	
	private File createFile() throws IOException {
		File file = new File(fileLocation);
		if (!file.exists())
			file.createNewFile();
		return file;
	}
	
	private String getTableHeader() {
		StringBuilder header = new StringBuilder();
		header.append("Experiment#;Zone#;Year;Subiteration;");
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
				// TODO fill columns map
			}
		columns.deleteCharAt(columns.length()-1);
		return columns.toString();
	}
	
	private Float getMaxAgeFor(Genotype genotype) {
		return Settings.getViabilitySettings(genotype)[Param.Lifetime.ordinal()];
	}
	
	private String getMatureAgesColumns() {
		StringBuilder columns = new StringBuilder();
		for (Genotype genotype : Genotype.getAll())
			for (int age=getMatureAge(genotype); age<getMaxAgeFor(genotype); age++) {
				columns.append(genotype.toString()).append('-').append(age).append(';');
				// TODO fill columns map
			}
		columns.deleteCharAt(columns.length()-1);
		return columns.toString();
	}
	
	private int getMatureAge(Genotype genotype) {
		return Math.round(Settings.getViabilitySettings(genotype)[Param.Spawning.ordinal()]+0.0001f);
	}
	
	private String getGenotypesColumns() {
		StringBuilder columns = new StringBuilder();
		for (Genotype genotype : Genotype.getAll()) {
			columns.append(genotype.toString()).append(';');
			// TODO fill columns map
		}
		columns.deleteCharAt(columns.length()-1);
		return columns.toString();
	}
	
	private void writeYearStatistic(YearStatistic yearStatistic) {
		// TODO
	}
	
	private void flushWriter() throws IOException {
		writer.flush();
	}
}