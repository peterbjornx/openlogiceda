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
import nl.peterbjornx.openlogiceda.model.Value;
import nl.peterbjornx.openlogiceda.sim.BooleanEquation;
import nl.peterbjornx.openlogiceda.sim.Process;
import nl.peterbjornx.openlogiceda.sim.Simulator;
import nl.peterbjornx.openlogiceda.sim.ValueTools;
import nl.peterbjornx.openlogiceda.util.SimulationException;

/**
 * @author Peter Bosch
 */
public class SequentialComponent extends Component {
    private final BooleanEquation eqn;
    private final Node inputs[];
    private final Node output;
    private Process process;
    private long delay;

    /**
     * Create a new component
     *
     * @param name The name of the component
     */
    public SequentialComponent(String name, int count, long delay, BooleanEquation eqn) {
        super(name);
        this.eqn = eqn;
        this.inputs = new Node[count];
        process = this::process;
        for ( int i = 0; i < count; i++ ) {
            inputs[i] = new Node(this, "I" + i);
            inputs[i].addProcess(process);
            nodes.add(inputs[i]);
        }
        this.output = new Node(this, "O" );
        this.delay = delay;
        nodes.add(output);
    }

    private void process(Simulator sim) throws SimulationException {
        Value[] vals = new Value[inputs.length];
        for ( int i = 0; i < inputs.length; i++ )
            vals[i] = inputs[i].getNet().getValue();
        sim.setNode( output, ValueTools.drivenSequential(eqn, vals), delay );
    }

    public Node getInput(int i) {
        return inputs[i];
    }

    public Node getOutput() {
        return output;
    }
}
