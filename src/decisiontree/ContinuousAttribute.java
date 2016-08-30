package decisiontree;

public class ContinuousAttribute extends Attribute{
    private double splitValue;

    public ContinuousAttribute(String name) {
        super(name);
    }

    public double getSplitValue() {
        return splitValue;
    }

    public void setSplitValue(double splitValue) {
        this.splitValue = splitValue;
    }
}
