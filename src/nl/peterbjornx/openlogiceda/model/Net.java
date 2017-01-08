package nl.peterbjornx.openlogiceda.model;/*
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

import nl.peterbjornx.openlogiceda.sim.Process;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a group of interconnected nodes.
 * @author Peter Bosch
 */
public class Net {

    /**
     * The name of this net
     */
    private String name;

    /**
     * The nodes connected to this net.
     */
    List<Node> nodes = new LinkedList<>();

    /**
     * The circuit this net is part of.
     */
    Circuit circuit;

    /**
     * The net value.
     */
    private Value value = Value.UNDEFINED;

    /**
     * The kind of change the net had
     */
    private ValueChange change = ValueChange.UNDEFINED;

    /**
     * The processes registered to run when this net changes state.
     */
    private List<Process> processes = new LinkedList<>();

    /**
     * Gets the effective value of this net.
     * This is only valid when running a simulation.
     */
    public Value getValue() {
        return value;
    }

    /**
     * Sets the value of this net.
     * @note Should only be called by the simulator core
     * @param value The new effective value of this net.
     */
    public void setValue(Value value) {
        if (!value.valid || !this.value.valid) {
            if ( value.combine(this.value) == Value.CONFLICT)
                change = ValueChange.CONFLICT;
            else
                change = ValueChange.UNDEFINED;
        } else if ( value.truth == this.value.truth )
            change = ValueChange.STEADY;
        else if ( value.truth )
            change = ValueChange.RISE;
        else
            change = ValueChange.FALL;
        this.value = value;
    }

    /**
     * Get the circuit this net is part of.
     */
    public Circuit getCircuit() {
        return circuit;
    }

    /**
     * Get the nodes connected to this net
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Gets the name of this net.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return circuit.toString() + "." + name;
    }

    /**
     * Gets the processes to run when this net changes
     */
    public List<Process> getProcesses() {
        return processes;
    }
}
