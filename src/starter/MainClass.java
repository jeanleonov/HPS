package starter;

public class MainClass {
	
	public static void main(String[] args) {
		try {
			new SystemStarter(args).startSystem();
		} catch (Exception e) {
			Shared.problemsLogger.error(Shared.printStack(e));
			e.printStackTrace();
		}
	}
}
