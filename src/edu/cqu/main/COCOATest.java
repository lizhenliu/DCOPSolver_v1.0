package edu.cqu.main;

import edu.cqu.core.FinishedListener;
import edu.cqu.core.Solver;
import edu.cqu.result.Result;
import edu.cqu.result.ResultCycle;

public class COCOATest {
    public static void main(String[] args) {
        Solver solver = new Solver();
        solver.solve("config/am.xml","COCOA", "problem/dcop/GC/GRAPH_COLORING_30_3_density_0.1_0.xml", new FinishedListener() {
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
