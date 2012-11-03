package statistic;

import starter.Shared;
import messaging.Messaging;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class StatisticDispatcherBehaviour extends CyclicBehaviour implements Messaging {

	private static final long serialVersionUID = 1L;
	private int totalPackages = 0;
	private int packageBuffer = Shared.DEFAULT_PACKAGE_BUFFER;
	
	@Override
	public void action() {
		ACLMessage message = getMessage();
		if (message.getPerformative() == ACLMessage.INFORM){
			totalPackages++;
			addPackageFromMessage(message);
		}
		if (message.getPerformative() == ACLMessage.REQUEST)
			exportStatistic();
		if (totalPackages == packageBuffer)
			exportStatistic();
	}

	private void addPackageFromMessage(ACLMessage message){		
		try {
			StatisticPackage statisticPackage = (StatisticPackage)message.getContentObject();
			((StatisticDispatcher)myAgent).addPackage(statisticPackage);
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}
	
	private void exportStatistic() {
		((StatisticDispatcher)myAgent).exportToFile();
	}
	
	private ACLMessage getMessage(){
		return myAgent.blockingReceive();
	}
	
}
