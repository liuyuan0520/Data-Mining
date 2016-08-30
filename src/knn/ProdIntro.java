package src;

import java.util.List;

public class ProdIntro {
    private Type type;
    private Customer customer;
    private double fee;
    private double budget;
    private Size size;
    private Promotion promotion;
    private int rate;
    private int period;
    private int label;

    static double fMax;
    static double fMin;
    static double bMax;
    static double bMin;
    static int rMax;
    static int rMin;
    static int pMax;
    static int pMin;

    public ProdIntro(String str) {
        String[] params = str.split(",");
        setType(params[0]);
        setCustomer(params[1]);
        setFee(Double.parseDouble(params[2]));
        setBudget(Double.parseDouble(params[3]));
        setSize(params[4]);
        setPromotion(params[5]);
        setRate(Integer.parseInt(params[6]));
        setPeriod(Integer.parseInt(params[7]));
        setLabel(Integer.parseInt(params[8]));
    }

    public ProdIntro() {
    }
    public ProdIntro(ProdIntro from) {
        this.type = from.type;
        this.customer = from.customer;
        this.fee = from.fee;
        this.budget = from.budget;
        this.size = from.size;
        this.promotion = from.promotion;
        this.period = from.period;
        this.rate = from.rate;
        this.label = from.label;
    }

    /**
     * Pass a list of training prodintro to reset the minmax.
     * 
     * @param list
     */
    public static void resetMinMax(List<ProdIntro> list) {
        fMin = bMin = Double.MAX_VALUE;
        rMin = pMin = Integer.MAX_VALUE;
        fMax = bMax = Double.MIN_VALUE;
        rMax = pMax = Integer.MIN_VALUE;
        for (ProdIntro p : list) {
            fMax = Math.max(fMax, p.getFee());
            bMax = Math.max(bMax, p.getBudget());
            rMax = Math.max(rMax, p.getRate());
            pMax = Math.max(pMax, p.getPeriod());
            fMin = Math.min(fMin, p.getFee());
            bMin = Math.min(bMin, p.getBudget());
            rMin = Math.min(rMin, p.getRate());
            pMin = Math.min(pMin, p.getPeriod());
        }
    }

    public Type getType() {
        return type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getFee() {
        return fee;
    }

    public double getNFee(){
        return normalize(fee, fMin, fMax);
    }

    public double getBudget() {
        return budget;
    }
    
    public double getNBudget(){
        return normalize(budget, bMin, bMax);
    }
    
    public Size getSize() {
        return size;
    }
    
    public Promotion getPromotion() {
        return promotion;
    }


    public int getRate() {
        return rate;
    }

    public double getNRate(){
        return normalize(rate, rMin, rMax);
    }
    
    public int getPeriod() {
        return period;
    }
    
    public double getNPeriod(){
        return normalize(period, pMin, pMax);
    }

    public int getLabel() {
        return label;
    }

    public void setType(String key) {
        switch (key) {
        case "Fund":
            type = Type.FUND;
            break;
        case "Loan":
            type = Type.LOAN;
            break;
        case "CD":
            type = Type.CD;
            break;
        case "Bank_Account":
            type = Type.BA;
        case "Mortgage":
            type = Type.MORT;
        default:
            break;
        }
    }

    public void setCustomer(String key) {
        switch (key) {
        case "Student":
            customer = Customer.STU;
            break;
        case "Business":
            customer = Customer.BUS;
            break;
        case "Other":
            customer = Customer.OTH;
            break;
        case "Doctor":
            customer = Customer.DOC;
            break;
        case "Professional":
            customer = Customer.PRO;
            break;
        default:
            break;
        }
    }
    
    public void setSize(String key) {
        switch (key) {
        case "Small":
            size = Size.SM;
            break;
        case "Large":
            size = Size.LG;
            break;
        case "Medium":
            size = Size.MD;
            break;
        default:
            break;
        }
    }
    
    public void setPromotion(String key) {
        switch (key) {
        case "Web&Email":
            promotion = Promotion.WEBEMAIL;
            break;
        case "Full":
            promotion = Promotion.FULL;
            break;
        case "Web":
            promotion = Promotion.WEB;
            break;
        case "None":
            promotion = Promotion.NONE;
            break;
        default:
            break;
        }
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
    
    public void setLabel(int key) {
        this.label = key;
    }
    
    enum Type {
        LOAN, BA, CD, MORT, FUND;
    }
    
    enum Customer {
        BUS, PRO, STU, DOC, OTH;
    }

    enum Size {
        SM, MD, LG;
    }
    
    enum Promotion {
        FULL, WEBEMAIL, WEB, NONE;
    }

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
    
    private double normalize(int val, int min, int max) {
        return (val - min) / (max - min);
    }
}


