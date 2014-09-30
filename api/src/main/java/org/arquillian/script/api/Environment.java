package org.arquillian.script.api;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

import java.util.HashSet;
import java.util.Set;

public class Environment {

    public Set<Container> containers = new HashSet<>();

    public Set<Deployment> deployments = new HashSet<>();

    public void container(String name, @DelegatesTo(Container.class) Closure<Container> cl) {
        Container container = new Container();
        container.setName(name);
        cl.setDelegate(container);
        cl.setDirective(Closure.DELEGATE_FIRST);
        cl.call(container);
        containers.add(container);
    }

    public void deployment(String name, Closure<Deployment> cl) {
        Deployment deployment = new Deployment();
        deployment.setName(name);
        cl.setDelegate(deployment);
        cl.setDirective(Closure.DELEGATE_FIRST);
        cl.call(deployment);
        deployments.add(deployment);
    }

    @Override
    public String toString() {
        return "Environment [containers=" + containers + ", deployments=" + deployments + "]";
    }
}
