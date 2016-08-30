package decisiontree;

import java.util.ArrayList;


public class TreeNode {
    private boolean isLeaf;
    // @attribute: Service_type, Customer, Monthly_fee, Advertisement_budget
    // Size, Promotion, Interest_rate, Period
    private Attribute splitAttribute;
    private String attributeName;
    // @attribute Label {0,1}
    private String label;
    private ArrayList<TreeNode> children;

    // is less than(numeric)
    private boolean ilt;

    ArrayList<Instance> instanceList;
    ArrayList<Attribute> remainingAttribute;

    public TreeNode(String attributeOrLabel, boolean isLeaf) {
        this.isLeaf = isLeaf;
        if (isLeaf) {
            children = null;
            label = attributeOrLabel;
        } else {
            children = new ArrayList<>();
            attributeName = attributeOrLabel;
        }
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    // Prints the entire subtree  of the node
    public void print(int depth) {
        if (isLeaf) {
            System.out.print(": " + label);
        } else {
            for (TreeNode child : children) {
                System.out.print("\n");
                for (int i = 0; i < depth; i++)
                    System.out.print("|\t");
                System.out.print(attributeName);
                System.out.print(commonValue);
                child.print(depth + 1);
            }
        }
    }

    public SplitAttribute.SplitValue getSplitValue() {
        return splitValue;
    }

    public void setSplitValue(SplitAttribute.SplitValue splitValue) {
        this.splitValue = splitValue;
    }
}
