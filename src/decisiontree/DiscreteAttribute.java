package decisiontree;

import java.util.ArrayList;

public class DiscreteAttribute extends Attribute{
    private ArrayList<String> values;

    public DiscreteAttribute(String name) {
        super(name);
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
}
