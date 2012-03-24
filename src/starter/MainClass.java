package starter;

public class MainClass implements Pathes{

	public static void main(String[] args) {
		SystemStarter starter = new SystemStarter(
										PROJECT_PATH + "src/starter/Viability.csv", 
										PROJECT_PATH + "src/starter/Posterity.csv", 
										PROJECT_PATH + "src/starter/Initiation.hpsi");
		starter.startSystem();
	}

}
