package decisiontree;

import java.util.HashMap;

// Holds data for a training instance
public class Instance {
	
	private final String label;
	private final HashMap<Attribute, String> attributeMap;
	
	public Instance(HashMap<Attribute, String> attributeMap, String label) {
		this.label = label;
		this.attributeMap = attributeMap;
	}
	
	// Developer tool: print instance
	public void print() {
		System.out.println(getLabel() + ": " + getAttributeMap());
	}


	public String getLabel() {
		return label;
	}

	public HashMap<Attribute, String> getAttributeMap() {
		return attributeMap;
	}
}
