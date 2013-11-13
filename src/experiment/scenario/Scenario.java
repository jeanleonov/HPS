package experiment.scenario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Scenario {

	final static public byte UNSTARTED = -1;
	
	private int yearCursor=UNSTARTED;

	private List<Rule> unstartedRules;
	private List<Rule> activeRules;
	private List<Rule> finishedRules;
	
	public Scenario() {
		unstartedRules = new ArrayList<>(0);
		activeRules = new ArrayList<>(0);
		finishedRules = new ArrayList<>(0);
	}
	
	public Scenario(List<Rule> rules) {
		unstartedRules = rules;
		activeRules = new ArrayList<>(0);
		finishedRules = new ArrayList<>(0);
	}
	
	private void reset(List<Rule> rules) {
		unstartedRules = new LinkedList<Rule>();
		activeRules = new LinkedList<Rule>();
		for (Rule rule : rules) {
			if (rule.getState(0) == Rule.ACTIVE)
				activeRules.add(rule);
			if (rule.getState(0) == Rule.UNSTARTED)
				unstartedRules.add(rule);
		}
	}
	
	public void start() {
		List<Rule> rules = new ArrayList<>(unstartedRules.size() + activeRules.size() + finishedRules.size());
		rules.addAll(activeRules);
		rules.addAll(unstartedRules);
		rules.addAll(finishedRules);
		reset(rules);
		yearCursor = 0;
		updateRuleLists();
	}
	
	private void updateRuleLists(){
		for (Iterator<Rule> iterator=unstartedRules.iterator(); iterator.hasNext(); ) {
			Rule rule = iterator.next();
			byte ruleState = rule.getState(yearCursor);
			if (ruleState == Rule.ACTIVE) {
				activeRules.add(rule);
				iterator.remove();
			}
		}
		for (Iterator<Rule> iterator=activeRules.iterator(); iterator.hasNext(); ) {
			Rule rule = iterator.next();
			byte ruleState = rule.getState(yearCursor);
			if (ruleState == Rule.FINISHED) {
				finishedRules.add(rule);
				iterator.remove();
			}
		}
	}
	
	public ArrayList<Action> getCommandsForNextYear(int experimentYearCursor) {
		yearCursor = experimentYearCursor;
		ArrayList<Action> commands = new ArrayList<Action>();
		for (Rule rule : activeRules){
			ArrayList<Action> toAdd = rule.getCommandsForIteration(yearCursor);
			if (toAdd != null)
				commands.addAll(toAdd);
		}
		yearCursor++;
		updateRuleLists();
		return commands;
	}

	public int getYearCursor() {
		return yearCursor;
	}
}
