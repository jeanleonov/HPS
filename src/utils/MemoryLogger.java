package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
	
	public void saveMemoryStateToCsv(String suffix) {
    	long used = (runtime.totalMemory() - runtime.freeMemory()) / KB;
    	long free = runtime.freeMemory() / KB;
    	long total = runtime.totalMemory() / KB;
    	long max = runtime.maxMemory() / KB;
        try {
			fileWriter.write(""+used+";"+free+";"+total+";"+max+";"+suffix+"\n");
			fileWriter.flush();
		} catch (IOException e1) {}
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
