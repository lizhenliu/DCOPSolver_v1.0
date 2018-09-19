package edu.cqu.benchmark.dcop;

import edu.cqu.benchmark.dcop.randomDCOPs.RandomDCOPsGenerator;
import edu.cqu.benchmark.dcop.coloringGraph.ColoringGraphGenerator;

import edu.cqu.benchmark.dcop.scaleFreeNetWork.ScaleFreeNetWorkGenerator;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Contentwriter {

    public static final String RANDOM_DCOP_PROBLEM = "RANDOM_DCOP_PROBLEM";
    public static final String SCALEFREE_PROBLEM = "SCALEFREE_PROBLEM";
    public static final String COLORING_GRAPH_PROBLEM = "COLORING_GRAPH_PROBLEM";
    public static final String WEIGHT_COLORING_GRAPH_PROBLEM = "WEIGHT_COLORING_GRAPH_PROBLEM";

    public String dirPath;
    public String problemType;
    public int nbAgents;
    public int nbInstance;
    public int domainSize;
    public int minCost;
    public int maxCost;
    HashMap<String,Object> extraPara;

    public Contentwriter(String dirPath, String problemType, int nbInstance, int nbAgents, int domainSize, int minCost, int maxCost, HashMap<String, Object> extraPara) {
        this.dirPath = dirPath;
        this.problemType = problemType;
        this.nbAgents = nbAgents;
        this.nbInstance = nbInstance;
        this.domainSize = domainSize;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.extraPara = extraPara;
    }

    private void generate() throws IOException {
        String baseFileName = dirPath+"_"+problemType+"_"+ nbAgents+"_"+domainSize+"_";
        int base = 0;
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        for (int i = 0; i < nbInstance; i++) {
            FileOutputStream stream = new FileOutputStream(baseFileName +(base+i)+".xml");
            Element root = new Element("instance");
            AbstractGraph graph = null;
            switch (problemType){
                case RANDOM_DCOP_PROBLEM:
                    graph = new RandomDCOPsGenerator(problemType,i,nbAgents,domainSize,minCost,maxCost,(double)(extraPara.get("density")));
                    break;
                case COLORING_GRAPH_PROBLEM:
                    graph = new ColoringGraphGenerator(problemType,i,nbAgents,domainSize,minCost,maxCost,(double)(extraPara.get("density")));
                    break;
                case WEIGHT_COLORING_GRAPH_PROBLEM:
                    graph = new ColoringGraphGenerator(problemType,i,nbAgents,domainSize,minCost,maxCost,(double)(extraPara.get("density")));
                    break;
                case SCALEFREE_PROBLEM:
                    graph = new ScaleFreeNetWorkGenerator(problemType,i,nbAgents,domainSize,minCost,maxCost,20,(Integer)extraPara.get("m1"));
                    break;
            }
            graph.generateConstraint();
            root.addContent(graph.getPresentation());
            root.addContent(graph.getAgents());
            root.addContent(graph.getDomains());
            root.addContent(graph.getVariables());
            root.addContent(graph.getConstraints());
            root.addContent(graph.getRelations());
            root.addContent(graph.getGuiPresentation());
            xmlOutputter.output(root,stream);
            stream.close();
        }

        File file = new File(dirPath);
        if (!file.exists()){
            file.mkdirs();
        }

        while (true){
            String filename = baseFileName + base +".xml";
            if (!new File(filename).exists())
                break;
            base++;
        }
    }

    public static void main(String[] args) throws IOException{
        HashMap<String,Object> para = new HashMap<>();
        HashMap<String,Object> paras = new HashMap<>();
        para.put("density",0.6);
        paras.put("m1",3);
        String dir = "E:\\DCOP\\problems\\test1\\";
        String problemType = COLORING_GRAPH_PROBLEM;
        String problemTypes = SCALEFREE_PROBLEM;
        Contentwriter writer = new Contentwriter(dir,problemType,1,20,10,1,100,para);
//        Contentwriter writer = new Contentwriter(dir,problemTypes,1,20,10,1,100,paras);
        writer.generate();
    }
}
