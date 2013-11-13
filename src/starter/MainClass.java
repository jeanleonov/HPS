package starter;

import org.apache.log4j.xml.DOMConfigurator;

import utils.MemoryLogger;

public class MainClass {
	
	public static void main(String[] args) {
		try {
			DOMConfigurator.configure("src/log4j.xml");
			MemoryLogger memoryLogger = new MemoryLogger();
			Thread memoryLoggerThread = new Thread(memoryLogger);
			memoryLoggerThread.setDaemon(true);
			memoryLoggerThread.start();
			new SystemStarter(args).startSystem();
			memoryLoggerThread.interrupt();
			memoryLogger.finish();
		} catch (Exception e) {
			Shared.problemsLogger.error(Shared.printStack(e));
			e.printStackTrace();
		}
	}
}
