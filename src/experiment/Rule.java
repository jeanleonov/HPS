package experiment;

import java.util.Vector;

public class Rule {
	
	final static public byte	FINISHED = 1,
								ACTIVE = 2,
								UNSTARTED = 3;
	
	private ActionApperance actionApperance;
	private Action[] actions;
	private int startYear, endYear;
	
	public Rule(String str){
		// TODO Parsing
	}
	
	public byte getState(int yearNumber){
		if (yearNumber>=0 && yearNumber<startYear)
			return UNSTARTED;
		if (yearNumber>=startYear && yearNumber<=endYear)
			return ACTIVE;
//		if (yearNumber>endYear)
			return FINISHED;
	}
	
	public Vector<Command> getCommandsForIteration(int iterationNumber){
		Vector<Command> commands = new Vector<Command>();
		if (actionApperance.shouldDoAction(iterationNumber-startYear))
			for (Action action : actions)
				commands.add(action.getCommand());
		return commands;
	}
}
