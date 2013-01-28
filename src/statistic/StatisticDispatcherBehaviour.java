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
		else if (message.getPerformative() == ACLMessage.REQUEST){			/*EXPORT#lao*/
			exportStatistic();
		}
		else if(message.getPerformative() == ACLMessage.QUERY_IF){
			sendState(message);
		}
		if (totalPackages == packageBuffer){
			exportStatistic();
		}
	}
	
	private void sendState(ACLMessage in) {
		boolean busy = ((StatisticDispatcher)myAgent).isBusy();
		ACLMessage message = new ACLMessage((busy) ? ACLMessage.REFUSE : ACLMessage.CONFIRM);
		message.addReceiver(in.getSender());
		myAgent.send(message);
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
