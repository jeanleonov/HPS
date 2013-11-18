package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import starter.Shared;

public class MemoryLogger implements Runnable {
	
	private BufferedWriter fileWriter;
	
	public MemoryLogger() throws IOException {
		FileWriter writer = new FileWriter("log/memory.csv");
		fileWriter = new BufferedWriter(writer);
	}

	@Override
	public void run() {
		int kb = 1024;
	    Runtime runtime = Runtime.getRuntime();
		try {
			fileWriter.write("Used;Free;Total;Max\n");
		} catch (IOException e2) {e2.printStackTrace();}
		while (true) {
        	long used = (runtime.totalMemory() - runtime.freeMemory()) / kb;
        	long free = runtime.freeMemory() / kb;
        	long total = runtime.totalMemory() / kb;
        	long max = runtime.maxMemory() / kb;
	        Shared.memoryLogger.debug("Used Memory:" + used);
	        Shared.memoryLogger.debug("Free Memory:" + free);
	        Shared.memoryLogger.debug("Total Memory:" + total);
	        Shared.memoryLogger.debug("Max Memory:" + max);
	        try {
				fileWriter.write(""+used+";"+free+";"+total+";"+max+"\n");
				fileWriter.flush();
			} catch (IOException e1) {}
	        try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
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
