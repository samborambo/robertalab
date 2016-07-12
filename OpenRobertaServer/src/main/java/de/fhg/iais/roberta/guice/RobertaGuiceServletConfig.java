package de.fhg.iais.roberta.guice;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;

public class RobertaGuiceServletConfig extends GuiceServletContextListener {
    private Injector injector;
    private final Properties openRobertaProperties;
    private final Map<String, IRobotFactory> robotPluginMap;
    private final RobotCommunicator robotCommunicator;

    public RobertaGuiceServletConfig(Properties openRobertaProperties, Map<String, IRobotFactory> robotPluginMap, RobotCommunicator robotCommunicator) {
        this.openRobertaProperties = openRobertaProperties;
        this.robotPluginMap = robotPluginMap;
        this.robotCommunicator = robotCommunicator;
    }

    @Override
    protected Injector getInjector() {

        JerseyServletModule jerseyServletModule = new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                // configure at least one JAX-RS resource or the server won't start.
                install(new RobertaGuiceModule(openRobertaProperties, robotPluginMap, robotCommunicator));
                Map<String, String> initParams = new HashMap<String, String>();
                // initParams.put("com.sun.jersey.config.feature.Trace", "true");
                initParams.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
                String packages = "" //
                    + "de.fhg.iais.roberta.javaServer.restServices.all,"
                    + "de.fhg.iais.roberta.javaServer.restServices.robot,"
                    + "de.fhg.iais.roberta.javaServer.provider";
                initParams.put("com.sun.jersey.config.property.packages", packages);
                serve("/*").with(GuiceContainer.class, initParams);
            }
        };
        this.injector = Guice.createInjector(jerseyServletModule);
        return this.injector;
    }

    public Injector getCreatedInjector() {
        return this.injector;
    }
}
