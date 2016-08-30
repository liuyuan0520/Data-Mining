package decisiontreeV2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

// Stores a set of instances
public class DataSet {

	// Ordered list of instances
	public ArrayList<Instance> instances;
	public ArrayList<Attribute> attributes;

	public DataSet() {
		instances = new ArrayList<Instance>();
		attributes = new ArrayList<Attribute>();
	}

	// Add instance to data set (add all attributes before doing this)
	public void addInstance(String line) {
		ArrayList<String> features = new ArrayList<String>(Arrays.asList(line.split(",")));
		String label = features.remove(features.size() - 1);
		LinkedHashMap<String, String> values = new LinkedHashMap<String, String>();
		for (int i = 0; i < features.size(); i++)
			values.put(attributes.get(i).name, features.get(i));
		instances.add(new Instance(values, label));
	}

	// Add attribute to data set
	public void addAttribute(String line) {
	 // Break line into attribute name and attributeMap
        String[] tokens = line.split(" ");
        ArrayList<String> values = new ArrayList<>(Arrays.asList(tokens[2].split(",")));
        if (values.size() != 1) {
            values.set(0,values.get(0).replace("{", ""));
            values.set(values.size() - 1,values.get(values.size() - 1).replace("}", ""));
        }
        attributes.add(new Attribute(values, tokens[1]));
	}

	// Developer tool: print data set
	public void print() {
		for (Attribute attribute : attributes)
			attribute.print();
		for (Instance instance : instances)
			instance.print();
	}

}
