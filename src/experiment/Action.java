package experiment;

import java.io.Serializable;

abstract public class Action implements Serializable {

	private static final long serialVersionUID = 1L;
	
	abstract void parseAction(String str);

	/*
	 * generation of commands to Zones
	 */
	abstract public ExperimentCommand getCommand();
}
