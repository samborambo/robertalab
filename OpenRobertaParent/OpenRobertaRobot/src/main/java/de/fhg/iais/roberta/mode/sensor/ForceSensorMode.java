package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IForceSensorMode;

public enum ForceSensorMode implements IForceSensorMode {
    DEFAULT(), LEFT( "left" ), RIGHT( "right" ), VALUE ( "value" );
    private final String[] values;

    private ForceSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}