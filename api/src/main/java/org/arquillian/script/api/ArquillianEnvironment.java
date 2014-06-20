package org.arquillian.script.api;

import groovy.lang.Closure;

import java.util.HashSet;
import java.util.Set;

public class ArquillianEnvironment {

    public String name;

    public Set<Environment> environments = new HashSet<>();

    public void name(String name) {
        this.name = name;
    }

    public void environment(Closure<Environment> cl) {
        Environment environment = new Environment();
        cl.setDelegate(environment);
        cl.setDirective(Closure.DELEGATE_ONLY);
        cl.call();
        this.environments.add(environment);
    }

    @Override
    public String toString() {
        return "ArquillianEnvironment [name=" + name + ", environments="
                + environments + "]";
    }
}
