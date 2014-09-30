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
import org.junit.Test;

public class BasicEnvTestCase {

    @Test
    public void shouldBeAbleToAddEnvironment() throws Exception {
        ArquillianEnvironment env = run(
                "environment { }");

        Assert.assertEquals(1, env.environments.size());
    }

    @Test
    public void shouldBeAbleToAddNamedAndTypeViaItToContainer() throws Exception {
        ArquillianEnvironment env = run(
                "environment {\n" +
                "  container('test') { \n" +
                "    type 'c'\n" +
                "  }\n" +
                "}");

        Assert.assertEquals(1, env.environments.size());
        Assert.assertEquals(1, env.environments.iterator().next().containers.size());
        Container container = env.environments.iterator().next().containers.iterator().next();
        Assert.assertEquals("test", container.getName());
        Assert.assertEquals("c", container.getType());
    }

    @Test
    public void shouldBeAbleToAddNamedAndTypeViaNamedTypeToContainer() throws Exception {
        ArquillianEnvironment env = run(
                "environment {\n" +
                "  container('test') { \n" +
                "    type 'c'\n" +
                "  }\n" +
                "}");

        Assert.assertEquals(1, env.environments.size());
        Assert.assertEquals(1, env.environments.iterator().next().containers.size());
        Container container = env.environments.iterator().next().containers.iterator().next();
        Assert.assertEquals("test", container.getName());
        Assert.assertEquals("c", container.getType());
    }

    @Test
    public void shouldBeAbleToAddMultipleContainers() throws Exception {
        ArquillianEnvironment env = run(
                "environment {\n" +
                "  container 'test-1', { \n" +
                "  }\n" +
                "  container 'test-2', { \n" +
                "  }\n" +
                "}");

        Assert.assertEquals(1, env.environments.size());
        Assert.assertEquals(2, env.environments.iterator().next().containers.size());
    }

    @Test
    public void shouldBeAbleToAddMultipleDynamicContainers() throws Exception {
        ArquillianEnvironment env = run(
                "containers = ['test-1', 'test-2']\n" +
                "environment {\n" +
                "  containers.each { \n" +
                "     container(it) { \n" +
                "       type 'Remote' \n" +
                "     }\n" +
                "  }\n" +
                "}");

        Assert.assertEquals(1, env.environments.size());
        Assert.assertEquals(2, env.environments.iterator().next().containers.size());
    }
    
    @Test
    public void shouldBeAbleToLookupContainerInScenariooBasedOnName() throws Exception {
        ArquillianEnvironment env = run(
                "environment {\n" +
                "  container('test') {  \n" +
                "    type 'c'\n" +
                "  }\n" +
                "} \n" +
                "scenario { \n" +
                "  test.start() \n" +
                "}");
        
        env.runScenario();
    }

    private ArquillianEnvironment run(String script) throws Exception {

        System.out.println("---------------");
        System.out.println(script);
        System.out.println("===============");
        Map<String, String> scripts = new HashMap<>();
        scripts.put("test-1", script);
        
        ScriptRunner runner = new ScriptRunner(new StringResourceConnector(scripts));

        ArquillianEnvironment env = runner.runScript(script);
        System.out.println(env);
        System.out.println("---------------\n");
        return env;
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