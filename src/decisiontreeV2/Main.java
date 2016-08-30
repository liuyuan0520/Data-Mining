package decisiontreeV2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

// Main class for decision tree learner
public class Main {

    // Extracts data set from ARFF file
    @SuppressWarnings("resource")
    private static DataSet read(String filename) {
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
            if (line.equals("@data"))
                break;
            if (line != null && !line.equals(""))
                data.addAttribute(line);
        }
        data.attributes.remove(data.attributes.size() - 1);
        while (scanner.hasNextLine())
            data.addInstance(scanner.nextLine());
        return data;
    }

    public static void main(String[] args) {
        String train = "trainProdIntro.binary.arff";
        String test = "testProdIntro.binary.arff";
        DataSet trainingSet = read(train);

        crossValidate(trainingSet);
//        DataSet testingSet = read(test);
//        int stopValue = Integer.parseInt("1");

//        DecisionTree ID3 = new DecisionTree(stopValue);
//        ID3.train(trainingSet);
//        ID3.test(trainingSet.instances);
    }

    private static void crossValidate(DataSet dataSet) {
	  //Cut samples to k sets
        List<List<Instance>> kSets = new ArrayList<>();
        for (int j = 0; j < 10; j++)
            kSets.add(new ArrayList<Instance>());

        Iterator<Instance> iterator = dataSet.instances.iterator();
        while (iterator.hasNext()) {
            for (int j = 0; j < 10 && iterator.hasNext(); j++) {
                kSets.get(j).add(iterator.next());
            }
        }

        double average = 0;
	    for (int i = 0; i < 10; i++) {
            //k-1 sets as training, ith set as testing
            ArrayList<Instance> training = new ArrayList<>();
            ArrayList<Instance> testing = new ArrayList<>();
            testing.addAll(kSets.get(i));
            for (int j = 0; j < 10; j++) {
                if (j != i) {
                    training.addAll(kSets.get(j));
                }
            }

            //Train and predict
            DecisionTree root = new DecisionTree(1);
            DataSet train = new DataSet();
            train.attributes = dataSet.attributes;
            train.instances = training;
            root.train(train);
            average += root.test(testing);
        }
	    average /= 10;
        System.out.println("average accuracy:" + average * 100 +"%");

	}
}
