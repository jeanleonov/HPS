package utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProbabilityCollection<T extends WithProbability> {
	
	private List<T> elements;
	private double totalSumOfProbabilities=0;
	static float stubCoeficient=1.269f;

	public ProbabilityCollection(List<T> collection) {
		elements = collection;
		for (WithProbability element : elements)
			totalSumOfProbabilities += element.getProbability();
	}
	
	/** @return random elements according to specified probabilities */
	public Set<T> getElements(float approximateQuantity) {
		HashSet<T> result = new HashSet<T>();
		double coeficient = totalSumOfProbabilities/approximateQuantity/(totalSumOfProbabilities/elements.size())/stubCoeficient;
		for (T element : elements)
			if (Math.random() <= element.getProbability()/coeficient)
				result.add(element);
		float aa = approximateQuantity;					// 
		float a = result.size() - approximateQuantity;	// #temporary.. collapse it to one line
		float correction = -a/aa;						// 
		stubCoeficient += correction;
		return result;
	}
	
	/** @return random elements according to specified probabilities */
	/*public Set<T> removeAntiElementsForCapacity(float approximateCapacity) {
		HashSet<T> result = new HashSet<T>();
		double coeficient = totalSumOfProbabilities/approximateCapacity/(totalSumOfProbabilities/elements.size())/stubCoeficient;
		for (T element : elements)
			if (Math.random() <= element.getProbability()/coeficient)
				result.add(element);
		float aa = approximateCapacity;					// 
		float a = result.size() - approximateCapacity;	// #temporary.. collapse it to one line
		float correction = -a/aa;						// 
		stubCoeficient += correction;
		return result;
	}*/
}
