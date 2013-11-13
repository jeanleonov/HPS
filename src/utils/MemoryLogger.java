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
		int mb = 1024*1024;
	    Runtime runtime = Runtime.getRuntime();
		try {
			fileWriter.write("Used;Free;Total;Max\n");
		} catch (IOException e2) {e2.printStackTrace();}
		while (true) {
	        Shared.memoryLogger.debug("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);
	        Shared.memoryLogger.debug("Free Memory:" + runtime.freeMemory() / mb);
	        Shared.memoryLogger.debug("Total Memory:" + runtime.totalMemory() / mb);
	        Shared.memoryLogger.debug("Max Memory:" + runtime.maxMemory() / mb);
	        try {
	        	long used = (runtime.totalMemory() - runtime.freeMemory()) / mb;
	        	long free = runtime.freeMemory() / mb;
	        	long total = runtime.totalMemory() / mb;
	        	long max = runtime.maxMemory() / mb;
				fileWriter.write(""+used+";"+free+";"+total+";"+max+"\n");
			} catch (IOException e1) {e1.printStackTrace();}
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public void finish() {
		try {
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null)
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
