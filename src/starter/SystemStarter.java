package starter;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.NotFoundException;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class SystemStarter extends Agent implements Pathes{
	
	private static final long serialVersionUID = 1L;

	DataFiller dataFiller;
	ContainerController container; 
	private AgentController statisticDispatcher;
	private AgentController settingsAgent;
	AID statisticAID;
	
	
	private String	viabilitySettingsPath,
					posteritySettingPath,
					movePossibilitiesPath,
					experimentInfoPath,
					scenarioPath;
	Integer remainingExperiments;
	int curExperiment;
	int numberOfModelingYears;
	int indivMultiplier;
	int zoneMultiplier;
	
	boolean shutdownFlag = false;

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
		this.indivMultiplier = (Integer)args[5];
		this.zoneMultiplier = (Integer)args[6];
		container = getContainerController();
		curExperiment = (Integer)args[7];
		if((Boolean) args[8]) startSniffer();
		if((Boolean) args[9]) startIntrospector();
		startSystem();
	}
	
	private void startAgent(String name, String className) {
		AgentController a;
		try {
			a = container.createNewAgent(name, className, null);
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
		getConfirmationFromSettingsAgent();
		createAndStartStatisticDispatcherAgent();
		getConfirmationFromStatisticDispatcherAgent();
		try {
			remainingExperiments = (Integer)MainClass.getArgument("number_of_experiments") - curExperiment;
			numberOfModelingYears = (Integer)MainClass.getArgument("years");
		} catch (NotFoundException e) {e.printStackTrace();}
		if (curExperiment == -1)
			curExperiment = 0;
		if (remainingExperiments < 1)
			remainingExperiments = 1;
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
			if (movePossibilitiesPath.endsWith(MainClass.DEFAULT_MAP))
				movePossibilitiesReader = null;
			else
				movePossibilitiesReader = new BufferedReader(new FileReader(movePossibilitiesPath));
			experimentInfoReader = new BufferedReader(new FileReader(experimentInfoPath));
			scenarioReader = new BufferedReader(new FileReader(scenarioPath));
			dataFiller = new DataFiller(viabilitySettingsReader, posteritySettingsReader, movePossibilitiesReader, scenarioReader, experimentInfoReader, zoneMultiplier);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void createAndStartSettingsAgents(){
		try {
			settingsAgent =
							container.createNewAgent(
								"Settings",
								"settings.Settings",
								new Object[]{
										dataFiller.getViabilityTable(),
										dataFiller.getPosterityTable(),
										dataFiller.getMovePosibilitiesTable(),
										getAID()}
							);
			settingsAgent.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	private void createAndStartStatisticDispatcherAgent(){
		try {
			statisticDispatcher	= container.createNewAgent(
										"statisticDispatcher", 
										"statistic.StatisticDispatcher", 
										new Object[]{"statistics" +
													((curExperiment==-1)?(""):("_"+curExperiment)) +
													".csv",
													 getAID()}
								  );
			statisticAID = new AID("statisticDispatcher", AID.ISLOCALNAME);
			statisticDispatcher.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	private void getConfirmationFromSettingsAgent(){
		ACLMessage confirm = blockingReceive();
		if (confirm.getPerformative() != ACLMessage.CONFIRM)
			/*throws new Exception("Problems with Settings agent")*/;		// TODO
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