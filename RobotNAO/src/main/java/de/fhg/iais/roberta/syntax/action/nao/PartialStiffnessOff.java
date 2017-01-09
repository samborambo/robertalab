package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.BodyPart;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class represents the <b>naoActions_PartialStiffnessOff</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for removing the stiffness from one part of the robots body.<br/>
 * <br/>
 * The client must provide the {@link BodyPart} (body part in which the motors are released).
 */
public final class PartialStiffnessOff<V> extends Action<V> {

    private final BodyPart bodyPart;

    @Override
    public String toString() {
        return "PartialStiffnessOff [" + this.bodyPart + "]";
    }

    private PartialStiffnessOff(BodyPart bodyPart, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PARTIAL_STIFFNESS_OFF"), properties, comment);
        Assert.notNull(bodyPart, "Missing body part in PartialStiffnessOff block!");
        this.bodyPart = bodyPart;
        setReadOnly();
    }

    /**
     * Creates instance of {@link PartialStiffnessOff}. This instance is read only and can not be modified.
     *
     * @param part {@link BodyPart} on which the stiffness is turned off,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PartialStiffnessOff}
     */
    private static <V> PartialStiffnessOff<V> make(BodyPart bodyPart, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PartialStiffnessOff<V>(bodyPart, properties, comment);
    }

    public BodyPart getBodyPart() {
        return this.bodyPart;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitPartialStiffnessOff(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);

        String bodyPart = helper.extractField(fields, BlocklyConstants.DIRECTION);

        return PartialStiffnessOff.make(BodyPart.get(bodyPart), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.bodyPart.toString());

        return jaxbDestination;
    }
}