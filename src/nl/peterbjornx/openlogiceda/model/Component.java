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

import nl.peterbjornx.openlogiceda.sim.Simulator;
import nl.peterbjornx.openlogiceda.util.SimulationException;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Bosch
 */
public abstract class Component
{

    /**
     * The nodes comprising this component
     */
    protected List<Node> nodes = new LinkedList<>();

    /**
     * The name of the component
     */
    private String name;

    /**
     * The circuit this component is on
     */
    Circuit circuit;

    /**
     * Create a new component
     * @param name The name of the component
     */
    protected Component( String name ) {
        this.name = name;
    }

    /**
     * Gets the name of this component
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the circuit this component is on
     * @return null when this is the root component.
     */
    public Circuit getCircuit() {
        return circuit;
    }

    /**
     * Produces a path like string
     */
    @Override
    public String toString() {
        if ( circuit != null )
            return circuit.toString()+"."+name;
        return name;
    }

    /**
     * Gets a node by its name
     */
    public Node getNodeByName(String name) throws SimulationException {
        for (Node n : nodes)
            if (n.getName().equals(name))
                return n;
        throw new SimulationException("No such node");
    }

    /**
     * This function is called by the simulator before starting the simulation
     * @param simulator The simulator context
     * @throws SimulationException if something went wrong during the simulation.
     */
    public void beginSimulation(Simulator simulator) throws SimulationException {

    }
}
