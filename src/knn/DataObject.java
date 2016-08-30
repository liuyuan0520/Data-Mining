class DataObject implements Comparable<DataObject> {
        public Element element;
        public double dist;
        public String trueLabel; 
        
        public DataObject(Element element) {
            this.element = element;
            this.dist = 0;
            this.trueLabel = null;
        }
        public String trueLabel() {
            return trueLabel;
        }
        public Element getElement() {
            return element;
        }
        
        public int compareTo(DataObject ob) {
            return Double.compare(ob.dist, this.dist);
        }
 
        public String toString() {
            return "DataObject = " + element.getType() +" " + element.getStyle() +" "
            + element.getVacation() +" " +element.getCredit() + " " + 
            element.getSalary() + " "+ element.getProperty() + " " + element.getLabel()
            + " "+ dist;
            
        }
}