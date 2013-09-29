package starter.repeating;

import starter.Shared;


public class RepeatingRunMainClass {
	
	public static void main(String[] args) {
		try {
			new RepeatingSystemStarter(args).startSystem();
		} catch (Exception e) {
			Shared.problemsLogger.error(Shared.printStack(e));
			e.printStackTrace();
		}
	}
}
