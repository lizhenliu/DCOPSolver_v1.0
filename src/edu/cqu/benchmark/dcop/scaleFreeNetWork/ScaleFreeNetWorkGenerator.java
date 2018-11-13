package edu.cqu.benchmark.dcop.scaleFreeNetWork;

import edu.cqu.benchmark.dcop.AbstractGraph;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class ScaleFreeNetWorkGenerator extends AbstractGraph {

    private int m0;
    private int m1;
    private Set<Integer> connectedSet;
    private HashMap<Integer,Integer> edgeCount;

    public ScaleFreeNetWorkGenerator(String name, String problemType, int instanceId, int nbAgents, int domainSize, int minCost, int maxCost,int m0,int m1) {
        super(name, nbAgents, domainSize, minCost, maxCost);
        this.m0 = m0;
        this.m1 = m1;
        connectedSet = new HashSet<>();
        edgeCount = new HashMap<>();
    }

    @Override
    protected void generateConstraint() {
        generateInitConstraint();
        ext:for (int i = 1; i < nbAgents; i++) {
            if (connectedSet.contains(i))
                continue ext;
            int[] preferenceList = new int[m1];
            int nbedges = nbRelations+1;
            for (int j = 0; j < m1; j++) {
                double p = Math.random();
                double pi = 0;
                int test = 0;
                for (int id : connectedSet) {
                    pi += (double)((double)edgeCount.get(id)/(double)nbedges);
                    if (p < pi){
                        preferenceList[j] = id;
                        break;
                    }
                }
            }
            for (int j = 0; j < m1; j++) {
                source.add(preferenceList[j]<i?preferenceList[j]:i);
                dest.add(preferenceList[j]<i?preferenceList[j]:i);
                edgeCount.put(preferenceList[j],edgeCount.get(preferenceList[j])+1);
                connectedSet.add(preferenceList[j]);
                nbRelations++;
                nbConstraints++;
            }
            edgeCount.put(i,edgeCount.get(i)+m1);
            connectedSet.add(i);
        }
    }

    private void generateInitConstraint() {
        int agents[] = new int[nbAgents];
        HashSet<Integer> sampled = new HashSet<>();
        int[] sampledList = new int[m0];
        for (int i = 0; i < nbAgents; i++) {
            agents[i] = i +1;
            edgeCount.put(i+1,0);
        }
        int sampleIndex;

        for (int i = 0; i <m0; i++) {
            sampleIndex = random.nextInt(nbAgents) + 1;
            while (sampled.contains(sampleIndex)){
                sampleIndex = random.nextInt(nbAgents) + 1;
            }
            sampled.add(sampleIndex);
            sampledList[i] = sampleIndex;
            connectedSet.add(sampledList[i]);
            edgeCount.put(sampledList[i],1);
        }

        for (int i = 0; i < m0-1; i++) {
            source.add(sampledList[i]<sampledList[i+1]?sampledList[i]:sampledList[i+1]);
            dest.add(sampledList[i]>sampledList[i+1]?sampledList[i]:sampledList[i+1]);
            nbRelations++;
            nbConstraints++;
        }

    }
}
