package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.ConfigurationBlockType;
import de.fhg.iais.roberta.components.UsedConfigurationBlock;
import de.fhg.iais.roberta.components.arduino.ArduinoConfiguration;
import de.fhg.iais.roberta.mode.sensor.HumiditySensorMode;
import de.fhg.iais.roberta.mode.sensor.RfidSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ExternalLedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ExternalLedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOnAction;
import de.fhg.iais.roberta.syntax.check.hardware.arduino.arduino.UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.codegen.arduino.ArduinoVisitor;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitors.arduino.ArduinoAstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a
 * StringBuilder. <b>This representation is correct C code for Arduino.</b> <br>
 */
public class CppVisitor extends ArduinoVisitor implements ArduinoAstVisitor<Void> {
    private final boolean isTimerSensorUsed;

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private CppVisitor(ArduinoConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrases, int indentation) {
        super(phrases, indentation);
        UsedHardwareCollectorVisitor codePreprocessVisitor = new UsedHardwareCollectorVisitor(phrases, brickConfiguration);
        this.usedVars = codePreprocessVisitor.getVisitedVars();
        this.usedConfigurationBlocks = codePreprocessVisitor.getUsedConfigurationBlocks();
        //TODO: fix how the timer is detected for all robots
        this.isTimerSensorUsed = codePreprocessVisitor.isTimerSensorUsed();
        this.loopsLabels = codePreprocessVisitor.getloopsLabelContainer();
    }

    /**
     * factory method to generate C++ code from an AST.<br>
     *
     * @param brickConfiguration
     * @param programPhrases to generate the code from
     * @param withWrapping if false the generated code will be without the surrounding configuration code
     */
    public static String generate(ArduinoConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        CppVisitor astVisitor = new CppVisitor(brickConfiguration, programPhrases, withWrapping ? 1 : 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        //this.sb.append("lcd.setCursor(0," + showTextAction.getX() + ");");
        //nlIndent();
        //this.sb.append("lcd.print(\"");
        //showTextAction.visit(this);
        //this.sb.append("\");");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        return null;

    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("analogRead(_output_" + lightSensor.getPort().getPortNumber() + ")");
        //this.sb.append("analogRead(_output_" + lightSensor.getBlockName() + ")");
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> button) {
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("pulseIn(_echo_" + ultrasonicSensor.getPort().getPortNumber() + ", HIGH)*_signalToDistance");
        return null;
    }

    @Override
    public Void visitMoistureSensor(MoistureSensor<Void> moistureSensor) {
        this.sb.append("analogRead(_moisturePin_" + moistureSensor.getPort().getPortNumber() + ")");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("analogRead(_TMP36_" + temperatureSensor.getPort().getPortNumber() + ")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        this.sb.append("encoder");
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> potentiometer) {
        this.sb.append("analogRead(_output_" + potentiometer.getPort().getPortNumber() + ")*_sensingToVolts");
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        switch ( (HumiditySensorMode) humiditySensor.getMode() ) {
            case HUMIDITY:
                this.sb.append("_dht_" + humiditySensor.getPort().getPortNumber() + ".readHumidity()");
                break;
            case TEMPERATURE:
                this.sb.append("_dht_" + humiditySensor.getPort().getPortNumber() + ".readTemperature()");
                break;
            default:
                throw new DbcException("Invalide mode for Humidity Sensor!");
        }
        return null;
    }

    @Override
    public Void visitDropSensor(DropSensor<Void> dropSensor) {
        this.sb.append("analogRead(_S_" + dropSensor.getPort().getPortNumber() + ")");
        return null;
    }

    @Override
    public Void visitPulseSensor(PulseSensor<Void> pulseSensor) {
        this.sb.append("pulse");
        return null;
    }

    @Override
    public Void visitRfidSensor(RfidSensor<Void> rfidSensor) {
        switch ( (RfidSensorMode) rfidSensor.getMode() ) {
            case PRESENCE:
                this.sb.append("mfrc522.PICC_IsNewCardPresent()");
                break;
            case SERIAL:
                this.sb.append("mfrc522.PICC_ReadCardSerial()");
                break;
            default:
                throw new DbcException("Invalide mode for Humidity Sensor!");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.sb.append("irrecv" + infraredSensor.getPort().getPortNumber() + ".decode(&_results_" + infraredSensor.getPort().getPortNumber() + ")");
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        this.sb.append("digitalRead(_output_" + motionSensor.getPort().getPortNumber() + ") == HIGH");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        decrIndentation();
        mainTask.getVariables().visit(this);
        nlIndent();
        generateConfigurationVariables();
        if ( this.isTimerSensorUsed ) {
            nlIndent();
            this.sb.append("unsigned long __time = millis(); \n");
        }
        incrIndentation();
        generateUserDefinedMethods();
        this.sb.append("\n \n void setup() \n");
        this.sb.append("{");
        nlIndent();
        this.sb.append("Serial.begin(9600); ");
        nlIndent();
        this.generateConfigurationSetup();
        generateUsedVars();
        this.sb.append("\n}\n");
        this.sb.append("\n").append("void loop() \n");
        this.sb.append("{");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("#include <math.h> \n");
        //TODO: only include if needed:
        //if IR is used
        this.sb.append("#include <Irremote.h> \n");
        //if humidity is used
        this.sb.append("#include <DHT.h> \n");
        //if RFID is used
        this.sb.append("#include <SPI.h> \n");
        this.sb.append("#include <MFRC522.h> \n");
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            this.sb.append("\n}\n");
        }
    }

    private void generateConfigurationSetup() {
        for ( UsedConfigurationBlock usedConfigurationBlock : this.usedConfigurationBlocks ) {
            switch ( (ConfigurationBlockType) usedConfigurationBlock.getType() ) {
                case HUMIDITY:
                    this.sb.append("_dht_" + usedConfigurationBlock.getBlockName() + "();");
                    nlIndent();
                    break;
                case ULTRASONIC:
                    this.sb.append("pinMode(_trigger_" + usedConfigurationBlock.getBlockName() + ", OUTPUT);");
                    nlIndent();
                    this.sb.append("pinMode(_echo_" + usedConfigurationBlock.getBlockName() + ", INPUT);");
                    nlIndent();
                    break;
                case MOTION:
                    this.sb.append("pinMode(_output_" + usedConfigurationBlock.getBlockName() + ", INPUT);");
                    nlIndent();
                    break;
                case MOISTURE:
                    break;
                case INFRARED:
                    this.sb.append("pinMode(13, OUTPUT);");
                    nlIndent();
                    this.sb.append("irrecv" + usedConfigurationBlock.getBlockName() + ".enableIRIn();");
                    nlIndent();
                    break;
                case LIGHT:
                    break;
                case POTENTIOMETER:
                    break;
                case TEMPERATURE:
                    break;
                case ENCODER:
                    break;
                case DROP:
                    break;
                case PULSE:
                    break;
                case RFID:
                    this.sb.append("SPI.begin();");
                    nlIndent();
                    this.sb.append("_mfrc522_" + usedConfigurationBlock.getBlockName() + ".PCD_Init();");
                    nlIndent();
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.getType());
            }
        }
    }

    private void generateConfigurationVariables() {
        for ( UsedConfigurationBlock usedConfigurationBlock : this.usedConfigurationBlocks ) {
            String blockName = usedConfigurationBlock.getBlockName();
            switch ( (ConfigurationBlockType) usedConfigurationBlock.getType() ) {
                case HUMIDITY:
                    this.sb.append("#define DHTPIN" + blockName + " ").append(usedConfigurationBlock.getPins().get(0));
                    nlIndent();
                    this.sb.append("#define DHTTYPE DHT11");
                    nlIndent();
                    this.sb.append("DHT _dht_" + blockName + "(DHTPIN" + blockName + ", DHTTYPE);");
                    nlIndent();
                    break;
                case ULTRASONIC:
                    this.sb.append("int _trigger_" + blockName + " = ").append(usedConfigurationBlock.getPins().get(0)).append(";");
                    nlIndent();
                    this.sb.append("int _echo_" + blockName + " = ").append(usedConfigurationBlock.getPins().get(1)).append(";");
                    nlIndent();
                    this.sb.append(
                        "double _signalToDistance = 0.03432/2; //Nun berechnet man die Entfernung in Zentimetern. Man teilt zunächst die Zeit durch zwei (Weil man ja nur eine Strecke berechnen möchte und nicht die Strecke hin- und zurück). Den Wert multipliziert man mit der Schallgeschwindigkeit in der Einheit Zentimeter/Mikrosekunde und erhält dann den Wert in Zentimetern.");
                    nlIndent();
                    break;
                case MOISTURE:
                    this.sb.append("int _moisturePin_" + blockName + " = ").append(usedConfigurationBlock.getPins().get(0)).append(";");
                    nlIndent();
                    break;
                case INFRARED:
                    this.sb.append("int _RECV_PIN_" + blockName + " = ").append(usedConfigurationBlock.getPins().get(0)).append(";");
                    nlIndent();
                    this.sb.append("IRrecv(_RECV_PIN_" + blockName + ");");
                    nlIndent();
                    this.sb.append("decode_results _results_" + blockName + ";");
                    nlIndent();
                    break;
                case LIGHT:
                    this.sb.append("int _output_" + blockName + " = ").append(usedConfigurationBlock.getPins().get(0)).append(";");
                    nlIndent();
                    break;
                case MOTION:
                    this.sb.append("int _output_" + blockName + " = ").append(usedConfigurationBlock.getPins().get(0)).append(";");
                    nlIndent();
                    break;
                case POTENTIOMETER:
                    this.sb.append("int _output_" + blockName + " = ").append(usedConfigurationBlock.getPins().get(0)).append(";");
                    nlIndent();
                    this.sb.append("double _sensingToVolts = 5/1023;");
                    nlIndent();
                    break;
                case TEMPERATURE:
                    this.sb.append("int _TMP36_" + blockName + " = ").append(usedConfigurationBlock.getPins().get(0)).append(";");
                    nlIndent();
                    break;
                case ENCODER:
                    break;
                case DROP:
                    this.sb.append("int _S_" + blockName + " = ").append(usedConfigurationBlock.getPins().get(0)).append(";");
                    nlIndent();
                    break;
                case PULSE:
                    break;
                case RFID:
                    this.sb.append("#define SS_PIN_" + blockName + " " + usedConfigurationBlock.getPins().get(0));
                    nlIndent();
                    this.sb.append("#define RST_PIN_" + blockName + " " + usedConfigurationBlock.getPins().get(1));
                    nlIndent();
                    this.sb.append("MFRC522 _mfrc522_" + blockName + "(SS_PIN_" + blockName + ", RST_PIN_" + blockName + ");");
                    nlIndent();
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.getType());
            }
        }
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitExternalLedOnAction(ExternalLedOnAction<Void> externalLedOnAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitExternalLedOffAction(ExternalLedOffAction<Void> externalLedOffAction) {
        // TODO Auto-generated method stub
        return null;
    }
}
