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

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a pin or any other kind of terminal in the circuit,
 * is connected to a net.
 * @author Peter Bosch
 */
public class Node {

    /**
     * The name of this node
     * This name is local to the component it is attached to
     */
    private String name;

    /**
     * The net this node is connected to.
     */
    Net net = null;

    /**
     * The component this node belongs to.
     */
    Component component;

    /**
     * The value of this node
     */
    private Value value = Value.UNDEFINED;

    /**
     * The processes to run when this node changes
     */
    private List<Process> processes = new LinkedList<>();

    /**
     * Creates a new Node
     */
    public Node(Component component, String name) {
        this.component = component;
        this.name = name;
    }

    /**
     * Gets the component this node is attached to
     */
    public Component getComponent() {
        return component;
    }

    /**
     * Gets the value of this node
     */
    public Value getValue() {
        return value;
    }

    /**
     * Gets the net this node is connected to
     */
    public Net getNet() {
        return net;
    }

    /**
     * Produces a path like string
     */
    @Override
    public String toString() {
        if ( component != null )
            return component.toString()+"."+name;
        return name;
    }

    /**
     * Sets the new value of this node.
     * Only to be used by the simulator
     */
    public void setValue(Value value) {
        this.value = value;
    }

    /**
     * The list of processes to run when the net this node is on changes
     */
    public List<Process> getProcesses() {
        return processes;
    }

    /**
     * Register a process to be called when the state of this node changes.
     */
    public void addProcess( Process process ) {
        processes.add(process);
    }

    public String getName() {
        return name;
    }
}
