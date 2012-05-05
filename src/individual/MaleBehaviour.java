package individual;

import jade.core.AID;

import java.util.Vector;

public class MaleBehaviour extends IndividualBehaviour {

	private static final long serialVersionUID = 1L;

	@Override
	protected void reproduce(Object msgContent) {
		// TODO Auto-generated method stub
		Vector<AID> FemalesInVisionArea = (Vector<AID>)msgContent;
	}

}
