package com.edu.cqu.main;

import com.edu.cqu.core.FinishedListener;
import com.edu.cqu.core.Solver;
import com.edu.cqu.result.Result;
import com.edu.cqu.result.ResultCycle;

public class DSATest {
    public static void main(String[] args) {
        Solver solver = new Solver();
        solver.solve("sssss","DSA_A", "C:\\Users\\Administrator.UE8E6XNMLQRIGS8\\Desktop\\问题\\20\\RandomDCOP_20_10_1.xml", new FinishedListener() {
            @Override
            public void onFinished(Result result) {
                ResultCycle resultCycle = (ResultCycle)result;
                for (int i = 0; i < resultCycle.costInCycle.length;i++){
                    System.out.println((i + 1) + "\t" + resultCycle.costInCycle[i]);
                }
            }
        },false);
    }
}
