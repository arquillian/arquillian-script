package org.arquillian.script.api;

import groovy.lang.Closure;

import java.util.HashSet;
import java.util.Set;

public abstract class ArquillianEnvironment extends groovy.lang.Script {

    public Set<Environment> environments = new HashSet<>();

    private Scenario scenario;
    private Closure<Scenario> scenarioClosure;
    
    public void environment(Closure<Environment> cl) {
        Environment environment = new Environment();
        cl.setDelegate(environment);
        cl.setDirective(Closure.DELEGATE_FIRST);
        cl.call(environment);
        this.environments.add(environment);
    }

    public void scenario(Closure<Scenario> cl) {
        Scenario scenario = new Scenario(this.environments.iterator().next());
        cl.setDelegate(scenario);
        cl.setDirective(Closure.DELEGATE_ONLY);
        this.scenarioClosure = cl;
        this.scenario = scenario;
    }

    public void runScenario() {
        scenarioClosure.call(scenario);
    }
    
    @Override
    public String toString() {
        return "ArquillianEnvironment [environments=" + environments
                + ", scenario=" + scenario + "]";
    }
}
