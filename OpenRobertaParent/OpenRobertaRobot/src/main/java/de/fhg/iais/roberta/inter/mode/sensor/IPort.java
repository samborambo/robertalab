package de.fhg.iais.roberta.inter.mode.sensor;

import de.fhg.iais.roberta.inter.mode.general.IMode;

public interface IPort extends IMode {

    /**
     * @return the number of the port
     */
    public String getPortNumber();

    public String getPortName();

}
