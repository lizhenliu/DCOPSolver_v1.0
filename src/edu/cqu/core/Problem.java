package edu.cqu.core;

import java.util.HashMap;
import java.util.Map;

public class Problem {
    public int[] allId;
    public Map<Integer,int[]> domains;
    public Map<Integer,Map<Integer,int[][]>> constraintCost;
    public Map<Integer,int[]> neighbors;

    public Map<Integer, int[]> getNeighborDomain(int id) {
        Map<Integer,int[]> neighorDomain = new HashMap<>();
        int[] neighbor = neighbors.get(id);
        for (int neighborId : neighbor) {
            neighorDomain.put(neighborId,domains.get(neighborId));
        }
        return neighorDomain;
    }
}
