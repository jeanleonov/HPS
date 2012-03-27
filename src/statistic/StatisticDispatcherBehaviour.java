package statistic;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class StatisticDispatcherBehaviour extends CyclicBehaviour {

	@Override
	public void action() {
		StatisticPackage pack = getPackage();
		((StatisticDispatcher)myAgent).addPackage(pack);
	}
	
	private StatisticPackage getPackage(){
		Object message = getMessage();
		return (StatisticPackage)message;
	}
	private Object getMessage(){
		try {	
			ACLMessage message = myAgent.blockingReceive();	// WARNING MAY BE CAN'T BE NULL ?{
			return message.getContentObject();
		}
		catch (UnreadableException e) {
			e.printStackTrace();
		}
		return null;
	}
}
