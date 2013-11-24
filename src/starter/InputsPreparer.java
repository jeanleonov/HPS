package starter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * Class that prepares inputs for the new point of configuration space 
 * */
public class InputsPreparer {
	
	private BufferedReader dimensionsConfigurationsReader;
	private List<String> dimensionsIDs;
	private List<Class<?>> dimensionValueClasses;
	private List<Integer> totalSteps;
	private List<Integer> currentSteps;
	private Map<String,String> onPointValues;
	private int pointNumber;

	private static final String contentRegex = "(?<static>(?s).+?)?(:?#\\{(?<dinamic>.*?)\\})|(?<juststatic>(?s).*)";
	private static final String templateRegex = "(?<dimension>\\w+)(:?\\((?<valueName>\\w+)\\))?:(?<first>[\\d.,]+)-(?<last>[\\d.,]+)";

	public InputsPreparer(String dimensionsToTestPath) throws Exception {
		this.dimensionsConfigurationsReader = new BufferedReader(new FileReader(dimensionsToTestPath));
		this.dimensionsIDs = new ArrayList<>();
		this.dimensionValueClasses = new ArrayList<>();
		this.totalSteps = new ArrayList<>();
		this.currentSteps = new ArrayList<>();
		this.onPointValues = new HashMap<>();
		initDimensions();
	}
	
	public void setPoint(int point) throws Exception {
		pointNumber = point;
		onPointValues.clear();
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
	
	public String getPreparedContent(String content) throws Exception {
		Matcher matcher = Pattern.compile(contentRegex).matcher(content);
		StringBuilder prepared = new StringBuilder("");
		while(matcher.find()) {
			String staticPart = matcher.group("juststatic");
			String dinamic = "";
			if (staticPart == null) {
				staticPart = matcher.group("static");
				String dinamicTemplate = matcher.group("dinamic");
				dinamic = compileTemplate(dinamicTemplate);
			}
			prepared.append(staticPart).append(dinamic);
		}
		return prepared.toString();
	}
	
	private String compileTemplate(String template) throws Exception {
		template = template.replace(" ", "");
		Matcher matcher = Pattern.compile(templateRegex).matcher(template);
		String dim=null, valueName=null, first=null, last=null;
		matcher.find();
		dim = matcher.group("dimension");
		valueName = matcher.group("valueName");
		first = matcher.group("first");
		last = matcher.group("last");
		String result = translate(dim, first, last);
		if (valueName == null)
			valueName = dim;
		onPointValues.put(valueName, result);
		return result;
	}
	
	private String translate(String dimID, String firstValStr, String lastValStr) throws Exception {
		int dimensionIndex = dimensionsIDs.indexOf(dimID);
		Integer steps = totalSteps.get(dimensionIndex);
		Integer currentStep = currentSteps.get(dimensionIndex);
		if (dimensionValueClasses.get(dimensionIndex).equals(Integer.class)) {
			Integer first = Integer.parseInt(firstValStr);
			Integer last = Integer.parseInt(lastValStr);
			return ((Integer)(first + ((last-first)*currentStep)/steps)).toString();
		}
		if (dimensionValueClasses.get(dimensionIndex).equals(Double.class)) {
			Double firstInt = Double.parseDouble(firstValStr);
			Double lastInt = Double.parseDouble(lastValStr);
			return ((Double)(firstInt + ((lastInt-firstInt)*currentStep)/steps)).toString();
		}
		throw new Exception("Changeable token translation was failed.");
	}
	
	public Map<String,String> getPrevPointValuesMap() {
		return onPointValues;
	}
}
