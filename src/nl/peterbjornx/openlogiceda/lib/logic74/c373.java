package nl.peterbjornx.openlogiceda.lib.logic74;/*
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
import nl.peterbjornx.openlogiceda.util.SimulationException;

import static nl.peterbjornx.openlogiceda.sim.ValueTools.*;

/**
 * @author Peter Bosch
 */
public class c373 extends Component {
    private final Node pinQ[];
    private final Node pinD[];
    private final Node le;
    private final Node oe;
    private final Process dataProcess;
    private final Process leProcess;
    private final Process oeProcess;
    private final Process driveProcess;
    private final Process inputProcess;
    private long value = 0L;
    private boolean wasSet = false;
    private Value leValue;
    private Value oeValue;

    public c373() {
        super("74LS373");
        pinQ = new Node[8];
        pinD = new Node[8];
        le = new Node(this,"LE");
        oe = new Node(this,"OE");
        driveProcess = this::driveOutput;
        inputProcess = this::loadInput;
        dataProcess = sim -> {
            sim.addEvent(12000,inputProcess);
        };
        leProcess = sim -> {
            Value v = le.getNet().getValue();
            if ( leValue.truth != v.truth ) {
                leValue = v;
                sim.addEvent(19000,inputProcess);
            } else
                leValue = v;
        };
        oeProcess = sim -> {
            Value v = oe.getNet().getValue();
            if ( oeValue.truth != v.truth ) {
                oeValue = v;
                sim.addEvent(20000,driveProcess);
            } else
                oeValue = v;
        };
        for ( int i = 0; i < 8; i++ ) {
            pinQ[i] = new Node(this,"Q"+i);
            pinD[i] = new Node(this,"D"+i);
            pinD[i].addProcess(dataProcess);
            nodes.add(pinQ[i]);
            nodes.add(pinD[i]);
        }
        oe.addProcess(oeProcess);
        le.addProcess(leProcess);
        nodes.add(oe);
        nodes.add(le);
    }

    private void loadInput(Simulator sim) throws SimulationException {
        if (!leValue.truth)
            return;
        if (!isValid(pinD))
            wasSet = false;
        else {
            value = decode(pinD);
            wasSet = true;
        }
        driveOutput(sim);
    }

    private void driveOutput(Simulator sim) throws SimulationException {
        if ((!oeValue.valid)||(!leValue.valid)||(!wasSet))
            undefined(sim,pinQ,1L);
        else if (!oeValue.truth)
            drive(sim,pinQ,value,1L);
        else
            highZ(sim,pinQ,1L);
    }
}
