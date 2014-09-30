package org.arquillian.script.api;

public class ContainerController {

    private Container container;
    
    public ContainerController(Container container) {
        this.container = container;
    }
    
    public void start() {
        System.out.println("Starting.. " + container.getName());
    }

    public void stop() {
        System.out.println("Stopping.. " + container.getName());
    }
    
    public void deploy(Deployment deployment) {
        System.out.println("Deploying.. " + deployment.getName());
    }

    public void undeploy(Deployment deployment) {
        System.out.println("Undeploying.. " + deployment.getName());
    }
}
