package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperMbedForXmlTest;

public class PinWriteValueSensorTest {
    private final HelperMbedForXmlTest h = new HelperMbedForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfPinValueSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "PinWriteValueSensor [ANALOG, S2, NumConst [1]], PinWriteValueSensor [DIGITAL, S4, NumConst [1]]]]]";

        String result = this.h.generateTransformerString("/action/write_value_to_pin.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/write_value_to_pin.xml");
    }

}
