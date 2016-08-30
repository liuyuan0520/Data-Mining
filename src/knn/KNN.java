import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class KNN {
    private static final int LABLENUM = 5;
    private static final String TRAINPATH = "trainProdSelection.arff";
    private static final String TESTPATH = "testProdSelection.arff";
    private static int k = 3;
    private static final double[] DEFAULTWEIGHT = { 1, 1, 1, 1, 1, 1 };
    private List<ProdSelection> trainData = new ArrayList<ProdSelection>();
    private List<ProdSelection> testData = new ArrayList<ProdSelection>();

    ///////////////////////////////////////////////////////
    ////////////////// CONSTRUCTORS/////////////////////////
    ///////////////////////////////////////////////////////
    /**
     * Default Constructor, K = 3
     */
    public KNN() {
        this(3);
    }

    /**
     * Use customized K
     * 
     * @param k
     */
    public KNN(int k) {
        this.k = k;
    }

    ///////////////////////////////////////////////////////
    ////////////////// Public Functions////////////////////
    ///////////////////////////////////////////////////////

    /**
     * Predict using default testing set path
     * 
     * @return
     */
    public Result predict() {
        return predict(TESTPATH);
    }

    /**
     * Use default weight and training set to predict the given test set
     * 
     * @param path
     *            test set file path
     * @return
     */
    public Result predict(String path) {
        try {
            // load test data
            loadData(path, false);
            loadData(TRAINPATH, true);
            return predict(trainData, testData, DEFAULTWEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Predict the test result using given training set and weight
     * 
     * @param train
     *            training set
     * @param test
     *            testing set
     * @param w
     *            weight
     * @return Result object
     */
    public Result predict(List<ProdSelection> train, List<ProdSelection> test,
            double[] w) {
        Result result = new Result();
        List<Integer> resultSet = new ArrayList<>();
        // for each element in test
        for (ProdSelection te : test) {
            // for each element in train
            PriorityQueue<Sim> queue = new PriorityQueue<>();
            for (ProdSelection tr : train) {
                // calculate the similarity of te and tr
                queue.offer(new Sim(calculateSim(te, tr, w), tr.getLabel()));
            }
            resultSet.add(findWinner(queue));
        }
        result.testSet = test;
        result.resultSet = resultSet;
        return result;
    }

    /**
     * Validate the predict accuracy using result object
     * 
     * @param result
     */
    public double validate(Result result) {
        double hit = 0;
        for (int i = 0; i < result.testSet.size(); i++) {
            if (result.testSet.get(i).getLabel() == result.resultSet.get(i)) {
                hit++;
            }
        }
        result.accuracy = hit / result.resultSet.size();
        return result.accuracy;
    }

    public double crossValidation(int fold) {
        return crossValidation(fold, DEFAULTWEIGHT);
    }

    /**
     * Take a weight vecotr and conduct 10 fold validation, return an average
     * accuracy.
     * 
     * @param w
     *            weight vector, size = 6
     * @return average accuracy
     */
    public double crossValidation(double[] w) {
        return crossValidation(10, w);
    }
    
    /**
     * Conduct x fold cross validation with weight w
     * @param fold
     * @param w
     * @return
     */
    public double crossValidation(int fold, double[] w) {
        try {
            loadData(TRAINPATH, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int size = trainData.size();
        if(fold > size) fold = size;
        List<ProdSelection> shuffled = cloneList(trainData);
        Collections.shuffle(shuffled);
        int testSize = size / fold;
        int mod = size % fold;
        int cnt = 0;
        int num = 1;
        int start = 0;
        double err = 0;
        while (start < size) {
            int end = start + testSize + (cnt++ < mod ? 1 : 0);
            List<ProdSelection> test = cloneList(
                    shuffled.subList(start, Math.min(end, size)));
            List<ProdSelection> train = cloneList(shuffled.subList(0, start));
            train.addAll(cloneList(shuffled.subList(end, size)));
            double currErr = validate(predict(train, test, w));
            System.out.println(
                    String.format("Round %d: %.2f%%", num++, currErr * 100));
            err += currErr;
            start = end;
        }
        System.out.println(String.format("\nResult: %.2f%%", err/fold * 100));
        return err / fold;
    }

    ///////////////////////////////////////////////////////
    ////////////////// PRIVATE FUNCTIONS///////////////////
    ///////////////////////////////////////////////////////
    /**
     * assign a label for given candidate queue.
     * 
     * @param queue
     * @return
     */
    private int findWinner(PriorityQueue<Sim> queue) {
        // find the winner
        int winner = 0;
        int[] counter = new int[LABLENUM + 1];
        int i = 0;
        while (i++ < k) {
            int curr = queue.poll().label;
            counter[curr]++;
            if (counter[curr] > counter[winner]) {
                winner = curr;
            }
            if (counter[curr] > (k / 2))
                break;
        }
        return winner;
    }

    /**
     * deep copy a List of ProdSelection.
     * @param list
     * @return
     */
    private List<ProdSelection> cloneList(List<ProdSelection> list) {
        List<ProdSelection> newList = new ArrayList<>();
        for (ProdSelection p : list) {
            newList.add(new ProdSelection(p));
        }
        return newList;
    }

    /**
     * Load training set / testing set
     * 
     * @param path
     *            file path
     * @param isTrain
     *            training set: true, testing set: false
     * @throws IOException
     */
    private void loadData(String path, boolean isTrain) throws IOException {
        List<ProdSelection> result = new ArrayList<ProdSelection>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.startsWith("@") && !(line.length() == 0)) {
                result.add(new ProdSelection(line));
            }
        }
        if (isTrain) {
            trainData = result;
            ProdSelection.resetMinMax(trainData);
        } else {
            testData = result;
        }
    }

    /**
     * calculate the similarity of p1 and p2, regarding to weight vetor w
     * 
     * @param p1
     * @param p2
     * @return similarity between p1 and p2, from 0 to Double.MAX_VALUE;
     */
    private double calculateSim(ProdSelection p1, ProdSelection p2,
            double[] w) {
        double dist = 0;
        if (p1.getType() != p2.getType()) {
            dist += 1 * w[0];
        }
        if (p1.getStyle() != p2.getStyle()) {
            dist += 1 * w[1];
        }
        dist += Math.pow(p1.getNVacation() - p2.getNVacation(), 2) * w[2];
        dist += Math.pow(p1.getNCredit() - p2.getNCredit(), 2) * w[3];
        dist += Math.pow(p1.getNSalary() - p2.getNSalary(), 2) * w[4];
        dist += Math.pow(p1.getNProperty() - p2.getNProperty(), 2) * w[5];
        if (dist == 0) {
            return Double.MAX_VALUE;
        } else {
            return 1 / Math.sqrt(dist);
        }
    }
    
    /**
     * calculate the similarity of p1 and p2 of ProdIntro class given simMatrix and weight w
     * 
     * @param p1 ProdIntro 
     * @param p2 ProdIntro
     * @return similarity between p1 and p2, from 0 to Double.MAX_VALUE;
     */
    private double calculateSim(ProdIntro p1, ProdIntro p2,
            double[] w) {
        double dist = 0;
        int row;
        int col;
        
        double[][] simType = new double[5][5];
        for (int i = 0; i < 5; i++) {
            simType[i][i] = 1;
        }
        simType[0][1] = simType[1][0] = 0;
        simType[0][2] = simType[2][0] = simType[3][4] = 0.1;
        simType[0][3] = simType[3][0] = 0.3;
        simType[0][4] = simType[4][0] = simType[2][3] = simType[2][4] = 
                simType[3][2] = simType[5][0] = simType[5][2] = 0.2;
        simType[1][2] = simType[2][1] = simType[1][3] = simType[1][4] = 
                simType[3][1] = simType[5][1] = 0;
       
        double[][] simCustomer= new double[5][5];
        for (int i = 0; i < 5; i++) {
            simCustomer[i][i] = 1;
        }
        simCustomer[0][1] = simCustomer[0][3] = simCustomer[1][0] = 
                simCustomer[1][2] = simCustomer[2][1] = simCustomer[3][0] = 0.2;
        simCustomer[0][2] = simCustomer[1][3] = simCustomer[2][0] = 
                simCustomer[2][3] = simCustomer[3][1] = simCustomer[3][2] = 0.1;
        simCustomer[0][4] = simCustomer[1][4] = simCustomer[2][4] = 
                simCustomer[3][4] = simCustomer[4][0] = simCustomer[4][1] = 
                simCustomer[4][2] = simCustomer[4][3] = 0;
        
        double[][] simSize= new double[3][3];
        for (int i = 0; i < 3; i++) {
            simSize[i][i] = 1;
        }
        simSize[0][1] = simSize[1][0] = simSize[1][2] = simSize[2][1] = 0.1;
        simSize[0][2] = simSize[2][0] = 0;
        
        double[][] simPromotion= new double[4][4];
        for (int i = 0; i < 4; i++) {
            simPromotion[i][i] = 1;
        }
        simPromotion[0][1] = simPromotion[1][0] = 0.8;
        simPromotion[0][2] = simPromotion[0][3] = simPromotion[2][0] = simPromotion[3][0] = 0;
        simPromotion[1][2] = simPromotion[2][1] = 0.1;
        simPromotion[1][3] = simPromotion[3][1] = 0.5;
        simPromotion[2][3] = simPromotion[3][2] = 0.4;
        
        row = p1.getType().ordinal();
        col = p2.getType().ordinal();
        dist += simType[row][col] * w[0];
       
        row = p1.getCustomer().ordinal();
        col = p2.getCustomer().ordinal();
        dist += simCustomer[row][col] * w[1];
        
        dist += Math.pow(p1.getNFee() - p2.getNFee(), 2) * w[2];
        dist += Math.pow(p1.getNBudget() - p2.getNBudget(), 2) * w[3];
        
        row = p1.getSize().ordinal();
        col = p2.getSize().ordinal();
        dist += simSize[row][col] * w[4];
        
        row = p1.getPromotion().ordinal();
        col = p2.getPromotion().ordinal();
        dist += simPromotion[row][col] * w[5];
        
        dist += Math.pow(p1.getNRate() - p2.getNRate(), 2) * w[6];
        dist += Math.pow(p1.getNPeriod() - p2.getNPeriod(), 2) * w[7];
        if (dist == 0) {
            return Double.MAX_VALUE;
        } else {
            return 1 / Math.sqrt(dist);
        }
    }

    /**
     * similarity object, stores current predicted label and score
     * 
     * @author Yuheng Li
     * @version 1.0
     * @since Apr 3, 2016
     */
    private class Sim implements Comparable<Sim> {
        double score;
        int label;

        public Sim(double s, int l) {
            this.score = s;
            this.label = l;
        }

        @Override
        public int compareTo(Sim o) {
            if (this.score == o.score) {
                return Integer.compare(this.label, o.label);
            } else {
                return -Double.compare(this.score, o.score);
            }
        }
    }

    // testing method, using all default settings.
    public static void main(String[] args) {
        // default
        KNN test = new KNN();
        double[] weight = {0.002, 0.00, 0.006, 0.172, 0.013, 0.109};
        double result = test.crossValidation(weight);
        // Result result = test.predict(TRAINPATH);
        // test.validate(result);
        // System.out.println(result.accuracy);
    }
}
