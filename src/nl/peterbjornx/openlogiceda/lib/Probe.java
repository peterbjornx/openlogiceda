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
import nl.peterbjornx.openlogiceda.sim.SignalHistory;

/**
 * @author Peter Bosch
 */
public class Probe extends Component {
    private Node input;
    private SignalHistory history;

    /**
     * Create a new component
     *
     */
    public Probe() {
        super("probe" );
        input = new Node( this, "IN" );
        input.addProcess(sim -> {
                System.out.println("[" + sim.getNow()+ "]"+input.getNet()+
                        " change of state "+input.getNet().getChange() +
                        " to "+input.getNet().getValue());
                history.addChange(sim.getNow(), input.getNet().getValue());
        });
        this.nodes.add(input);
        history = new SignalHistory();

    }

    /**
     * Gets the history of the net this probe is attached to
     */
    public SignalHistory getHistory() {
        return history;
    }

    /**
     * Gets the input pin of this probe
     */
    public Node getInput() {
        return input;
    }
}
