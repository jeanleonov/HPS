package statistic;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import experiment.ZoneSettings;

public class StatisticDispatcher {
	
	private StatisticSettings settings;
	private Queue<YearStatistic> queue = new LinkedList<>();
	private Lock queueLock = new ReentrantLock();
	
	private StatisticWriter statisticWriter;

	public StatisticDispatcher(String statisticURL, 
							   StatisticSettings settings, 
							   ZoneSettings zoneSettings) {
		this.settings = settings;
		this.statisticWriter = new StatisticWriter(statisticURL, zoneSettings, settings, queue, queueLock);
		startWritingThread();
	}
	
	private void startWritingThread() {
		Thread writingThread = new Thread(statisticWriter);
		writingThread.setDaemon(false);
		writingThread.start();
	}

	public StatisticSettings getSettings() {
		return settings;
	}

	public void addPackage(YearStatistic yearStatistic) {
		queueLock.lock();
		queue.add(yearStatistic);
		queueLock.unlock();
	}
	
	public void finish() {
		statisticWriter.finish();
	}
}