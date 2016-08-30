package decisiontree;


public class Attribute implements Comparable<Attribute> {
    private String name;
    private double infoGain;

    public Attribute(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getInfoGain() {
        return infoGain;
    }

    public void setInfoGain(double infoGain) {
        this.infoGain = infoGain;
    }

    @Override
    public int compareTo(Attribute o) {
        if (o.getInfoGain() - this.getInfoGain() > 0) {
            return 1;
        } else if (o.getInfoGain() - this.getInfoGain() < 0) {
            return -1;
        }
        return 0;    }
}
