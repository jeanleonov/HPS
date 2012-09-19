package individual;

import java.util.HashMap;


public class IndividualsManagerDispatcher {
	
	public static final int	DEFAULT=0,
							SINGLE_OBJECT_PULL=1,
							MULTIPROC_OBJECT_PULL=2;
	
	private static DefaultManager defaultManager=null;
	private static ObjectPull objectPull=null;
	private static HashMap<Integer,ObjectPull> objectPulls=null;
	
	private static int mode=DEFAULT;
	
	public static void setDispatchingMode(int newMode){
		if (newMode>=0 && newMode<=2)
			mode = newMode;
		else
			mode = DEFAULT;
	}
	
	static public IIndividualsManager getIndividualsManager(int zoneNumber){
		switch (mode){
		case SINGLE_OBJECT_PULL:
			return (objectPull==null)?(objectPull=new ObjectPull()):objectPull;
		case MULTIPROC_OBJECT_PULL:
			if (objectPulls == null){
				objectPulls = new HashMap<Integer, ObjectPull>();
				objectPulls.put((Integer)zoneNumber, new ObjectPull());
			}
			else
				if (!objectPulls.containsKey((Integer)zoneNumber))
					objectPulls.put((Integer)zoneNumber, new ObjectPull());
			return objectPulls.get((Integer)zoneNumber);
		default:
			return (defaultManager==null)?(defaultManager=new DefaultManager()):defaultManager;
		}
	}
}