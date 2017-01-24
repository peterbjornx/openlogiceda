package nl.peterbjornx.openlogiceda.sim;/*
Part of OpenLogicEDA
Copyright (C) 2017 Peter Bosch

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import nl.peterbjornx.openlogiceda.model.Node;
import nl.peterbjornx.openlogiceda.model.Value;
import nl.peterbjornx.openlogiceda.util.SimulationException;

/**
 * Implements utility routines to ease working with Value objects
 * @author Peter Bosch
 */
public class ValueTools {

    /**
     * Generates a strongly driven Value corresponding to state
     */
    public static Value drive(boolean state) {
        return state ? Value.HIGH : Value.LOW;
    }

    public static Value drivenSequential(BooleanEquation eqn, Value... inputs ) {
        boolean[] bvalues = new boolean[ inputs.length ];
        for ( int i = 0; i < inputs.length; i++ ) {
            if ( !inputs[i].valid )
                return Value.UNDEFINED;
            bvalues[i] = inputs[i].truth;
        }
        return drive(eqn.compute( bvalues ));
    }

    public static boolean isValid(Value... inputs){
        for (Value input : inputs) {
            if (!input.valid)
                return false;
        }
        return true;
    }

    public static boolean isValid(Node[] inputs){
        for (Node input : inputs) {
            if (!input.getNet().getValue().valid)
                return false;
        }
        return true;
    }

    public static long decode(Value... inputs ) throws SimulationException {
        long value = 0;
        if ( inputs.length > 63)
            throw new SimulationException("Can only simulate up to 63 bit words");
        for ( int i = 0; i < inputs.length; i++ ) {
            if ( !inputs[i].valid )
                throw new SimulationException("Tried to decode invalid value");
            if ( inputs[i].truth )
                value |= 1L << i;
        }
        return value;
    }

    public static long decode(Node[] inputs) throws SimulationException {
        long value = 0;
        if ( inputs.length > 63)
            throw new SimulationException("Can only simulate up to 63 bit words");
        for ( int i = 0; i < inputs.length; i++ ) {
            if ( !inputs[i].getNet().getValue().valid )
                throw new SimulationException("Tried to decode invalid value");
            if ( inputs[i].getNet().getValue().truth )
                value |= 1L << i;
        }
        return value;
    }

    public static void highZ(Simulator sim, Node[] nodes, long delay) throws SimulationException {
        if ( nodes.length > 63)
            throw new SimulationException("Can only simulate up to 63 bit words");
        for (Node node : nodes)
            sim.setNode(node, Value.HIGH_Z,
                    delay);
    }

    public static void undefined(Simulator sim, Node[] nodes, long delay) throws SimulationException {
        if ( nodes.length > 63)
            throw new SimulationException("Can only simulate up to 63 bit words");
        for (Node node : nodes)
            sim.setNode(node, Value.UNDEFINED, delay);
    }

    public static void drive(Simulator sim, Node[] nodes, long value, long delay) throws SimulationException {
        if ( nodes.length > 63)
            throw new SimulationException("Can only simulate up to 63 bit words");
        for ( int i = 0; i < nodes.length; i++ )
            sim.setNode(nodes[i],(value & (1L << i)) != 0 ?
                    Value.HIGH : Value.LOW,
                    delay);
    }
}
