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
		unstartedRules = new LinkedList<Rule>();
		activeRules = new LinkedList<Rule>();
		finishedRules = new LinkedList<Rule>();
	}
	
	public Scenario(List<Rule> rules) {
		unstartedRules = new LinkedList<Rule>();
		activeRules = new LinkedList<Rule>();
		finishedRules = new LinkedList<Rule>();
		for (Rule rule : rules) {
			if (rule.getState(0) == Rule.ACTIVE)
				activeRules.add(rule);
			if (rule.getState(0) == Rule.UNSTARTED)
				unstartedRules.add(rule);
		}
	}
	
	public void start() {
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
	
	// experimentYearCursor is necessary to avoid desynchronization
	public ArrayList<Action> getCommandsForNextYear(int experimentYearCursor){
		// TODO if (experimentYearCursor != yearCursor) throw ...; OR reorganize all.
		ArrayList<Action> commands = new ArrayList<Action>();
		for (Rule rule : activeRules){
			ArrayList<Action> toAdd = rule.getCommandsForIteration(yearCursor);
			if (toAdd != null)
				commands.addAll(toAdd);
		}
		updateRuleLists();
		yearCursor++;
		return commands;
	}

	public int getYearCursor() {
		return yearCursor;
	}
}
