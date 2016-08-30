package decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tree {

    public TreeNode buildDecisionTree(ArrayList<Instance> instances, ArrayList<Attribute> attributes,
                                      ArrayList<Instance> parentInstances) {
        if (instances.isEmpty())
            return new TreeNode(getLabel(parentInstances), true);

        // Returns null if no more attributes can be used
        SplitAttribute.SplitValue bestSplitValue = SplitAttribute.getBestAttribute(instances, attributes);
        if (bestSplitValue == null)
            return new TreeNode(getLabel(instances), true);

        TreeNode root = new TreeNode(bestSplitValue.getAttribute().name, false);

        // Partition instances based on best attribute
        HashMap<String, ArrayList<Instance>> subsets = SplitAttribute.getSplitedInstances(instances, bestSplitValue);

        // Iterate through possible values of bestAttribute
        for(String key: subsets.keySet()) {
            // Copy attributes list and remove bestAttribute from copy
            ArrayList<Attribute> remainingAttributes = new ArrayList<Attribute>();
            remainingAttributes.addAll(attributes);
            remainingAttributes.remove(bestSplitValue);
            // Add child to subtree of root
            TreeNode child = buildDecisionTree(subsets.get(key), remainingAttributes, instances);
            child.commonValue = key;
            child.remainingAttribute = remainingAttributes;
            root.addChild(child);

        }

        return root;
    }

    private String getLabel(List<Instance> instances) {
        int countOne = 0;
        for (Instance instance : instances)
            if (instance.label.equals("1"))
                countOne++;
        if (countOne >= instances.size()/2)
            return "1";
        else return "0";
    }

}
