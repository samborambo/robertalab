package de.fhg.iais.roberta.factory.mbed.calliope.calliope2017;

import java.util.Map;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.factory.mbed.calliope.AbstractFactory;
import de.fhg.iais.roberta.factory.mbed.calliope.CompilerWorkflow;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractFactory {

    Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:Calliopeports.properties"));
    Map<String, ActorPort> actorToPorts = IRobotFactory.getActorPortsFromProperties(Util1.loadProperties("classpath:Calliopeports.properties"));

    public Factory(RobertaProperties robertaProperties) {
        super(robertaProperties);
        this.calliopeProperties = Util1.loadProperties("classpath:Calliope2017.properties");
        this.name = this.calliopeProperties.getProperty("robot.name");
        this.robotPropertyNumber = robertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new CompilerWorkflow(
                robertaProperties.getTempDirForUserProjects(),
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"),
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.dir"));

        addBlockTypesFromProperties("Calliope2017.properties", this.calliopeProperties);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return getSensorPortValue(port, this.sensorToPorts);
    }

    @Override
    public IActorPort getActorPort(String port) {
        return getActorPortValue(port, this.actorToPorts);
    }
}
