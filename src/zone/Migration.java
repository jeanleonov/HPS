package zone;

import java.io.IOException;
import java.util.Vector;

import distribution.ZoneDistribution;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Migration {
	private Zone myZone;
	ZoneDistribution distribution;
	
	public Migration(Zone myAgent, ZoneDistribution distribution){
		this.myZone = myAgent;
		this.distribution = distribution;
	}
	
	public void action(){
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
						journey.setLanguage("Migration");
						journey.setContentObject(individual);
						journey.addReceiver(neighbours.get(i).getFirst());
						myZone.send(journey);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("Individual migration error");
						e.printStackTrace();
					}
				}
				myZone.killIndividual(individual);
			}
		}
		
		myZone.createIndividuals(distribution);
	}
}
