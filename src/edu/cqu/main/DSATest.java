package edu.cqu.main;

import edu.cqu.core.FinishedListener;
import edu.cqu.core.Solver;
import edu.cqu.result.Result;
import edu.cqu.result.ResultCycle;

public class DSATest {
    public static void main(String[] args) {
        Solver solver = new Solver();
        solver.solve("config/am.xml","DSA", "problem/dcop/30/RANDOM_DCOP_30_10_density_0.2_2.xml", new FinishedListener() {
            @Override
            public void onFinished(Result result) {
                ResultCycle resultCycle = (ResultCycle) result;
                for (Double cost : resultCycle.costInCycle){
                    System.out.println(cost);
                }
            }
        },false,false);
    }
}
