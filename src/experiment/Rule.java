package experiment;

import java.io.Serializable;
import java.util.Vector;

public class Rule implements Serializable {
	
	private static final long serialVersionUID = 1L;

	final static public byte	FINISHED = 1,
								ACTIVE = 2,
								UNSTARTED = 3,
								FOREVER_BEFORE = -2,
								FOREVER_AFTER = -3;
	
	private ActionApperance actionApperance;
	private Action[] actions;
	private int startYear, endYear;
	
	public Rule(String str){
		// TODO Parsing
	}
	
	public byte getState(int yearNumber){
		if (	(yearNumber>=startYear || yearNumber==FOREVER_BEFORE)
			&&	(yearNumber<=endYear || yearNumber==FOREVER_AFTER))
			return ACTIVE;
		if (yearNumber>=0 && yearNumber<startYear)
			return UNSTARTED;
//		if (yearNumber>endYear)
			return FINISHED;
	}
	
	public Vector<ExperimentCommand> getCommandsForIteration(int iterationNumber){
		Vector<ExperimentCommand> commands = new Vector<ExperimentCommand>();
		if (actionApperance.shouldDoAction(iterationNumber-startYear))
			for (Action action : actions)
				commands.add(action.getCommand());
		return commands;
	}
}
