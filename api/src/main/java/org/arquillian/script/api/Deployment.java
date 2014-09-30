package org.arquillian.script.api;

public class Deployment {

    private String name;

    private Object source;

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public void source(Object source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Deployment [name=" + name + ", source=" + source + ']';
    }
}
