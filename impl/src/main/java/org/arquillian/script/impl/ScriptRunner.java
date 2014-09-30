package org.arquillian.script.impl;

import groovy.lang.GroovyShell;
import groovy.util.ResourceConnector;
import groovy.util.ResourceException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.arquillian.script.api.ArquillianEnvironment;
import org.codehaus.groovy.control.CompilerConfiguration;

public class ScriptRunner {

    private ResourceConnector connector;

    public ScriptRunner(ResourceConnector connector) {
        this.connector = connector;
    }

    public ArquillianEnvironment run(String script) throws Exception {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(ArquillianEnvironment.class.getName());
        GroovyShell shell = new GroovyShell(config);
        
//        GroovyScriptEngine gse = new GroovyScriptEngine(connector);
//        System.out.println(ArquillianEnvironment.class.getName());
//        gse.getConfig().setScriptBaseClass(ArquillianEnvironment.class.getName());

        return (ArquillianEnvironment)shell.run(script, "arquillian", new ArrayList<String>());
        
//        Binding b = new Binding();
//        return (ArquillianEnvironment)gse.run(script, b);
    }

    public ArquillianEnvironment runScript(String script) throws Exception {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(ArquillianEnvironment.class.getName());
        GroovyShell shell = new GroovyShell(config);
        ArquillianEnvironment env = (ArquillianEnvironment)shell.parse(script);
        env.run();
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
