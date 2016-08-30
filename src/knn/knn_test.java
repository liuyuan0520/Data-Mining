
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class knn_test {
    public static void main(String[] args) throws Exception {
        String FILE_PATH= "trainProdSelection.arff";        
        double sum = 0;
        double count = 0;
        for (int i = 0; i < 100 ; i++) {
        double num = getResult(FILE_PATH);
        sum = sum + num;
        count++;
        }
        System.out.println((double)(sum)/count);
    }
    private static DataSet readData(String filename) {
            DataSet data = new DataSet();
            Scanner scanner = null;
            try {
                scanner = new Scanner(new File(filename));
            } catch (FileNotFoundException exception) {
                System.out.println("Error: could not find file");
                System.exit(1);
            }
            String line;
            scanner.nextLine(); // Skip first line
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.length() == 0){
                    continue;
                }
                if (line.equals("@data")) {
                    
                    break;
                }
                data.addAttribute(line);
            }
            while (scanner.hasNextLine()) {
                data.addInstance(scanner.nextLine());
            }
            System.out.println(data.instances.toString());
            return data;
        
    }
    public static String getMode(HashMap<String, Double> gMode) {
        String maxMode = null;
        double maxCount = 0;
        for(String d : gMode.keySet()) {
            double tCount = gMode.get(d);
            if (tCount > maxCount) {
                maxCount = tCount;
                maxMode = d;
            }
        }
        return maxMode;
    }
    public static double getResult(String FILE_PATH, double[] array) throws IOException, FileNotFoundException {
        FileReader in_train = new FileReader(FILE_PATH);
        FileReader in_test = new FileReader("/testProdSelection.arff");        
      
        BufferedReader br_train = new BufferedReader(in_train);
        BufferedReader br_test = new BufferedReader(in_test);
        
        DataSet dataSet = readData(FILE_PATH);
        ArrayList<Attribute> attributeList = dataSet.attributes;
        int result;
        /*
         * load train data
         */
        String line;
        ArrayList<String> newList = new ArrayList<String>();
        while ((line = br_train.readLine()) != null) {
            if  (!line.startsWith("@") && !(line.length()==0)) {
               //   System.out.println(line);
                newList.add(line);
            }
        }
        br_train.close();
        /*
        * load test data
        */
        String line2;
        ArrayList<String> newList2 = new ArrayList<String>();
        while ((line2 = br_test.readLine()) != null) {
            if (!line2.startsWith("@") && !(line2.length()==0)) {
                // System.out.println(line);
                newList2.add(line2);
               // System.out.println(Arrays.toString(tokens));
            }
        }
        br_test.close();
        
        int K = 3;
        int nAttrib = 6;  
        int TrainSize = newList.size();    // Number of Train Data

        int ntestData = newList2.size();   // Number of Test Data
        // Read Train Data with nAttribe Features
        Collections.shuffle(newList);
        
        int nTrainData = (int) (TrainSize*0.9); // 90% train
        ntestData = TrainSize - nTrainData; // 10% test
        ArrayList<DataObject> data = new ArrayList<DataObject>();
        
        
        for(int i = 0; i < nTrainData; i++) {
            String temp = newList.get(i).toString();
            Element element = new Element(temp);
            DataObject ob_train = new DataObject(element);
            data.add(ob_train);
            
            String tempStr = dataSet.instances.get(i).toString();
            //  System.out.println(tempStr);

        }
        System.out.println(data.size());
        //   Read Test Data 
        ArrayList<DataObject> testData = new ArrayList<DataObject>();
        //   ntestData = 1;
        for(int i = nTrainData; i < TrainSize; i++) {
            String temp = newList.get(i).toString();
            Element element = new Element(temp);
            DataObject ob_test = new DataObject(element);
            ob_test.trueLabel = element.getLabel();
            testData.add(ob_test);
        }
        // Set Normalization
        ArrayList<DataObject> allData = new ArrayList<DataObject>();
        allData.addAll(data);
        allData.addAll(testData);
        int maxVacation = 0;
        int maxCredit = 0;
        double maxSalary = 0;
        double maxProperty = 0;
        int minVacation = 0;
        int minCredit = 0;
        double minSalary = 0;
        double minProperty = 0;
        for (int i = 0; i< allData.size(); i++) {
            if (allData.get(i).getElement().getVacation() > maxVacation) {
                maxVacation = allData.get(i).getElement().getVacation();
            }
            if (allData.get(i).getElement().getCredit() > maxCredit) {
                maxCredit = allData.get(i).getElement().getCredit();
            }
            if (allData.get(i).getElement().getSalary() > maxSalary) {
                maxSalary = allData.get(i).getElement().getSalary();
            }
            if (allData.get(i).getElement().getProperty() > maxProperty) {
                maxProperty = allData.get(i).getElement().getProperty();
            }
            if (allData.get(i).getElement().getVacation() < minVacation) {
                minVacation = allData.get(i).getElement().getVacation();
            }
            if (allData.get(i).getElement().getCredit() < minCredit) {
                minCredit = allData.get(i).getElement().getCredit();
            }
            if (allData.get(i).getElement().getSalary() < minSalary) {
                minSalary = allData.get(i).getElement().getSalary();
            }
            if (allData.get(i).getElement().getProperty() < minProperty) {
                minProperty = allData.get(i).getElement().getProperty();
            }
        }
        for (int i = 0; i< allData.size(); i++) {
            allData.get(i).getElement().setNormVacation((double)(allData.get(i).getElement().getVacation() - minVacation)/(double) (maxVacation - minVacation));
            allData.get(i).getElement().setNormCredit((double)(allData.get(i).getElement().getCredit() - minCredit)/(double) (maxCredit - minCredit));
            allData.get(i).getElement().setNormSalary((double)(allData.get(i).getElement().getSalary() - minSalary)/(double) (maxSalary - minSalary));            
            allData.get(i).getElement().setNormProperty((double)(allData.get(i).getElement().getProperty() - minProperty)/(double) (maxProperty - minProperty));            
        }   
        int count = 0;
        // Compute for the Distance of all the Test Data
        for (int z = 0; z < ntestData; z++) {
            for(int i = 0; i < nTrainData; i++) {
                Element train = data.get(i).getElement();
                Element test = testData.get(z).getElement();
                double dist =  0;
                if (train.getType() != test.getType()) {
                    dist += array[0]*1;
                    
                }
                if (train.getStyle() != test.getStyle()) {
                    dist += array[1]*1;
                }
                dist += array[2]*Math.pow(train.getNormVacation() - test.getNormVacation(),2);
                dist += array[3]*Math.pow(train.getNormCredit() - test.getNormCredit(),2);
                dist += array[4]*Math.pow(train.getNormSalary() - test.getNormSalary(),2);
                dist += array[5]*Math.pow(train.getNormProperty() - test.getNormProperty(),2);
                if (dist == 0) {
                    dist = 1000000;
                    
                } else {
                    dist = 1/Math.sqrt(dist);
                }
                data.get(i).dist = dist;
            }
            Collections.sort(data);
            //  System.out.println(data);
            
            //  Rank all the K neighbors
            HashMap<String, Double> gMode = new HashMap<String, Double>();
            double val = data.get(0).dist;
            gMode.put(data.get(0).getElement().getLabel(), data.get(0).dist);
            for(int i = 1, rank = 1; i < nTrainData && rank < K; i++) {
                if (val >= data.get(i).dist) {
                    rank++;
                    String temp_label = data.get(i).getElement().getLabel();
                    // System.out.println(gMode.get(data.get(i).getElement().getLabel()));
                    if (gMode.containsKey(temp_label)) {
                        double temp_dist = gMode.get(data.get(i).getElement().getLabel());
                        gMode.put(temp_label, temp_dist + data.get(i).dist);
                    } else{
                        gMode.put(temp_label, data.get(i).dist);
                    }
                }
            }
            // Get K for test data
            testData.get(z).getElement().setLabel(getMode(gMode));        // Find the mode of the neighbors
            System.out.println("The test data" + z +" is classified as: "+testData.get(z).getElement().getLabel());
            System.out.println("The true label of test data" + z +" is " + testData.get(z).trueLabel());
           
            if (testData.get(z).trueLabel().equals(testData.get(z).getElement().getLabel())){
                count++;
            }
        }
        System.out.println("The accurency: "+ (double) count/ntestData);
        return (double) count/ntestData;
    }
 
}
