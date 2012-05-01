package experiment;

import java.io.Serializable;
import java.util.Vector;

public class Action implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	Vector<Integer> zonesNumbers;
	ZoneCommand command;
	
	public Action(Vector<Integer> zonesNumbers, ZoneCommand command){
		this.zonesNumbers = zonesNumbers;
		this.command = command;
	}
}
