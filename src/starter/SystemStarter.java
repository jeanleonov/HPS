package starter;
import jade.core.AID;
import jade.core.Agent;
import jade.core.NotFoundException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

public class SystemStarter extends Agent implements Pathes{
	
	private static final long serialVersionUID = 1L;

	DataFiller dataFiller;
	private ContainerController headContainerController;
	private AgentController settingsAgent, statisticDispatcher;
	AID statisticAID;
	
	private Vector<ContainerController> containerControllers; 
	
	private String	viabilitySettingsPath,
					posteritySettingPath,
					experimentInfoPath,
					scenarioPath,
					statisticPath;
	Integer remainingExperiments;		// TODO !!! implement synchronization
	int numberOfModelingYears;

	@SuppressWarnings("unchecked")
	@Override
	protected void setup(){
		Object[] args = this.getArguments();
		this.viabilitySettingsPath = (String)args[0];
		this.posteritySettingPath = (String)args[1];
		this.scenarioPath = (String)args[2];
		this.experimentInfoPath = (String)args[3];
		this.statisticPath = (String)args[4];
		this.containerControllers = (Vector<ContainerController>)args[5];
		headContainerController = getContainerController();
		startSystem();
	}
	
	public void startSystem(){
		readData();
		createAndStartSettingsAgents();
		getConfirmationFromSettingsAgents();
		createAndStartStatisticDispatcherAgent();
		try {
			remainingExperiments = (Integer)MainClass.getArgument("experiments");
			numberOfModelingYears = (Integer)MainClass.getArgument("years");
		} catch (NotFoundException e) {e.printStackTrace();}
		startExperimetnsProviders();
	}
	
	private void readData(){
		BufferedReader posteritySettingsReader;
		BufferedReader viabilitySettingsReader;
		BufferedReader scenarioReader;
		BufferedReader experimentInfoReader;
		try {
			viabilitySettingsReader = new BufferedReader(new FileReader(viabilitySettingsPath));
			posteritySettingsReader = new BufferedReader(new FileReader(posteritySettingPath));
			experimentInfoReader = new BufferedReader(new FileReader(experimentInfoPath));
			scenarioReader = new BufferedReader(new FileReader(scenarioPath));
			dataFiller = new DataFiller(viabilitySettingsReader, posteritySettingsReader, scenarioReader, experimentInfoReader);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void createAndStartSettingsAgents(){
		for (ContainerController container : containerControllers){
		//*** SETTINGS_AGENT STARTS ON EACH NODE
			try {
				settingsAgent = container.createNewAgent(
									"Settings",
									"settings.Settings",
									new Object[]{
											dataFiller.getViabilityTable(),
											dataFiller.getPosterityTable(),
											getAID()}
								);
				
				settingsAgent.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createAndStartStatisticDispatcherAgent(){
		try {
			statisticDispatcher	= headContainerController.createNewAgent(
										"statisticDispatcher", 
										"statistic.StatisticDispatcher", 
										new Object[]{statisticPath});
			statisticAID = new AID("statisticDispatcher", AID.ISLOCALNAME);
			
			statisticDispatcher.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	private void getConfirmationFromSettingsAgents(){
		//*** CONFIRMATION MUST BE GOT FROM EACH SETTINGS AGENT
		ACLMessage confirm = blockingReceive();
		if (confirm.getPerformative() != ACLMessage.CONFIRM)
			/*throws new Exception("Problems with Settings agent")*/;		// TODO
	}
	
	private void startExperimetnsProviders(){
		for (ContainerController container : containerControllers)
		//*** START ExperimentProvider FOR EACH NODE
			addBehaviour(new ExperimentsProvider(container));
	}
}