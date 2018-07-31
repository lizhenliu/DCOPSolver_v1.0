package com.edu.cqu.benchmark;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jdom2.Element;

public class AbstractGraph {
    protected String name;
    protected String type = "DCOP";
    protected String benchmark = "RandomDCOP";
    protected String model = "Simple";
    protected String constraintModel = "TKC";
    protected String format = "XDisCSP 1.0";
    protected int nbAgents;
    protected int nbVariables;
    protected int nbDomains;
    protected int nbConstraints;
    protected int nbRelations;
    protected int domainsize;
    protected int density;
    protected int tightness;
    protected int nbTuples;
    protected int minCost;

    public AbstractGraph(String name, int nbAgents, int domainsize, int minCost, int maxCost) {
        this.name = name;
        this.nbAgents = nbAgents;
        this.domainsize = domainsize;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.nbVariables = nbAgents;
        this.random = new Random();
        this.source = new ArrayList<Integer>();
        this.dest = new ArrayList<Integer>();
    }

    protected int maxCost;
    protected List<Integer> source;
    protected List<Integer> dest;
    protected Random random;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getConstraintModel() {
        return constraintModel;
    }

    public void setConstraintModel(String constraintModel) {
        this.constraintModel = constraintModel;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getNbAgents() {
        return nbAgents;
    }

    public void setNbAgents(int nbAgents) {
        this.nbAgents = nbAgents;
    }

    public int getNbVariables() {
        return nbVariables;
    }

    public void setNbVariables(int nbVariables) {
        this.nbVariables = nbVariables;
    }

    public int getNbDomains() {
        return nbDomains;
    }

    public void setNbDomains(int nbDomains) {
        this.nbDomains = nbDomains;
    }

    public int getNbConstraints() {
        return nbConstraints;
    }

    public void setNbConstraints(int nbConstraints) {
        this.nbConstraints = nbConstraints;
    }

    public int getNbRelations() {
        return nbRelations;
    }

    public void setNbRelations(int nbRelations) {
        this.nbRelations = nbRelations;
    }

    public int getDomainsize() {
        return domainsize;
    }

    public void setDomainsize(int domainsize) {
        this.domainsize = domainsize;
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }

    public int getTightness() {
        return tightness;
    }

    public void setTightness(int tightness) {
        this.tightness = tightness;
    }

    public int getNbTuples() {
        return nbTuples;
    }

    public void setNbTuples(int nbTuples) {
        this.nbTuples = nbTuples;
    }

    public int getMinCost() {
        return minCost;
    }

    public void setMinCost(int minCost) {
        this.minCost = minCost;
    }

    public int getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(int maxCost) {
        this.maxCost = maxCost;
    }

    public List<Integer> getSource() {
        return source;
    }

    public void setSource(List<Integer> source) {
        this.source = source;
    }

    public List<Integer> getDest() {
        return dest;
    }

    public void setDest(List<Integer> dest) {
        this.dest = dest;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Element getAgents(){
        Element agentRootElement = new Element("agents");
        agentRootElement.setAttribute("nbAgents", String.valueOf(nbAgents));
        for (int i = 1; i <= nbAgents; i++) {
            Element agent = new Element("agent");
            agent.setAttribute("name","A"+i);
            agent.setAttribute("id", String.valueOf(i));
            agent.setAttribute("descroption","Agent "+i);
            agentRootElement.addContent(agent);
        }
        return agentRootElement;
    }

    public Element getVaribales(){
        Element variableRootElement = new Element("variables");
        variableRootElement.setAttribute("nbVariables", String.valueOf(nbVariables));
        for (int i = 1; i <= nbVariables; i++) {
            Element variable = new Element("variable");
            variable.setAttribute("agent","A"+i);
            variable.setAttribute("name","X"+i+".1");
            variable.setAttribute("id","1");
            variable.setAttribute("domain","D1");
            variable.setAttribute("description",i+".1");
            variableRootElement.addContent(variable);
        }
        return variableRootElement;
    }

    public Element getDomains(){
        Element domainRootElement = new Element("domains");
        domainRootElement.setAttribute("nbDomains", String.valueOf(nbDomains));
        for (int i = 0; i < nbDomains; i++) {
            Element domain = new Element("domain");
            domain.setAttribute("name","D"+i);
            domain.setAttribute("nbValues", String.valueOf(domainsize));
            domain.addContent("1.."+domainsize);
            domainRootElement.addContent(domain);
        }
        return domainRootElement;
    }

    public Element getConstraints(){
        Element constraintRootElement = new Element("constraints");
        constraintRootElement.setAttribute("nbConstraints", String.valueOf(nbConstraints));
        constraintRootElement.setAttribute("initialCost", String.valueOf(0));
        constraintRootElement.setAttribute("maximalCosts","infinity");
        for (int i = 0; i < nbConstraints; i++) {
            Element constraint = new Element("constraint");
            constraint.setAttribute("name","C"+i);
            constraint.setAttribute("model","TKC");
            constraint.setAttribute("arity", String.valueOf(2));
            constraint.setAttribute("scope",source.get(i)+".1 X" + dest.get(i)+".1");
            constraint.setAttribute("reference","R"+i);
            constraintRootElement.addContent(constraint);
        }
        return constraintRootElement;
    }

    public Element getRelations(){
        Element relationRootElement = new Element("relations");
        relationRootElement.setAttribute("nbRelations", String.valueOf(nbRelations));
        for (int i = 0; i < nbRelations; i++) {
            Element relation = new Element("relation");
            relation.setAttribute("name","R"+i);
            relation.setAttribute("arity", String.valueOf(2));
            relation.setAttribute("nbTuples", String.valueOf(domainsize*domainsize));
            relation.setAttribute("semantics","soft");
            relation.setAttribute("defaultCost","infinity");
            relation.addContent(getTuples());
            relationRootElement.addContent(relation);
        }
        return relationRootElement;
    }

    private String getTuples() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < domainsize; i++) {
            for (int j = 0; j < domainsize; j++) {
                int cost = randomCost(i,j);
                stringBuilder.append(cost + ":");
                stringBuilder.append(i + " " + j+ "|");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    protected int randomCost(int i, int j) {
        return minCost + (int)(random.nextDouble()*(maxCost-minCost+1));
    }

    public Element getGuiPresentation(){
        Element guipresentation = new Element("GuiPresentation");
        guipresentation.setAttribute("name",name);
        guipresentation.setAttribute("type",type);
        guipresentation.setAttribute("benchmark",benchmark);
        guipresentation.setAttribute("model",model);
        guipresentation.setAttribute("nbAgent", String.valueOf(nbAgents));
        guipresentation.setAttribute("domainSize", String.valueOf(domainsize));
        guipresentation.setAttribute("density", String.valueOf(density));
        guipresentation.setAttribute("tightness", String.valueOf(tightness));
        guipresentation.setAttribute("nbConstraints", String.valueOf(nbConstraints));
        guipresentation.setAttribute("nbTuples", String.valueOf(nbTuples));
        return guipresentation;
    }
}
