package starter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** 
 * Class that prepares inputs for the new point of configuration space 
 * */
public class InputsPreparer {
	
	private BufferedReader dimensionsConfigurationsReader;
	private List<String> dimensionsIDs;
	private List<Class<?>> dimensionValueClasses;
	private List<Integer> totalSteps;
	private List<Integer> currentSteps;
	private int pointNumber;

	public InputsPreparer(String dimensionsToTestPath) throws Exception {
		this.dimensionsConfigurationsReader = new BufferedReader(new FileReader(dimensionsToTestPath));
		this.dimensionsIDs = new ArrayList<>();
		this.dimensionValueClasses = new ArrayList<>();
		this.totalSteps = new ArrayList<>();
		this.currentSteps = new ArrayList<>();
		initDimensions();
	}
	
	public void setPoint(int point) throws Exception {
		pointNumber = point;
		currentSteps.clear();
		for (int i=0; i<dimensionsIDs.size(); i++)
			currentSteps.add(0);
		initCurrentSteps();
	}
	
	private void initDimensions() throws Exception {
		String line;
		while ((line = dimensionsConfigurationsReader.readLine()) != null) {
			if (line.isEmpty())
				continue;
			String[] cells = line.replace(" ", "").split(";");
			String dimensionID = cells[0];
			String clazz = cells[1];
			Integer steps = Integer.parseInt(cells[2]);
			dimensionsIDs.add(dimensionID);
			dimensionValueClasses.add(getClassByString(clazz));
			totalSteps.add(steps);
		}
	}
	
	private Class<?> getClassByString(String clazz) throws Exception {
		if (clazz.equals("integer"))
			return Integer.class;
		if (clazz.equals("float"))
			return Double.class;
		throw new Exception("Wrong content of file with dimensions configurations. \n");
	}
	
	private void initCurrentSteps() throws Exception {
		if (pointNumber > maxPointNumber())
			throw new Exception("Too big point number.");
		for (int i=0; i<pointNumber; i++)
			moveToNextPoint(0);
	}
	
	public int maxPointNumber() {
		int result = 1;
		for (Integer steps : totalSteps)
			result *= steps;
		return result-1;
	}
	
	private void moveToNextPoint(int index) {
		int currentValueOnDimension = currentSteps.get(index);
		if (currentValueOnDimension < totalSteps.get(index)-1)
			currentSteps.set(index, currentValueOnDimension+1);
		else {
			currentSteps.set(index, 0);
			moveToNextPoint(index+1);
		}
	}
	
	public String getPreparedContent(String inputPath) throws Exception {
		String content = getFullContent(inputPath);
		return putThisPointsValues(content);
	}
	
	private String getFullContent(String inputPath) throws IOException {
		BufferedReader inputReader = new BufferedReader(new FileReader(inputPath));
		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = inputReader.readLine()) != null)
			builder.append(line).append('\n');
		inputReader.close();
		return builder.toString();	
	}
	
	private String putThisPointsValues(String content) throws Exception {
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<content.length(); i++)
			if (i+1<content.length() && content.charAt(i)=='#' && content.charAt(i+1)=='{')
				i = processChangeableToken(i, builder, content);
			else
				builder.append(content.charAt(i));
		return builder.toString();
	}
	
	private int processChangeableToken(int sharpIndex, StringBuilder globalBuilder, String content) throws Exception {
		int tokenFinishIndex = getTokenFinishIndex(sharpIndex, content);
		String changeableToken = content.substring(sharpIndex+2, tokenFinishIndex).replace(" ", "");
		String[] subTokens = changeableToken.split(":");
		String dimensionID = subTokens[0];
		String[] valuesTokens = subTokens[1].split("\\|");
		globalBuilder.append(translateToken(dimensionID, valuesTokens[0], valuesTokens[1]));
		return tokenFinishIndex;
	}
	
	private int getTokenFinishIndex(int sharpIndex, String content) {
		int i;
		for (i=sharpIndex+2; i<content.length() && content.charAt(i)!='}'; i++);
		return i;
	}
	
	private String translateToken(String dimensionID, String firstValueString, String lastValueString) throws Exception {
		int dimensionIndex = dimensionsIDs.indexOf(dimensionID);
		Integer steps = totalSteps.get(dimensionIndex);
		Integer currentStep = currentSteps.get(dimensionIndex);
		if (dimensionValueClasses.get(dimensionIndex).equals(Integer.class)) {
			Integer first = Integer.parseInt(firstValueString);
			Integer last = Integer.parseInt(lastValueString);
			return ((Integer)(first + (last-first)/steps*currentStep)).toString();
		}
		if (dimensionValueClasses.get(dimensionIndex).equals(Double.class)) {
			Double firstInt = Double.parseDouble(firstValueString);
			Double lastInt = Double.parseDouble(lastValueString);
			return ((Double)(firstInt + (lastInt-firstInt)/steps*currentStep)).toString();
		}
		throw new Exception("Changeable token translation was failed.");
	}
}
