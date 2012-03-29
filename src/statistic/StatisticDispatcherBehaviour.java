package statistic;

import messaging.Messaging;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class StatisticDispatcherBehaviour extends CyclicBehaviour implements Messaging {

	private int totalPackages = 0;
	private int exportingFrequency = 10;
	@Override
	public void action() {
		ACLMessage message = getMessage();
		if (message.getContent() == STATISTIC){
			totalPackages++;
			addPackageFromMessage(message);
		}
		if (message.getContent() == EXPORT){
			exportStatistic();
		}
		if ((totalPackages % 100/exportingFrequency) == 0){
			exportStatistic();
		}
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
