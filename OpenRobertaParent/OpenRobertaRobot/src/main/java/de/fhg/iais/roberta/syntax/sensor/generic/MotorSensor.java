package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * This class represents the <b>robSensors_motor_getSample</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link MotorSensorMode}. See enum {@link MotorSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(MotorSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class MotorSensor<V> extends ExternalSensor<V> {

    private MotorSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("MOTOR_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link ForceSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link MotorSensorMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link MotorSensor}
     */
    public static <V> MotorSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MotorSensor<>(sensorMetaDataBean, properties, comment);
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstSensorsVisitor<V>) visitor).visitMotorSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        IRobotFactory factory = helper.getModeFactory();
        SensorMetaDataBean sensorMetaDataBean;
        sensorMetaDataBean = extractSensorPortAndMode(block, helper, helper.getModeFactory()::getMotorSensorMode);
        return ForceSensor.make(sensorMetaDataBean, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        String fieldValue = getPort().getPortNumber();
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        return jaxbDestination;
    }

}