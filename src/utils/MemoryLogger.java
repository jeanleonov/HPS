package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import starter.Shared;

public class MemoryLogger {

	private final static int KB = 1024;
	private static MemoryLogger instance = null;
	private BufferedWriter fileWriter;
	private Runtime runtime;
	
	private MemoryLogger() throws IOException {
		FileWriter writer = new FileWriter("log/memory.csv");
		fileWriter = new BufferedWriter(writer);
		runtime = Runtime.getRuntime();
		instance = this;
	}
	
	public static MemoryLogger get() throws IOException {
		if (instance == null) {
			synchronized (MemoryLogger.class) {
				if (instance == null)
					instance = new MemoryLogger();
			}
		}
		return instance;
	}
	
	public void saveMemoryStateToCsv(String prefix, String suffix) {
    	long used = (runtime.totalMemory() - runtime.freeMemory()) / KB;
    	long free = runtime.freeMemory() / KB;
    	long total = runtime.totalMemory() / KB;
    	long max = runtime.maxMemory() / KB;
        //try {
			//fileWriter.write(prefix+";"+used+";"+free+";"+total+";"+max+";"+suffix+"\n");
			//fileWriter.flush();
			double usedPart = (double)(max-used)/max;
			if (usedPart < 0.15) {
				System.gc();
				Shared.problemsLogger.warn("Used "+((1-usedPart)*100)+"% of available memory.");
			}
			if (usedPart < 0.05) {
				Shared.problemsLogger.warn("Less than 5% of available memory is used. The program should be stopped.");
				System.exit(-1);
			}
		//} catch (IOException e1) {}
	}
	
	public void finish() {
		try {
			fileWriter.flush();
		} catch (IOException e) {}
		finally {
			if (fileWriter != null)
				try {
					fileWriter.close();
				} catch (IOException e) {}
		}
	}

}
