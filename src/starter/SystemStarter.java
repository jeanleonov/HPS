package starter;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

public class SystemStarter implements Pathes{
	
	private DataFiller dataFiller;
	private AgentContainer agentController;
	private AgentController settingsAgent,
							statisticDispatcher;
	private Vector<AgentController> experimentAgents = new Vector<AgentController>();
	private String	viabilitySettingsPath,			// TODO improve it
					posteritySettingPath,
					experimentInfoPath;
	
	public SystemStarter(
			String viabilitySettingsPath,
			String posteritySettingPath, 
			String experimentInfoPath) {
		this.viabilitySettingsPath = viabilitySettingsPath;
		this.posteritySettingPath = posteritySettingPath;
		this.experimentInfoPath = experimentInfoPath;
	}

	public void startSystem(){
		readData();
		startContainers();
		createAndStartSettingsAgents();
		createAndStartStatisticDispatcherAgent();
		createAndStartExperimentAgents();
	}
	
	private void startContainers(){
		Runtime current = Runtime.instance();
		Profile pf = new ProfileImpl(null, 8899, null);
		agentController = current.createMainContainer(pf);
		//*** STARTING OF OTHER CONTAINERS ON OTHER NODES CAN BE HERE
	}
	
	private void readData(){
		BufferedReader posteritySettingsReader;
		BufferedReader viabilitySettingsReader;
	//	BufferedReader scenarioReader;			// TODO
		BufferedReader experimentInfoReader;
		try {
			viabilitySettingsReader = new BufferedReader(new FileReader(viabilitySettingsPath));
			posteritySettingsReader = new BufferedReader(new FileReader(posteritySettingPath));
			experimentInfoReader = new BufferedReader(new FileReader(experimentInfoPath));
		//	scenarioReader = new BufferedReader(new FileReader(PROJECT_PATH + /*!!!*/));			// TODO
			dataFiller = new DataFiller(viabilitySettingsReader, posteritySettingsReader, experimentInfoReader);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void createAndStartSettingsAgents(){
		try {
			//*** YOU CAN START THIS AGENT ON EACH NODE HERE
			settingsAgent = agentController.createNewAgent(
								"Settings",
								"settings.Settings",
								new Object[]{
										dataFiller.getViabilityTable(),
										dataFiller.getPosterityTable()});
			settingsAgent.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	private void createAndStartExperimentAgents(){
		for (int i = 0; i < dataFiller.getNumberOfExperiments(); i++) {
			try {
				//*** YOU CAN START THOSE AGENTS ON OTHER NODES HERE
				experimentAgents.add(
						agentController.createNewAgent(
							getExperimentName(i), 
							"experiment.Experiment", 
							new Object[]{
									dataFiller.getExperimentDistribution(),
									dataFiller.getScenario(),
									dataFiller.getNumberOfModelingYears(),
									i}));
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
		startExperiments();
	}
	
	private String getExperimentName(int i){
		return "Experiment_" + i;
	}
	
	private void startExperiments(){
		for (AgentController agent : experimentAgents){
			try {
				agent.start();								// agent behaviors started
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createAndStartStatisticDispatcherAgent(){
		try {
			statisticDispatcher = agentController.createNewAgent("statisticDispatcher", "statistic.statisticDispatcher", null);
			statisticDispatcher.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
}