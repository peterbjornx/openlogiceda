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

import nl.peterbjornx.openlogiceda.model.Circuit;
import nl.peterbjornx.openlogiceda.model.Net;
import nl.peterbjornx.openlogiceda.model.Node;
import nl.peterbjornx.openlogiceda.model.Value;
import nl.peterbjornx.openlogiceda.util.SimulationException;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * @author Peter Bosch
 */
public class Simulator {

    private Set<Process> pendingProcesses = new HashSet<>();
    private Set<Net> dirtyNets = new HashSet<>();
    private PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    private long now = 0;

    /**
     * Sets a nodes value
     * @param node The node to update
     * @param value The value to set
     * @param delay The delay before the new value propagates
     */
    public void setNode(Node node, Value value, long delay) throws SimulationException {
        if ( delay == 0 )
            throw new SimulationException("cannot simulate an instantaneous effect");
        addEvent(delay, sim -> {setNode(node,value);});
    }

    /**
     * Adds an event to run in a given amount of time
     * @param delay The amount of time to wait before running the process
     * @param process The process to run after the delay
     */
    public void addEvent(long delay, Process process) {
        eventQueue.add( new Event( now+delay, process ));
    }

    /**
     * Actually performs the update of the node
     * @param node The node to update
     * @param value Its new value
     */
    private void setNode(Node node, Value value) {
        if ( node.getValue() == value )
            return;
        node.setValue(value);
        dirtyNets.add(node.getNet());
    }

    /**
     * Run the processes pending for this timestep
     * @throws SimulationException Something went wrong during simulation
     */
    private void runProcesses() throws SimulationException {
        for ( Process p : pendingProcesses )
            p.process(this);
        pendingProcesses.clear();
    }

    /**
     * Calculate all new net values and clear dirty net list
     */
    private void propagateNets() {
        for ( Net n : dirtyNets ) {
            propagateNet( n );
        }
        dirtyNets.clear();
    }

    /**
     * Calculate the new value for a net
     * @param net The net we are to process
     */
    private void propagateNet(Net net) {
        List<Node> nodes = net.getNodes();
        Value nv = Value.UNDEFINED;

        /* For each of the nodes on this net, combine into effective value */
        for ( Node n : nodes )
            nv = nv.combine(n.getValue());

        /* If it changed, schedule the processes associated with it */
        if ( nv != net.getValue() ) {
            net.setValue(nv);
            for ( Node n : nodes )
                pendingProcesses.addAll( n.getProcesses() );
            pendingProcesses.addAll( net.getProcesses() );
        }
    }

    /**
     * Execute the next tick's worth of events
     * @return Whether there were any events to process
     * @throws SimulationException Something went wrong while simulating
     */
    private boolean doEventStep() throws SimulationException {
        if ( eventQueue.isEmpty() )
            return false;
        now = eventQueue.peek().timestamp;
        while ( eventQueue.isEmpty() ) {
            if ( eventQueue.peek().timestamp != now )
                break;
            eventQueue.poll().process.process(this);
        }
        return true;
    }

    /**
     * Execute a timestep
     * @return Whether there was still a change to be evaluated
     * @throws SimulationException Something went wrong while simulating
     */
    public boolean step() throws SimulationException {
        if (!doEventStep())
            return false;
        propagateNets();
        runProcesses();
        return true;
    }

    /**
     * Gets the current simulated time
     */
    public long getNow() {
        return now;
    }

    private class Event implements Comparable {
        Process process;
        long    timestamp;

        Event(long t, Process p) {
            process = p;
            timestamp = t;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Event ) {
                Event e = (Event) o;
                return (int)(e.timestamp - timestamp);//TODO Proper bounding
            }
            throw new RuntimeException("comparing Event to somehing that isn't an Event");
        }
    }

}
