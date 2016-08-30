import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Runner {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int fold = 10;
        List<Integer> suffleIdx = new ArrayList<Integer>();
        for (int i = 0; i < 186; i++) {
            suffleIdx.add(i);
        }
        Collections.shuffle(suffleIdx);

        int size = 186;
        int a = 186 / 10;
        int b = 186 % 10;
        int count = 0;
        List<List<Integer>> partitions = new LinkedList<List<Integer>>();
        int start = 0;
        while(start < size) {
            int end = start + a + (count++ < b ? 1 : 0);
            List<Integer> temp = suffleIdx.subList(start,
                    Math.min(end , suffleIdx.size()));
            List<Integer> train = new ArrayList<>();
            for (Integer cur : temp) {
                train.add(Integer.valueOf(cur));
            }
            System.out.println(train.size() + ": " + train.toString());
            List<Integer> test = new ArrayList<>();
            temp = suffleIdx.subList(0, start);
            for (Integer cur : temp) {
                test.add(Integer.valueOf(cur));
            }
            temp = suffleIdx.subList(Math.min(end, suffleIdx.size()),
                    suffleIdx.size());
            for (Integer cur : temp) {
                test.add(Integer.valueOf(cur));
            }
            System.out.println(test.size() + ": " + test.toString());
            start = end;
        }

        Sim s1 = new Sim(0.8, 1);
        Sim s2 = new Sim(1.9, 2);
        Sim s3 = new Sim(1.9, 1);
        PriorityQueue<Sim> queue = new PriorityQueue<>();
        queue.add(s1);
        queue.add(s2);
        queue.add(s3);
        for (int i = 0; i < 3; i++) {
            Sim tSim = queue.poll();
            System.out.println(tSim.score + "+" + tSim.label);
        }
    }

    static class AA {
        int i;

        AA(int i) {
            this.i = i;
        }

    }

    static private class Sim implements Comparable<Sim> {
        double score;
        int label;

        public Sim(double s, int l) {
            this.score = s;
            this.label = l;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(Sim o) {
            if (this.score == o.score) {
                return Integer.compare(this.label, o.label);
            } else {
                return -Double.compare(this.score, o.score);
            }
        }
    }
}
