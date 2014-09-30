package org.arquillian.script.api;

public class Scenario {

    private Environment environment;

    public Scenario(Environment environment) {
        this.environment = environment;
    }
    
    public Object methodMissing(String name, Object args) {
        Container container = findContainer(name);
        if(container != null) {
            return new ContainerController(container);
        }

        return findDeployment(name);
    }

    public Object propertyMissing(String name) {
        System.out.println("Get Property " + name);
        Container container = findContainer(name);
        if(container != null) {
            return new ContainerController(container);
        }
        return findDeployment(name);
    }
    
    public void propertyMissing(String name, Object value) {
        System.out.println("Set Property " + name);
    }

    private Container findContainer(String name) {
        for(Container container : environment.containers) {
            if(name.equalsIgnoreCase(container.getName())) {
                return container;
            }
        }
        return null;
    }

    private Deployment findDeployment(String name) {
        for (Deployment deployment : environment.deployments) {
            if (name.equalsIgnoreCase(deployment.getName())) {
                return deployment;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Scenario []";
    }
   
}
