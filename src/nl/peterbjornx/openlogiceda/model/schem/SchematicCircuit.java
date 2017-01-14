package nl.peterbjornx.openlogiceda.model.schem;/*
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

import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Bosch
 */
public class SchematicCircuit {

    private List<SchematicNet> nets = new LinkedList<>();
    private List<SchematicNode> nodes;

    private void join(List<SchematicNode> nodes){
        SchematicNet net = null;
        for (SchematicNode node : nodes ) {
            if (net == null) {
                net = node.getNet();
                if (net != null)
                    continue;
                net = new SchematicNet();
                nets.add(net);
                net.addNode(node);
            } else if (net != node.getNet() && node.getNet() != null) {
                SchematicNet other = node.getNet();
                net.joinNets(other);
                nets.remove(other);
            } else if (net != node.getNet())
                net.addNode(node);
        }
    }

    public void build(Schematic s){
        nodes = s.getNodes();
        for ( SchematicNode node : nodes) {
            if (node.getNet() == null)
                continue;
            node.setNet(null);
            nets.remove(node.getNet());
        }
        for ( SchematicNode n : nodes ){
            SchematicNet net;
            List<SchematicNode> cn =  n.getConnectedNodes();
            net = n.getNet();
            for ( SchematicNode cnode : cn ) {
                if ( net != null)
                    break;
                net = cnode.getNet();
            }
            if ( net == null ) {
                net = new SchematicNet();
                nets.add(net);
            }
            net.addNode(n);
            for ( SchematicNode cnode : cn ) {
                SchematicNet other = cnode.getNet();
                if ( other == null )
                    net.addNode(cnode);
                else if ( other != net ){
                    net.joinNets(other);
                    nets.remove(other);
                }
            }
        }
        for ( SchematicNode node : nodes) {
            List<SchematicNode> net = s.getNode(node.getConnectionX(), node.getConnectionY());
            join(net);
        }
        System.out.println(s);
    }
}
