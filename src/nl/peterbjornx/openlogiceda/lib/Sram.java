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
import nl.peterbjornx.openlogiceda.sim.Process;
import nl.peterbjornx.openlogiceda.sim.Simulator;
import nl.peterbjornx.openlogiceda.sim.ValueTools;
import nl.peterbjornx.openlogiceda.util.SimulationException;

import javax.xml.bind.ValidationEvent;

/**
 * @author Peter Bosch
 */
public class Sram extends Component {

    private long addressLatch;
    private long dataValue;
    private final Node address[];
    private final Node data[];
    private final Node we;
    private final Node ce;
    private final Node oe;
    private Process addressProcess;
    private Process ceProcess;
    private Process weProcess;
    private Process oeProcess;
    private long delay;

    /**
     * Create a new component
     *
     * @param name The name of the component
     */
    protected Sram(String name, int addressbits) {
        super(name);
        address = new Node[addressbits];
        data = new Node[8];
        for ( int i = 0; i < 8; i++ ) {
            data[i] = new Node(this, "D" + i);
            nodes.add(data[i]);
        }
        for ( int i = 0; i < addressbits; i++ ) {
            address[i] = new Node(this, "A" + i);
            nodes.add(address[i]);
            address[i].addProcess(addressProcess);
        }
        we = new Node(this, "WE");
        ce = new Node(this, "CE");
        oe = new Node(this, "OE");
        nodes.add(we);
        nodes.add(ce);
        nodes.add(oe);
        addressProcess = sim -> {
            Value[] val = new Value[addressbits];
     //       if ()
            for ( int i = 0; i < addressbits; i++ )
                val[i] = address[i].getNet().getValue();
            addressLatch = ValueTools.decode(val);
        };
        oeProcess = sim -> {
            Value[] val = new Value[addressbits];
            for ( int i = 0; i < addressbits; i++ )
                val[i] = address[i].getNet().getValue();
            addressLatch = ValueTools.decode(val);
        };

    }
}
