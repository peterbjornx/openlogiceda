package nl.peterbjornx.openlogiceda.lib;/*
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

import nl.peterbjornx.openlogiceda.model.Component;
import nl.peterbjornx.openlogiceda.model.Node;
import nl.peterbjornx.openlogiceda.sim.Simulator;
import nl.peterbjornx.openlogiceda.sim.ValueTools;
import nl.peterbjornx.openlogiceda.util.SimulationException;

/**
 * @author Peter Bosch
 */
public class Clock extends Component {

    private final long periodH;
    private final long periodL;
    private final Node output;
    private boolean state = false;

    /**
     * Creates a clock source
     * @param name The name of the new clock
     * @param period The time between ticks
     */
    public Clock( String name, long period ) {
        super( name );
        this.periodH = period;
        this.periodL = period;
        this.output = new Node( this, "OUT" );
        nodes.add( output );
    }

    /**
     * Creates a clock source
     * @param name The name of the new clock
     * @param periodL The time the signal stays low
     * @param periodH The time the signal stays high
     */
    public Clock( String name, long periodH, long periodL ) {
        super( name );
        this.periodH = periodH;
        this.periodL = periodL;
        this.output = new Node( this, "OUT" );
        nodes.add( output );
    }

    @Override
    public void beginSimulation( Simulator sim ) throws SimulationException {
        super.beginSimulation(sim);
        sim.setNode( output, ValueTools.drive(state), 1 );
        sim.addEvent( periodL, this::processTick );
    }

    private void processTick( Simulator sim ) throws SimulationException {
        state = !state;
        sim.setNode( output, ValueTools.drive(state), 1 );
        sim.addEvent( state ? periodH : periodL, this::processTick );
    }

    public Node getOutput() {
        return output;
    }
}
