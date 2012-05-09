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
	private Vector<Pair<AID, Double>> neighbours;
	
	public Migration(Zone myAgent){
		this.myZone = myAgent;
		neighbours = myZone.getNeighbours();
	}
	
	public void action(ZoneDistribution distribution){
		Vector<AID> individuals = myZone.getIndividuals();
		double attractivness = myZone.getAttractivness();
		
		for(AID individual : individuals){
			if(Math.random() > attractivness){
				choozeZone(individual);
			}
		}
		
		// now it's dead code, but it may be useful in future
		if(distribution != null) myZone.createIndividuals(distribution);
	}
	
	private void choozeZone(AID individual){
		double weightSum = 0;
		for(Pair<AID, Double> i : neighbours){
			weightSum += i.getSecond();
		}
		double point = Math.random() * weightSum;
		double totalWeight = 0;
		int i = 0;
		
		while((point > totalWeight + neighbours.get(i).getSecond()) && i < neighbours.size()){
			totalWeight += neighbours.get(i).getSecond();
			i++;
		}
		
		if(neighbours.get(i).getFirst() != null){			
			innerMigration(neighbours.get(i).getFirst(), individual);		
		}
		else{
			outerMigration(individual);
		}
		myZone.killIndividual(individual);
	}
	
	private void innerMigration(AID newHome, AID individual){
		try {
			ACLMessage journey = new ACLMessage(ACLMessage.REQUEST);
			journey.setLanguage(MIGRATION);
			journey.setContentObject(newHome);
			journey.addReceiver(individual);
			myZone.send(journey);
		} catch (IOException e) {
			System.out.println("Individual migration error");
			e.printStackTrace();
		}
	}
	
	private void outerMigration(AID individual){
		ACLMessage journey = new ACLMessage(ACLMessage.INFORM);
		journey.setLanguage(MIGRATION);
		journey.setContent(DIE);
		journey.addReceiver(individual);
		myZone.send(journey);
	}
}
