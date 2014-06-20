package org.arquillian.script.tests;

import groovy.util.ResourceConnector;
import groovy.util.ResourceException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;

import org.arquillian.script.api.ArquillianEnvironment;
import org.arquillian.script.api.Container;
import org.arquillian.script.impl.ScriptRunner;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class BasicEnvTestCase {

    @Test
    public void shouldBeAbleToSetName() throws Exception {
        ArquillianEnvironment env = run("arquillian.name = 'a'");
        Assert.assertEquals("a", env.name);
    }
    
    @Test
    public void shouldBeAbleToAddEnvironment() throws Exception {
        ArquillianEnvironment env = run(
                "arquillian.environment { }");

        Assert.assertEquals(1, env.environments.size());
    }

    @Test
    public void shouldBeAbleToAddNamedContainer() throws Exception {
        ArquillianEnvironment env = run(
                "arquillian.environment {\n " +
                "  container 'test', { \n" +
                "  }" +
                "}");

        Assert.assertEquals(1, env.environments.size());
        Assert.assertEquals(1, env.environments.iterator().next().containers.size());
        Container container = env.environments.iterator().next().containers.iterator().next();
        Assert.assertEquals("test", container.name);
    }

    @Test
    public void shouldBeAbleToAddMultipleContainers() throws Exception {
        ArquillianEnvironment env = run(
                "arquillian.environment {\n " +
                "  container 'test-1', { \n" +
                "  }\n" +
                "  container 'test-2', { \n" +
                "  }" +
                "}");

        Assert.assertEquals(1, env.environments.size());
        Assert.assertEquals(2, env.environments.iterator().next().containers.size());
    }

    private ArquillianEnvironment run(String script) throws Exception {
        Map<String, String> scripts = new HashMap<>();
        scripts.put("test-1", script);
        
        ScriptRunner runner = new ScriptRunner(new StringResourceConnector(scripts));
        return runner.run("test-1");
    }
    
    private static class StringResourceConnector implements ResourceConnector {

        private Map<String, String> scripts;

        public StringResourceConnector(Map<String, String> scripts) {
            this.scripts = scripts;
        }
        
        @Override
        public URLConnection getResourceConnection(final String name)
                throws ResourceException {
            if(scripts.containsKey(name)) {
                try {
                    return new URL(null, "string://" + name, new URLStreamHandler() {
                        
                        @Override
                        protected URLConnection openConnection(URL u) throws IOException {
                            return new URLConnection(u) {
                                
                                @Override
                                public void connect() throws IOException {
                                }
                                @Override
                                public InputStream getInputStream() throws IOException {
                                    return new ByteArrayInputStream(scripts.get(name).getBytes());
                                }
                            };
                        }
                    }).openConnection();
                } catch (Exception e) {
                    throw new ResourceException(e);
                }
            }
            return null;
        }
    }
}