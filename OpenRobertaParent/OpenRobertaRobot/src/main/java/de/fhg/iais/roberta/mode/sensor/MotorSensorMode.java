package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IForceSensorMode;

public enum MotorSensorMode implements IForceSensorMode {
    DEFAULT(), CURRENT ( "current" );
    private final String[] values;

    private MotorSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}