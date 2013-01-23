package experiment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class Scenario implements Serializable{
	
	private static final long serialVersionUID = 1L;

	final static public byte UNSTARTED = -1;
	
	private int yearCursor=UNSTARTED;

	private Vector<Rule> unstartedRules;
	private Vector<Rule> activeRules;
	private Vector<Rule> finishedRules;
	
	public Scenario(){
		unstartedRules = new Vector<Rule>();
		activeRules = new Vector<Rule>();
		finishedRules = new Vector<Rule>();
	}
	
	public Scenario(Vector<Rule> rules){
		unstartedRules = new Vector<Rule>();
		activeRules = new Vector<Rule>();
		finishedRules = new Vector<Rule>();
		for (Rule rule : rules){
			if (rule.getState(0) == Rule.ACTIVE)
				activeRules.add(rule);
			if (rule.getState(0) == Rule.UNSTARTED)
				unstartedRules.add(rule);
		}
	}
	
	void start(){
		yearCursor = 0;
		updateRuleLists();
	}
	
	private void updateRuleLists(){
		for (Iterator<Rule> iterator=unstartedRules.iterator(); iterator.hasNext(); /*iterator.next()*/){
			Rule rule = iterator.next();
			byte ruleState = rule.getState(yearCursor);
			if (ruleState == Rule.ACTIVE){
				activeRules.add(rule);
				iterator.remove();
			}
		}
		for (Iterator<Rule> iterator=activeRules.iterator(); iterator.hasNext(); /*#iterator.next()*/){
			Rule rule = iterator.next();
			byte ruleState = rule.getState(yearCursor);
			if (ruleState == Rule.FINISHED){
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
