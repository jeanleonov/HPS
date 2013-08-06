package experiment.scenario;

import java.util.ArrayList;
import java.util.List;

import experiment.scenario.repeaters.ActionRepeat;

public class Rule {

	final static public byte	FINISHED = 1,
								ACTIVE = 2,
								UNSTARTED = 3,
								FOREVER_BEFORE = -2,
								FOREVER_AFTER = -3;
	
	private ActionRepeat actionRepeat;
	private List<Action> actions;
	private int startYear, endYear;
	
	public Rule(ActionRepeat actionRepeat, List<Action> actions,
			int startYear, int endYear) {
		this.actionRepeat = actionRepeat;
		this.actions = actions;
		this.startYear = startYear;
		this.endYear = endYear;
	}

	public byte getState(int yearNumber) {
		if (	(yearNumber>=startYear || yearNumber==FOREVER_BEFORE)
			&&	(yearNumber<=endYear || yearNumber==FOREVER_AFTER))
			return ACTIVE;
		if (yearNumber>=0 && yearNumber<startYear)
			return UNSTARTED;
		return FINISHED;
	}
	
	public ArrayList<Action> getCommandsForIteration(int iterationNumber) {
		ArrayList<Action> commands = null;
		if (actionRepeat.shouldDoAction(iterationNumber-startYear)) {
			commands = new ArrayList<Action>();
			for (Action action : actions)
				commands.add(action);
		}
		return commands;
	}
}
