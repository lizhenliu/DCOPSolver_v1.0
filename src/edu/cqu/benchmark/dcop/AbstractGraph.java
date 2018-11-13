package edu.cqu.benchmark.dcop;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public abstract class AbstractGraph {

    public String name;
    public String type = "DCOP";
    public String problemType;
    public String model = "Simple";
    public String constraintModel = "TKC";
    public String format = "XDisCSP 1.0";
    protected String benchmark = "RandomDCOP";
    public int instanceId;
    public int nbAgents;
    public int nbVariables;
    public int nbConstraints;
    public int nbRelations;
    public int nbTuples;
    public double density;
    public int tightness;
    public int domainSize;
    public int minCost;
    public int maxCost;
    public Random random;
    public ArrayList<Integer> source;
    public ArrayList<Integer> dest;
    public HashMap<Integer,Set<Integer>> adjacentTable;

    public AbstractGraph(String name, int nbAgents, int domainSize, int minCost, int maxCost) {

        random = new Random();
        source = new ArrayList<>();
        dest = new ArrayList<>();
        adjacentTable = new HashMap<>();
        this.problemType = problemType;
        this.instanceId = instanceId;
        this.nbAgents = nbAgents;
        this.domainSize = domainSize;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.nbVariables = nbAgents;
        this.setNbTuples(domainSize);
        this.nbTuples = getNbTuples();
        this.name = name;
    }

    public Element getPresentation(){
        Element presentation = new Element("presentation");
        presentation.setAttribute("name",name);
        presentation.setAttribute("type",type);
        presentation.setAttribute("benchmark",benchmark);
        presentation.setAttribute("model",model);
        presentation.setAttribute("constraintModel",constraintModel);
        presentation.setAttribute("format",format);
        return presentation;
    }

    public Element getAgents(){
        Element agents = new Element("agents");
        agents.setAttribute("nbAgents",String.valueOf(nbAgents));
        for (int i = 1; i < nbAgents + 1; i++) {
            Element agent = new Element("agent");
            agent.setAttribute("name","A"+i);
            agent.setAttribute("id",String.valueOf(i));
            agent.setAttribute("description","Agent " + String.valueOf(i));
            agents.addContent(agent);
        }
        return agents;
    }

    public Element getDomains(){
        Element domains = new Element("domains");
        domains.setAttribute("nbDomains","1");
        Element domain = new Element("domain");
        domain.setAttribute("name","D1");
        domain.setAttribute("nbValues",String.valueOf(domainSize));
        domain.addContent("1.." + domainSize);
        domains.addContent(domain);
        return domains;
    }

    public Element getVariables(){
        Element variables = new Element("variables");
        variables.setAttribute("nbVariables",String.valueOf(nbVariables));
        for (int i = 1; i < nbVariables + 1; i++) {
            Element variable = new Element("variable");
            variable.setAttribute("name","X"+i+".1");
            variable.setAttribute("agent","A"+i);
            variable.setAttribute("id",String.valueOf(1));
            variable.setAttribute("domain","D1");
            variable.setAttribute("description","variable " + String.valueOf(i) + ".1");
            variables.addContent(variable);
        }
        return variables;
    }

    public Element getConstraints(){
        Element constraints = new Element("constraints");
        constraints.setAttribute("nbConstraints",String.valueOf(nbConstraints));
        constraints.setAttribute("initialCost",String.valueOf(this.getMinCost()));
        constraints.setAttribute("maximalCost","infinity");
        for (int i = 0; i < nbConstraints; i++) {
            Element constraint = new Element("constraint");
            constraint.setAttribute("name","C" + String.valueOf(i));
            constraint.setAttribute("model",this.getConstraintModel());
            constraint.setAttribute("arity","2");
            constraint.setAttribute("scope","X"+source.get(i)+".1 X" + dest.get(i)+".1");
            constraint.setAttribute("reference","R" + String.valueOf(i));
            constraints.addContent(constraint);
        }
        return constraints;
    }

    public Element getRelations(){
        Element relations = new Element("relations");
        relations.setAttribute("nbRelations",String.valueOf(nbRelations));
        for (int i = 0; i < nbRelations; i++) {
            Element relation  = new Element("relation");
            relation.setAttribute("name","R" + String.valueOf(i));
            relation.setAttribute("arity",String.valueOf("2"));
            relation.setAttribute("nbTuples",String.valueOf(domainSize*domainSize));
            relation.setAttribute("semantics","soft");
            relation.setAttribute("defaultCost","infinity");
            relation.addContent(this.getTuple());
            relations.addContent(relation);
        }
        return relations;
    }

    public Element getGuiPresentation(){
        Element guiPresentation = new Element("GuiPresentation");
        guiPresentation.setAttribute("type",type);
        guiPresentation.setAttribute("benchmark",problemType);
        guiPresentation.setAttribute("name","instance"+String.valueOf(instanceId));
        guiPresentation.setAttribute("nbAgents",String.valueOf(nbAgents));
        guiPresentation.setAttribute("nbVariables",String.valueOf(nbVariables));
        guiPresentation.setAttribute("model",model);
        guiPresentation.setAttribute("domainSize",String.valueOf(domainSize));
        guiPresentation.setAttribute("density",String.valueOf(density));
        guiPresentation.setAttribute("tightness",String.valueOf(tightness));
        guiPresentation.setAttribute("nbConstraints",String.valueOf(nbConstraints));
        guiPresentation.setAttribute("nbTuple",String.valueOf(nbTuples));
        return guiPresentation;
    }

    private String getTuple() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < domainSize; i++) {
            for (int j = 0; j < domainSize; j++) {
                int cost = getRandomCost(i,j);
                stringBuilder.append(cost);
                stringBuilder.append(":"+String.valueOf(i)+" " + String.valueOf(j)+"|");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }



    protected abstract void generateConstraint();

    protected int getRandomCost(int i, int j){
        return  minCost + (int)(random.nextDouble() * (maxCost - minCost + 1));
    }

    public int getDomainSize() {
        return domainSize;
    }

    public void setDomainSize(int domainSize) {
        this.domainSize = domainSize;
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

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
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

    public int getNbTuples() {
        return nbTuples;
    }

    public void setNbTuples(int domainSize) {
        this.nbTuples = domainSize*domainSize;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public int getTightness() {
        return tightness;
    }

    public void setTightness(int tightness) {
        this.tightness = tightness;
    }

    public ArrayList<Integer> getSource() {
        return source;
    }

    public void setSource(ArrayList<Integer> source) {
        this.source = source;
    }

    public ArrayList<Integer> getDest() {
        return dest;
    }

    public void setDest(ArrayList<Integer> dest) {
        this.dest = dest;
    }
}
