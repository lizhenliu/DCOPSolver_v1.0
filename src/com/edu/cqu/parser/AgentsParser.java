package com.edu.cqu.parser;

import com.edu.cqu.core.AgentDescriptor;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentsParser {
    private String agentPath;

    public AgentsParser(String agentPath) {
        this.agentPath = agentPath;
    }

    public Map<String,AgentDescriptor> paraser() {
        Map<String,AgentDescriptor> map = new HashMap<>();
        try {
            Element root = new SAXBuilder().build(new File(agentPath)).getRootElement();
            List<Element> agentList = root.getChild("agent").getChildren();
            for (Element agentElement : agentList) {
                String name = agentElement.getAttributeValue("name").toUpperCase();
                AgentDescriptor agentDescriptor = new AgentDescriptor();
                agentDescriptor.className = agentElement.getAttributeValue("class");
                agentDescriptor.method = agentElement.getAttributeValue("method").toUpperCase();
                map.put(name.toUpperCase(),agentDescriptor);
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String,String> parseConfigurations(){
        Map<String,String> configurations = new HashMap<>();
        Element root = null;
        try{
            root = new SAXBuilder().build(new File(agentPath)).getRootElement();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root.getChildren("configurations").size() == 0){
            return configurations;
        }
        List<Element> configurationList = root.getChildren("configurations");
        for (Element configuration : configurationList) {
            configurations.put(configuration.getAttributeValue("name").toUpperCase(),configuration.getAttributeValue("value").toUpperCase());
        }
        return configurations;
    }
}
