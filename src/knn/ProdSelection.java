import java.util.List;

public class ProdSelection {
    private Type type;
    private Style style;
    private int vacation;
    private int credit;
    private double salary;
    private double property;
    private int label;

    static int vMax;
    static int vMin;
    static int cMax;
    static int cMin;
    static double sMax;
    static double sMin;
    static double pMax;
    static double pMin;

    public ProdSelection(String str) {
        String[] params = str.split(",");
        setType(params[0]);
        setStyle(params[1]);
        setVacation(Integer.parseInt(params[2]));
        setCredit(Integer.parseInt(params[3]));
        setSalary(Double.parseDouble(params[4]));
        setProperty(Double.parseDouble(params[5]));
        setLabel(params[6]);
    }

    public ProdSelection() {
    }
    public ProdSelection(ProdSelection from) {
        this.type = from.type;
        this.style = from.style;
        this.vacation = from.vacation;
        this.credit = from.credit;
        this.salary = from.salary;
        this.property = from.property;
        this.label = from.label;
    }

    /**
     * Pass a list of training prodselection to reset the minmax.
     * 
     * @param list
     */
    public static void resetMinMax(List<ProdSelection> list) {
        vMin = cMin = Integer.MAX_VALUE;
        sMin = pMin = Double.MAX_VALUE;
        vMax = cMax = Integer.MIN_VALUE;
        sMax = pMax = Double.MIN_VALUE;
        for (ProdSelection p : list) {
            vMax = Math.max(vMax, p.getVacation());
            cMax = Math.max(cMax, p.getCredit());
            sMax = Math.max(sMax, p.getSalary());
            pMax = Math.max(pMax, p.getProperty());
            vMin = Math.min(vMin, p.getVacation());
            cMin = Math.min(cMin, p.getCredit());
            sMin = Math.min(sMin, p.getSalary());
            pMin = Math.min(pMin, p.getProperty());
        }
    }

    public Type getType() {
        return type;
    }

    public Style getStyle() {
        return style;
    }

    public int getVacation() {
        return vacation;
    }

    public double getNVacation(){
        return normalize(vacation, vMin, vMax);
    }

    public int getCredit() {
        return credit;
    }
    
    public double getNCredit(){
        return normalize(credit, cMin, cMax);
    }

    public double getSalary() {
        return salary;
    }
    
    public double getNSalary(){
        return normalize(salary, sMin, sMax);
    }

    public double getProperty() {
        return property;
    }

    public double getNProperty(){
        return normalize(property, pMin, pMax);
    }
    
    public int getLabel() {
        return label;
    }

    public void setType(String key) {
        switch (key) {
        case "librarian":
            type = Type.LIB;
            break;
        case "student":
            type = Type.STU;
            break;
        case "professor":
            type = Type.PRO;
            break;
        case "engineer":
            type = Type.ENG;
        default:
            break;
        }
    }

    public void setStyle(String key) {
        switch (key) {
        case "spend<<saving":
            style = Style.SAV2;
            break;
        case "spend<saving":
            style = Style.SAV;
            break;
        case "spend>saving":
            style = Style.SPD;
            break;
        case "spend>>saving":
            style = Style.SPD2;
        default:
            break;
        }
    }

    public void setVacation(int vacation) {
        this.vacation = vacation;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setProperty(double property) {
        this.property = property;
    }
    public void setLabel(int key) {
        this.label = key;
    }
    public void setLabel(String key) {
        switch (key) {
        case "C1":
            label = 1;
            break;
        case "C2":
            label = 2;
            break;
        case "C3":
            label = 3;
            break;
        case "C4":
            label = 4;
            break;
        case "C5":
            label = 5;
            break;
        default:
            break;
        }
    }
    
    enum Type {
        LIB, STU, ENG, PRO, DOC;
    }

    enum Style {
        SAV2, SAV, SPD, SPD2;
    }

//    enum Label {
//        C1, C2, C3, C4, C5;
//    }
    /**
     * normalize given val using min and max.
     * @param val
     * @param min
     * @param max
     * @return
     */
    private double normalize(double val, double min, double max) {
        return (val - min) / (max - min);
    }
}