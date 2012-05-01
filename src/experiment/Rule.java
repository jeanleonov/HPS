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
	
	private ActionAppearance actionAppearance;
	private Vector<Action> actions;
	private int startYear, endYear;
	
	public Rule(ActionAppearance actionAppearance, Vector<Action> actions,
			int startYear, int endYear) {
		this.actionAppearance = actionAppearance;
		this.actions = actions;
		this.startYear = startYear;
		this.endYear = endYear;
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
	
	public Vector<Action> getCommandsForIteration(int iterationNumber){
		Vector<Action> commands = null;
		if (actionAppearance.shouldDoAction(iterationNumber-startYear)){
			commands = new Vector<Action>();
			for (Action action : actions)
				commands.add(action);
		}
		return commands;
	}
}
