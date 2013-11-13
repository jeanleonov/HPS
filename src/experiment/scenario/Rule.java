package experiment.scenario;

import java.util.ArrayList;
import java.util.List;

import experiment.scenario.repeaters.ActionRepeat;

public class Rule {

	final static public int 	FINISHED = 1,
								ACTIVE = 2,
								UNSTARTED = 3,
								FOREVER_BEFORE = 0,
								FOREVER_AFTER = Integer.MAX_VALUE;
	
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
		if (yearNumber>=startYear && yearNumber<=endYear)
			return ACTIVE;
		if (yearNumber<startYear)
			return UNSTARTED;
		return FINISHED;
	}
	
	public ArrayList<Action> getCommandsForIteration(int iterationNumber) {
		ArrayList<Action> commands = null;
		if (actionRepeat.shouldDoAction(iterationNumber)) {
			commands = new ArrayList<Action>();
			for (Action action : actions)
				commands.add(action);
		}
		return commands;
	}
}
