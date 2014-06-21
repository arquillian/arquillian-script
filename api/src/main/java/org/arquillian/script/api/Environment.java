package org.arquillian.script.api;

import groovy.lang.Closure;

import java.util.HashSet;
import java.util.Set;

public class Environment {

    public Set<Container> containers = new HashSet<>();

    public void container(String name, Closure<Container> cl) {
        Container container = new Container();
        container.name = name;
        cl.setDelegate(container);
        cl.setDirective(Closure.DELEGATE_FIRST);
        cl.call(container);
        containers.add(container);
    }

    @Override
    public String toString() {
        return "Environment [containers=" + containers + "]";
    }
}
