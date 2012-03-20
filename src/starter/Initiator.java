package starter;


import genotype.Genotype;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;
import java.util.Vector;

import distribution.*;

import jade.core.behaviours.OneShotBehaviour;


public class Initiator extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	
	// TODO

	private int iterationNum;
	private String source = new String();
	protected int years;
	protected ExperimentDistribution exp = new ExperimentDistribution();
	
	public Initiator()
	{
		super();
		this.source = "Initiation.hpsi";
	}
	
	public Initiator(String Source)
	{
		super();
		this.source = Source;
	}
	
	public void action()
	{
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(source));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try
			{
				years = Integer.parseInt(in.readLine());
				iterationNum = Integer.parseInt(in.readLine());                 // number of ticks  
				int dNum = Integer.parseInt(in.readLine());				// number of distribution units
																				// at the beginning of modeling
				String res = new String(in.readLine());
				while(res != null) //for(int i = 0; i < dNum; i++) 	// for each genotype
				{
					//in.readLine();
					
					ZoneDistribution zone = new ZoneDistribution();
					
					//----- genotype distribution initialisation ---------------------------------
					while(res != ";")
					{
						GenotypeAgeNumberTrio gen = new GenotypeAgeNumberTrio();
						
						res = in.readLine();
						Genotype type = Genotype.getGenotype(res);
						
						//----- reading age - number distribution ------------------------------------
						res = in.readLine();
						while(res != "\n")
						{
							float age = Float.parseFloat(res);
							
							res = in.readLine();
							int measure = Integer.parseInt(res);
							
							gen.addAgeDistribution(age, count);
							res = in.readLine();
						}
						
						zone.addGenotypeDistribution(type, gen);
						
					}
					//----------------------------------------------
					
					exp.addZoneDistribution(zone);
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("Incorrect file format");
				System.exit(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

			
			System.out.println(years + " " + iterationNum);
	/*		for(int i = 0; i < info.size(); i++)
			{
				System.out.println(info.get(i).getZoneId() + " " + info.get(i).getAge() + " " + info.get(i).getCount());
				System.out.println(info.get(i).getGenotype().getGenomeSet());
				//info.get(i).getGenotype().getGenomeSet().//TreeSet<Genome>
			}*/
			//Initiator n = new Initiator(myAgent, iterationNum, info);
			//myAgent.addBehaviour(n);
	}
	
//	@Override
//	public void action() {
		// TODO
		
		// call of Experiment, Settings, StatisticDispatcher setups will be around here!!
		// SOMETHING LIKE THIS:
		
// ~ 	ContainerController controller = this.getContainerController();
//		Vector<AgentController> experimentAgents = new Vector<AgentController>;
//		Vector<AID> experimentAIDs = new Vector<AID>();
//		Object[] data = new Object[.?.];
//		fillData(data);
//
//		for (each experiment) {
//			String name = "Experiment_" + i;
//			experimentAgents.add(controller.createNewAgent("Experiment_" + i, "experiment.Experiment", data));
//			experimentAIDs.add(new AID(name, AID.ISLOCALNAME));
//		}
		
//		for (each created agent)
//			agent.start();
//	}
}