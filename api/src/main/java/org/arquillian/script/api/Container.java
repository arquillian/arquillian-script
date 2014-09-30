package org.arquillian.script.api;

public class Container {

    private String name;

    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public void type(String type) {
        this.type = type;
    }
        
    @Override
    public String toString() {
        return "Container [name=" + name + ", type=" + type + "]";
    }
}
