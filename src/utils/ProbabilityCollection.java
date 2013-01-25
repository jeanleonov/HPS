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
		totalSumOfProbabilities = getTotalSumOfProbabilities(elements);
	}
	
	static private <T extends WithProbability>
	double getTotalSumOfProbabilities(List<T> collection) {
		double totalSumOfProbabilities=0;
		for (WithProbability element : collection)
			totalSumOfProbabilities += element.getProbability();
		return totalSumOfProbabilities;
	}
	
	/** @return random elements according to specified probabilities */
	public Set<T> getElements(float approximateQuantity) {
		HashSet<T> result = new HashSet<T>();
		double coeficient = approximateQuantity/elements.size()/(totalSumOfProbabilities/elements.size());
		for (T element : elements)
			if (Math.random() <= element.getProbability()*coeficient)
				result.add(element);
		float aa = approximateQuantity;					// 
		float a = result.size() - approximateQuantity;	// #temporary.. collapse it to one line
		float correction = -a/aa;						// 
		stubCoeficient += correction;
		return result;
	}
	
	/** @return random elements according to specified probabilities 
	 *  */
	/*public List<T> getElementsForCapacity(float approximateCapacity) {
		List<T> result = new ArrayList<T>();
		double coeficient = totalSumOfProbabilities/approximateCapacity*stubCoeficient;
		for (T element : elements)
			if (Math.random() <= element.getProbability()*coeficient)
				result.add(element);
		float aa = approximateCapacity;																			// 
		float a = (float) ((float)getTotalSumOfProbabilities(result) - (approximateCapacity-totalSumOfProbabilities));	// #temporary.. collapse it to one line
		float correction = -a/aa;																				// 
		stubCoeficient += correction;
		return result;
	}*/
}
