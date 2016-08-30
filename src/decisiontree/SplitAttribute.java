package decisiontree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static decisiontree.DataSet.readData;

/**
 * @author Siqi Wang siqiw1 on 4/3/16.
 */
public class SplitAttribute {
    static final String FILE_PATH = "trainProdIntro.binary.arff";
    private static DataSet dataSet = readData(FILE_PATH);
    private static ArrayList<Attribute> attributeList = dataSet.attributes;
    private static ArrayList<String> labelList = dataSet.labels;


    /**
     * Get the entropy of one attribute value.
     *
     * @param labelCounts Hashmap of label as key, count of data with the label as value
     * @return
     */
    private static double calculateE(HashMap<String, Integer> labelCounts) {
        int total = 0;
        for (Integer integer : labelCounts.values()) {
            total += integer;
        }
        double entropy = Integer.MAX_VALUE;
        for (Integer labelCount : labelCounts.values()) {
            double p = labelCount / total;
            double entropySv = -p * (Math.log(p) / Math.log(2));
            entropy += entropySv;
        }
        return entropy;
    }

    /**
     * Get Splitting object with information gain for attribute with discrete values.
     *
     * @param instances the list of remaining instances
     * @param attribute the attribute for calculating information gain
     * @return SplitValue object with splitting attribute and information gain (split value is -1 for discrete
     * attributes).
     */
    private static DiscreteAttribute getDiscreteInfoGain(ArrayList<Instance> instances, DiscreteAttribute attribute) {
        //Count of instance with label 0 for each attribute value.
        Map<String, Integer> label0CountMap = new HashMap<>();
        //Count of instance with label 1 for each attribute value.
        Map<String, Integer> label1CountMap = new HashMap<>();

        Map<String, HashMap<String, Integer>> attributeLabelCount = new HashMap<>();
        // Initialize label count hashmaps
        for (int i = 0; i < labelList.size(); i++) {
            // Key: label value; Value: count of data with this label
            HashMap<String, Integer> labelCountMap = new HashMap<>();
            for (String label : labelList) {
                labelCountMap.put(label,0);
            }
        }

        for (String attriValue : attribute.getValues()) {
            for (Instance instance : instances) {
                String label = instance.getLabel();

            }
        }


        int totalLabel0 = 0;
        int totalLabel1 = 0;

        for (Instance instance : instances) {
            if (instance.label.equals("0")) {
                ++totalLabel0;
                String attributeValue = instance.attributeMap.get(attribute);
                if (label0CountMap.containsKey(attributeValue)) {
                    label0CountMap.put(attributeValue, label0CountMap.get(attributeValue) + 1);
                } else {
                    label0CountMap.put(attributeValue, 1);
                }
                // else if to avoid null label.
            } else if (instance.label.equals("1")) {
                ++totalLabel1;
                String attributeValue = instance.attributeMap.get(attribute);
                if (label1CountMap.containsKey(attributeValue)) {
                    label1CountMap.put(attributeValue, label1CountMap.get(attributeValue) + 1);
                } else {
                    label1CountMap.put(attributeValue, 1);
                }
            }
        }
        // Gain(S,A) = Entropy(S) - Sigma (Sv/S)Entropy(Sv)
        int accumEntropySi = 0;

        for (Attribute theAttribute : attributeList) {
            int sLabel0 = label0CountMap.get(theAttribute);
            int sLabel1 = label1CountMap.get(theAttribute);
            accumEntropySi += (sLabel0 + sLabel1) / (totalLabel0 + totalLabel1) * calculateE(sLabel0, sLabel1);

        }
        if (accumEntropySi == 0) {
            return null;
        }
        double entropyS = calculateE(totalLabel0, totalLabel1);
        DiscreteAttribute newAttribute = new DiscreteAttribute(attribute.getName());
        newAttribute.setInfoGain(entropyS - accumEntropySi);
        return newAttribute;
    }

    /**
     * Get the Splitting object with information gain for attribute with continuous values.
     *
     * @param instances
     * @param attribute
     * @return the SplitValue object with highest information gain.
     */
    private static ContinuousAttribute getContinuousInfoGain(ArrayList<Instance> instances, Attribute attribute) {
        ArrayList<ContinuousAttribute> attributeList = new ArrayList<>();
        for (Instance instance : instances) {
            // Use below value as split value.
            double value = Double.valueOf(instance.attributeMap.get(attribute));

            for (Instance theInstance : instances) {
                double theValue = Double.valueOf(theInstance.attributeMap.get(attribute));

                // Change attribute values from continuous to discrete.
                if (theValue <= value) {
                    instance.attributeMap.put(attribute, "0");
                } else {
                    instance.attributeMap.put(attribute, "1");
                }
            }
            for (int i = 0; i < instances.size(); i++) {
                DiscreteAttribute aTemp = getDiscreteInfoGain(instances, attribute);
                ContinuousAttribute a = new ContinuousAttribute(attribute.getName());
                a.setInfoGain(aTemp.getInfoGain());
                a.setSplitValue(value);

                attributeList.add(a);
            }
        }
        Collections.sort(attributeList);
        return attributeList.get(0);
    }


    /**
     * Get the current best attribute to split tree.
     *
     * @param instances
     * @param attributes
     * @return
     */
    static Attribute getBestAttribute(ArrayList<Instance> instances, ArrayList<Attribute> attributes) {
        PriorityQueue<Attribute> attributeQueue = new PriorityQueue<>();

        for (Attribute a : attributes) {
            if (a instanceof DiscreteAttribute) {
                attributeQueue.offer(getDiscreteInfoGain(instances, a));
            } else {
                attributeQueue.offer(getContinuousInfoGain(instances, a));
            }
        }
        return attributeQueue.peek();
    }

    /**
     * Get hashmap for splitted instances lists, use the attribute value as key, instances of the same kind as value.
     *
     * @param instances
     * @param attribute
     * @return
     */
    static HashMap<String, ArrayList<Instance>> getSplitedInstances(ArrayList<Instance> instances, Attribute attribute) {
        HashMap<String, ArrayList<Instance>> splitedInstanceMap = new HashMap<>();

        // For discrete attribute, split instances to groups based on number of attribute values.
        if (attribute instanceof DiscreteAttribute) {
            for (String value : ((DiscreteAttribute) attribute).getValues()) {
                splitedInstanceMap.put(value, new ArrayList<Instance>());
            }
            for (Instance instance : instances) {
                String value = instance.attributeMap.get(attribute);
                ArrayList<Instance> instanceList = splitedInstanceMap.get(value);
                instanceList.add(instance);
            }
            return splitedInstanceMap;

            // For continuous attribute, split instances to two groups based on split threshold.
        } else {
            double value = ((ContinuousAttribute) attribute).getSplitValue();
            String splitValue = String.valueOf(value);
            splitedInstanceMap.put("<=" + splitValue, new ArrayList<Instance>());
            splitedInstanceMap.put(">" + splitValue, new ArrayList<Instance>());

            for (Instance instance : instances) {
                double splitVal = Double.valueOf(instance.attributeMap.get(attribute));
                if (splitVal <= value) {
                    ArrayList<Instance> instanceList = splitedInstanceMap.get("<=" + splitValue);
                    instanceList.add(instance);
                } else {
                    ArrayList<Instance> instanceList = splitedInstanceMap.get(">" + splitValue);
                    instanceList.add(instance);
                }
            }
            return splitedInstanceMap;
        }
    }
}
