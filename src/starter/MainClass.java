package starter;

import org.apache.log4j.xml.DOMConfigurator;

import utils.MemoryLogger;

public class MainClass {

	public static void main(String[] args) {
		try {
			DOMConfigurator.configure("src/log4j.xml");
			new SystemStarter(args).startSystem();
			MemoryLogger.get().finish();
		} catch (Exception e) {
			Shared.problemsLogger.error(Shared.printStack(e));
			e.printStackTrace();
		}
	}
}
