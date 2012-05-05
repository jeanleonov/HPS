package zone;

import java.io.IOException;

import java.util.Vector;

import messaging.Messaging;

import distribution.ZoneDistribution;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Migration implements Messaging{
	private Zone myZone;
	
	public Migration(Zone myAgent){
		this.myZone = myAgent;
	}
	
	public void action(ZoneDistribution distribution){
		Vector<AID> individuals = myZone.getIndividuals();
		double attractivness = myZone.getAttractivness();
		Vector<Pair<AID, Double>> neighbours = myZone.getNeighbours();
		
		double sum = 0;
		for(Pair<AID, Double> i : neighbours){
			sum += i.getSecond();
		}
		
		for(AID individual : individuals){
			if(Math.random() > attractivness){
				double point = Math.random() * sum;
				double s = 0;
				int i = 0;
				
				while((point > s + neighbours.get(i).getSecond()) && i < neighbours.size()){
					s += neighbours.get(i).getSecond();
					i++;
				}
				
				if(neighbours.get(i).getFirst() != null){
					//TODO: register individual in this zone
					// and don't forget to tell individual about it!
					
					
					try {
						ACLMessage journey = new ACLMessage(ACLMessage.REQUEST);
						journey.setLanguage(MIGRATION);
						journey.setContentObject(neighbours.get(i).getFirst());
						journey.addReceiver(individual);
						myZone.send(journey);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("Individual migration error");
						e.printStackTrace();
					}
				}
				else{
					ACLMessage journey = new ACLMessage(ACLMessage.INFORM);
					journey.setLanguage(MIGRATION);
					journey.setContent(DIE);
					journey.addReceiver(individual);
					myZone.send(journey);
				}
				myZone.killIndividual(individual);
			}
		}
		
		if(distribution != null) myZone.createIndividuals(distribution);
	}
}
