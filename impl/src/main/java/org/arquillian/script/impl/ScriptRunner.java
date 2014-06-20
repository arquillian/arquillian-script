package org.arquillian.script.impl;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceConnector;
import groovy.util.ResourceException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.arquillian.script.api.ArquillianEnvironment;

public class ScriptRunner {

    private ResourceConnector connector;

    public ScriptRunner(ResourceConnector connector) {
        this.connector = connector;
    }

    public ArquillianEnvironment run(String script) throws Exception {
        GroovyScriptEngine gse = new GroovyScriptEngine(connector);

        ArquillianEnvironment env = new ArquillianEnvironment();

        Binding binding = new Binding();
        binding.setVariable("arquillian", env);
        gse.run(script, binding);

        return env;
    }

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            printUsage("Not enough arguments");
        }
        new ScriptRunner(new ResourceConnector() {
            
            @Override
            public URLConnection getResourceConnection(String name)
                    throws ResourceException {
                try {
                    return new URL("file://" + name).openConnection();
                } catch (IOException e) {
                    throw new ResourceException(e);
                }
            }
        }).run(args[0]);
    }

    private static void printUsage(String string) {
        System.out.println(string);
        
        System.out.println("java -jar arquillian-script-impl.jar my-script.groovy");
    }
}
