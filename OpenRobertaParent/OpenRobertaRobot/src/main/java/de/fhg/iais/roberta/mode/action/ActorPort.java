package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.IActorPort;

public class ActorPort implements IActorPort, Comparable<ActorPort> {
    //NO_PORT, A( "A", "MA" ), B( "B", "MB" ), C( "C", "MC" ), D( "D", "MD" ), LEFT( "left" ), RIGHT( "right" );

    private final String[] values;

    public ActorPort(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

    @Override
    public String getPortNumber() {
        return this.values[0];
    }

    @Override
    public String getPortName() {
        return this.values[1];
    }

    @Override
    public int compareTo(ActorPort other) {
        return this.values[1].compareTo(other.values[1]);
    }

    @Override
    public String toString() {
        return this.values[1];
    }

}