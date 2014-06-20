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
import org.arquillian.script.impl.ScriptRunner;
import org.junit.Assert;
import org.junit.Test;

public class BasicEnvTestCase {

    @Test
    public void test() throws Exception {
        ArquillianEnvironment env = run("arquillian.name = 'a'");
        Assert.assertEquals("a", env.name);
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