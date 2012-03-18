package experiment;

import java.io.Serializable;

abstract public class Action implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * generation of commands to Zones
	 */
	abstract public ExperimentCommand getCommand();
}
