package starter;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.NotFoundException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import jade.domain.JADEAgentManagement.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

public class SystemStarter extends Agent implements Pathes{
	
	private static final long serialVersionUID = 1L;

	DataFiller dataFiller;
	private ContainerController headContainerController;
	private AgentController statisticDispatcher;
	private Vector<AgentController> settingsAgents;
	AID statisticAID;
	
	Vector<ContainerController> containerControllers; 
	
	private String	viabilitySettingsPath,
					posteritySettingPath,
					movePossibilitiesPath,
					experimentInfoPath,
					scenarioPath,
					statisticPath;
	Integer remainingExperiments;		// TODO !!! implement synchronization
	int curExperiment;
	int numberOfModelingYears;
	int multiplier;
	
	boolean shutdownFlag = false;

	@SuppressWarnings("unchecked")
	@Override
	protected void setup(){
		/*
		 * For AMS communication
		 */
		Codec codec = new SLCodec();    
		Ontology jmo = JADEManagementOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(jmo);
		
		Object[] args = this.getArguments();
		this.viabilitySettingsPath = (String)args[0];
		this.posteritySettingPath = (String)args[1];
		this.movePossibilitiesPath = (String)args[2];
		this.scenarioPath = (String)args[3];
		this.experimentInfoPath = (String)args[4];
		this.statisticPath = (String)args[5];
		this.multiplier = (Integer)args[6];
		this.containerControllers = (Vector<ContainerController>)args[7];
		
		settingsAgents = new Vector<AgentController>();
		headContainerController = getContainerController();
		curExperiment = 0;
		
		if((Boolean) args[8]) startSniffer();
		if((Boolean) args[9]) startIntrospector();
		
		startSystem();
	}
	
	private void startAgent(String name, String className) {
		AgentContainer c = getContainerController();
		AgentController a;
		try {
			a = c.createNewAgent(name, className, null);
			a.start();
		} catch (StaleProxyException e2) {
			e2.printStackTrace();
		}
	}
	
	private void startIntrospector() {
		startAgent("Introspector", "jade.tools.introspector.Introspector");
	}
	
	private void startSniffer() {
		startAgent("Sniffer", "jade.tools.sniffer.Sniffer");
	}
	
	private void shutdownPlatformQuery() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);		
		msg.addReceiver(getAMS());
		msg.setLanguage(new SLCodec().getName());
		msg.setOntology(JADEManagementOntology.getInstance().getName());
		try {
			getContentManager().fillContent(msg, new Action(getAID(), new ShutdownPlatform()));
			send(msg);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
	}
	
	private boolean listenForAMSReply() {
	    ACLMessage receivedMessage = blockingReceive(MessageTemplate.MatchSender(getAMS()));
	    Object recMsg;
		try {
			recMsg = getContentManager().extractContent(receivedMessage);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	    return recMsg.getClass() == Done.class;
	}
	
	@Override
	public void doDelete() {
		if(shutdownFlag) return;
		shutdownFlag = true;
		
		shutdownPlatformQuery();
		if(listenForAMSReply()) MainClass.shutDown();
		else System.err.println("Cannot shutdown platform");
		super.doDelete();
	}
	
	public void startSystem(){
		readData();
		createAndStartSettingsAgents();
		getConfirmationFromSettingsAgents();
		createAndStartStatisticDispatcherAgent();
		getConfirmationFromStatisticDispatcherAgent();
		try {
			remainingExperiments = (Integer)MainClass.getArgument("experiments");
			numberOfModelingYears = (Integer)MainClass.getArgument("years");
		} catch (NotFoundException e) {e.printStackTrace();}
		startExperimetnProvider();
	}
	
	private void readData(){
		BufferedReader posteritySettingsReader;
		BufferedReader viabilitySettingsReader;
		BufferedReader movePossibilitiesReader;
		BufferedReader scenarioReader;
		BufferedReader experimentInfoReader;
		try {
			viabilitySettingsReader = new BufferedReader(new FileReader(viabilitySettingsPath));
			posteritySettingsReader = new BufferedReader(new FileReader(posteritySettingPath));
			movePossibilitiesReader = new BufferedReader(new FileReader(movePossibilitiesPath));
			experimentInfoReader = new BufferedReader(new FileReader(experimentInfoPath));
			scenarioReader = new BufferedReader(new FileReader(scenarioPath));
			dataFiller = new DataFiller(viabilitySettingsReader, posteritySettingsReader, movePossibilitiesReader, scenarioReader, experimentInfoReader);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void createAndStartSettingsAgents(){
		for (ContainerController container : containerControllers){
		//*** SETTINGS_AGENT STARTS ON EACH NODE
			try {
				AgentController settingsAgent =
								container.createNewAgent(
									"Settings",
									"settings.Settings",
									new Object[]{
											dataFiller.getViabilityTable(),
											dataFiller.getPosterityTable(),
											dataFiller.getMovePosibilitiesTable(),
											getAID()}
								);
				settingsAgents.add(settingsAgent);
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
										new Object[]{statisticPath,
													 getAID()}
								  );
			statisticAID = new AID("statisticDispatcher", AID.ISLOCALNAME);
			
			statisticDispatcher.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	private void getConfirmationFromSettingsAgents(){
		for (int i = 0; i < containerControllers.size(); i++){
			ACLMessage confirm = blockingReceive();
			if (confirm.getPerformative() != ACLMessage.CONFIRM)
				/*throws new Exception("Problems with Settings agent")*/;		// TODO
		}
	}
	
	private void getConfirmationFromStatisticDispatcherAgent(){
		ACLMessage confirm = blockingReceive();
		if (confirm.getPerformative() != ACLMessage.CONFIRM)
			/*throws new Exception("Problems with StatisticDispather agent")*/;		// TODO
	}
	
	private void startExperimetnProvider(){
		addBehaviour(new ExperimentsProvider());
	}
}