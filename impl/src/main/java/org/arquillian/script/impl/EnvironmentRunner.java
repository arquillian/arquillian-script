package org.arquillian.script.impl;

import org.arquillian.script.api.ArquillianEnvironment;

public class EnvironmentRunner {
    
    private ArquillianEnvironment environment;
    
    public EnvironmentRunner(ArquillianEnvironment environment) {
        this.environment = environment;
    }
    
    public void run() throws Exception {
        // do some magic with the 'configured Arquillian env' to run it 
    }
}
