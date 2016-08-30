package decisiontree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

// Stores a set of instances
public class DataSet {

    public ArrayList<Attribute> attributes;
    public ArrayList<Instance> instances;
    public ArrayList<String> labels;

    // Add attribute to data set
    public void addAttribute(String line) {
        // Break line into attribute name and attributeMap
        String[] tokens = line.split(" ");

        if (!tokens[1].toLowerCase().equals("label")) {
            ArrayList<String> values = new ArrayList<>(Arrays.asList(tokens[2].split(",")));
            if (values.size() != 1) {
                values.set(0, values.get(0).replace("{", ""));
                values.set(0, values.get(values.size() - 1).replace("}", ""));

                DiscreteAttribute attribute = new DiscreteAttribute(tokens[1]);
                attribute.setValues(values);
                attributes.add(attribute);

            } else {
                attributes.add(new ContinuousAttribute(tokens[1]));
            }
        } else {
            addLabels(line);
        }
    }

    // Add instance to data set (add all attributes before doing this)
    public void addInstance(String line) {
        String[] values = line.split(",");
        HashMap<Attribute, String> attributes = new HashMap<>();
        for (int i = 0; i < values.length - 1; i++) {
            attributes.put(this.attributes.get(i), values[i]);
        }
        instances.add(new Instance(attributes, values[values.length - 1]));
    }

    private void addLabels(String line) {
        String[] tokens = line.split(" ");
        if (tokens[1].toLowerCase().equals("labels")) {
            String[] labelList = tokens[2].split("\\W+");
            for (String label : labelList) {
                labels.add(label);
            }
        }
    }

    // Initially read in file.
    static DataSet readData(String filename) {
        DataSet data = new DataSet();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filename));
        } catch (FileNotFoundException exception) {
            System.out.println("Error: could not find file");
            System.exit(1);
        }
        String line;
        scanner.nextLine(); // Skip first line
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.equals("@data")) {
                break;
            }
            data.addAttribute(line);
        }
        while (scanner.hasNextLine()) {
            data.addInstance(scanner.nextLine());
        }
        return data;
    }

}
