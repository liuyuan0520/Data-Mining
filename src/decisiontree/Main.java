package decisiontree;

import java.util.ArrayList;

public class Main {
    public static void main(String[] arg) {
        // train or build tree
        DataSet dataSet = DataSet.readData(SplitAttribute.FILE_PATH);
        ArrayList<Attribute> attributes = dataSet.attributes;
        ArrayList<Instance> instances = dataSet.instances;

        Tree tree = new Tree();
        TreeNode root = tree.buildDecisionTree(instances, attributes, instances);
        root.print(0);

        // test
        String testPath = "";
        DataSet test = DataSet.readData(testPath);
        ArrayList<Instance> testInstances = test.instances;

        System.out.println("\n\nListed as: <predicted class> <actual class>");
        int hit = 0;
        for (Instance instance : testInstances) {
            String predicted = classify(instance, root);
            System.out.println(predicted + " " + instance.label);
            if (predicted.equals(instance.label))
                hit++;
        }

        System.out.println("\nCorrect: " + hit);
        System.out.println("Total: " + instances.size());
        double hitRate = (hit * 1.0) / (instances.size() * 1.0);
        System.out.println("Hit Rate: " + (hitRate * 100) + "%");
    }

    // Classifies an instance
    private static String classify(Instance instance, TreeNode node) {
        if(node.isLeaf)
            return node.label;
        ArrayList<TreeNode> children = node.children;
        for (TreeNode c : children) {

            String value = instance.attributeMap.get(c.attributeName);
            if(value.equals(c.commonValue))
                return classify(instance, c);
        }
        return null;
    }
}
