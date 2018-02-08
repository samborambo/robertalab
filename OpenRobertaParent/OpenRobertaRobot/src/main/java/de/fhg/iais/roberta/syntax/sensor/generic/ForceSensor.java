package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.mode.sensor.ForceSensorMode;
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
 * This class represents the <b>robSensors_force_getSample</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link ForceSensorMode}. See enum {@link ForceSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(ForceSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ForceSensor<V> extends ExternalSensor<V> {

    private ForceSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("FORCE_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link ForceSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link ForceSensorMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link ForceSensor}
     */
    public static <V> ForceSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ForceSensor<>(sensorMetaDataBean, properties, comment);
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstSensorsVisitor<V>) visitor).visitForceSensor(this);
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
        sensorMetaDataBean = extractSensorPortAndMode(block, helper, helper.getModeFactory()::getForceSensorMode);
        return ForceSensor.make(sensorMetaDataBean, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        String fieldValue = getPort().getPortNumber();
        if ( getMode().toString().equals("VALUE") ) {
            Mutation mutation = new Mutation();
            mutation.setMode(getMode().toString());
            jaxbDestination.setMutation(mutation);
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE, getMode().toString());
        }
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        return jaxbDestination;
    }

}