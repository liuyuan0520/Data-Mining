

public class Element{
        private Type type;
        private Style style;
        private int Vacation;
        private int credit;
        private double salary;
        private double property;
        private double normVacation;
        private double normCredit;
        private double normSalary;
        private double normProperty;
        private Label label;
        

        public Element(String str) {
           // String[] params = str.split(", |\\[");
            String[] params = str.split(",");
            if (params.length == 7) {
                setType(params[0]);
                setStyle(params[1]);
                setVacation(Integer.parseInt(params[2]));
                setCredit(Integer.parseInt(params[3]));
                setSalary(Double.parseDouble(params[4]));
                setProperty(Double.parseDouble(params[5]));
                setLabel(params[6]);
            } else {
                setType(params[0]);
                setStyle(params[1]);
                setVacation(Integer.parseInt(params[2]));
                setCredit(Integer.parseInt(params[3]));
                setSalary(Double.parseDouble(params[4]));
                setProperty(Double.parseDouble(params[5]));
                setLabel("C1");
            }
        }

        public Type getType() {
            return type;
        }

        public Style getStyle() {
            return style;
        }

        public int getVacation() {
            return Vacation;
        }

        public int getCredit() {
            return credit;
        }

        public double getSalary() {
            return salary;
        }

        public double getProperty() {
            return property;
        }
        public double getNormVacation() {
            return normVacation;
        }
        public double getNormCredit() {
            return normCredit;
        }
        public double getNormSalary() {
            return normSalary;
        }
        public double getNormProperty() {
            return normProperty;
        }

        public String getLabel() {
            return label.toString();
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
            case "doctor":
                type = Type.DOC;
                break;
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
            Vacation = vacation;
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
        public void setNormVacation(double normVacation) {
            this.normVacation = normVacation;
        }
        public void setNormCredit(double normCredit) {
            this.normCredit = normCredit;
        }
        public void setNormSalary(double normSalary) {
            this.normSalary = normSalary;
        }
        public void setNormProperty(double normProperty) {
            this.normProperty = normProperty;
        }
        public void setLabel(String key) {
            switch (key) {
            case "C1":
                label = Label.C1;
                break;
            case "C2":
                label = Label.C2;
                break;
            case "C3":
                label = Label.C3;
                break;
            case "C4":
                label = Label.C4;
                break;
            case "C5":
                label = Label.C5;
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

    enum Label {
        C1, C2, C3, C4, C5;
    }
}
 
  
