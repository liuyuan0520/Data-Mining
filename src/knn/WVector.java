import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by liuyuan on 4/3/16.
 */
public class WVector {

    private static double[] w = {0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
    private static List<ProdSelection> train;
    private static List<ProdSelection> test;
    private static final int DROP = 10;

    public static void main(String[] args) {
        KNN knn = new KNN();
        for (int i = 0; i < 6; i++) {
            WeightAndAccuracy optimal = null;
            double j = 0;
            ArrayList<Double> list = new ArrayList<>();
            while (!hasFinished(list)) {

                w[i] = j;
                Result result = knn.predict(train, test, w);
                double accuracy = result.accuracy;
                if (optimal == null) {
                    optimal = new WeightAndAccuracy(j, accuracy);
                } else {
                    if (accuracy > optimal.accuracy) {
                        optimal = new WeightAndAccuracy(j, accuracy);
                    }
                }

                list.add(accuracy);
                if (list.size() > DROP) {
                    list.remove(0);
                }

                System.out.println("weight = " + j + "\n accuracy = " + result.accuracy);
            }
            w[i] = optimal.weight;
        }
    }

    public static boolean hasFinished (ArrayList<Double> list) {
        for (int i = 0; i < DROP - 1; i++) {
            if (list.get(i + 1) > list.get(i)) {
                return false;
            }
        }
        return true;
    }

    static class WeightAndAccuracy {
        private double weight;
        private double accuracy;

        public WeightAndAccuracy (double weight, double accuracy) {
            this.weight = weight;
            this.accuracy = accuracy;
        }
    }
}
