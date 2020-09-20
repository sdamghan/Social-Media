import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class MakeComms {

    long randomSeed;

    public MakeComms() {

        randomSeed = System.currentTimeMillis();
    }

    public class CNode {

        String uSlug;
        boolean isExist;
        int weight;
        ArrayList<String> contents;

        public CNode(String uSlug, int weight) {

            this.uSlug = uSlug;
            this.weight = weight;
            isExist = true;
            contents = new ArrayList<String>();
            contents.add(uSlug);
        }
    }

    private class Step {

        int u;
        int v;

        private Step(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    public class Result {

        int finalWeight;
        ArrayList<Step> steps;

        public Result() {

            this.finalWeight = 10000;
            steps = new ArrayList<Step>();
        }
    }

    private Result makeComms(CNode[][] graph, int size) {

        CNode[][] temp = new CNode[size][size];
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                temp[i][j] = new CNode(graph[i][j].uSlug, graph[i][j].weight);
            }
        }

        Result result = new Result();

        int tempSize = size;

        int finalSize = 1;

        while (tempSize > 2) {

            Random randomProducer  = new Random(randomSeed);
            int num1 = randomProducer.nextInt(size);
            int num2 = randomProducer.nextInt(size);

            if((temp[num1][num2].isExist) && (temp[num1][num2].weight > 0)) { // yani agar mojaver hastan

                result.steps.add(new Step(num1, num2));
                finalSize = mergeTwoNodes(temp, size, num1, num2, finalSize);
                tempSize--;
            }

        }

        result.finalWeight = finalSize;

        return result;

    }
    
    private int mergeTwoNodes(CNode[][] temp,int size, int num1, int num2, int fs) {

        int finalSize = fs;

        for (int i=0; i<size; i++) {

            if ((temp[num2][i].isExist) && ((temp[i][num2].isExist)) && (i != num1) && (i != num2)) {

                temp[num1][i].weight += temp[num2][i].weight;
                temp[num2][i].isExist = false;
                temp[i][num1].weight += temp[num2][i].weight;
                temp[i][num2].isExist = false;
                temp[i][num1].contents.addAll(temp[num2][i].contents);

                if (temp[i][num1].weight > finalSize)
                    finalSize = temp[i][num1].weight;
            }
            temp[num2][num1].isExist = false;
            temp[num1][num2].isExist = false;
            temp[num2][num2].isExist = false;
        }

        return finalSize;
    }

    private TwoComm bestPartitioning (CNode[][] graph, int size, ArrayList<Step> steps) {

        TwoComm ption = new TwoComm();

        CNode[][] temp = new CNode[size][size];
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                temp[i][j] = new CNode(graph[i][j].uSlug, graph[i][j].weight);
            }
        }

        Result result = new Result();

        int tempSize = size;

        int finalSize = 1;
        int limit = steps.size();

        while (tempSize > 2) {

            int num1 = steps.get(limit - tempSize + 2).u;
            int num2 = steps.get(limit - tempSize + 2).v;

            if((temp[num1][num2].isExist) && (temp[num1][num2].weight > 0)) { // yani agar mojaver hastan

                result.steps.add(new Step(num1, num2));
                finalSize = mergeTwoNodes(temp, size, num1, num2, finalSize);
                tempSize--;
            }

        }

        boolean setFirstComm = false;
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {

                if(graph[i][j].isExist) {

                    if(!setFirstComm) {
                        ption.com1 = graph[i][j].contents;
                    }

                    else {
                        ption.com2 = graph[i][j].contents;
                    }
                }
            }
        }

        result.finalWeight = finalSize;

//        return result;


        return ption;
    }

    public TwoComm findBestPartitioning(CNode[][] graph, int size) {

        int repeatTimes = (size*(size+1))/2;

        Result bestResult = new Result();
        Result tempResult = new Result();

        for (int i=0; i<repeatTimes; i++) {

            tempResult = makeComms(graph, size);
            if (tempResult.finalWeight < bestResult.finalWeight) {

                bestResult = tempResult;
            }
        }

        return  bestPartitioning(graph, size, bestResult.steps);
    }

    /*
    public static void main(String[] args) {

    }
    */
}


class TwoComm {

    ArrayList<String> com1;
    ArrayList<String> com2;

    public TwoComm() {

        com1 = new ArrayList<String>();
        com2 = new ArrayList<String>();
    }
}
