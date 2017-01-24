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

import nl.peterbjornx.openlogiceda.model.Circuit;
import nl.peterbjornx.openlogiceda.model.Component;
import nl.peterbjornx.openlogiceda.model.Net;
import nl.peterbjornx.openlogiceda.model.Node;
import nl.peterbjornx.openlogiceda.model.draw.CompositePart;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;
import nl.peterbjornx.openlogiceda.sim.ComponentCreator;
import nl.peterbjornx.openlogiceda.util.ModificationException;
import nl.peterbjornx.openlogiceda.util.SimulationException;

import java.util.*;

/**
 * @author Peter Bosch
 */
public class SchematicCircuit {

    private List<SchematicNet> nets = new LinkedList<>();
    private List<SchematicNode> nodes;
    private String name;

    private void join(Collection<SchematicNode> nodes){
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
        name = s.getName();
        nodes = s.getNodes();
        for ( SchematicNode node : nodes) {
            if (node.getNet() == null)
                continue;
            node.setNet(null);
            nets.remove(node.getNet());
        }
        for ( SchematicNode n : nodes ){
            Set<SchematicNode> cn = new HashSet<>();
            cn.add(n);
            if (n instanceof WireNode){
                cn.addAll(((WireNode) n).getPart().getNodes());
            } else if (n instanceof JunctionNode) {
                List<DrawingPart> parts = s.getParts(n.getConnectionX(),n.getConnectionY());
                for (DrawingPart p : parts) {
                    if (!(p instanceof BaseSchematicPart))
                        continue;
                    BaseSchematicPart sp = (BaseSchematicPart) p;
                    if (!(p instanceof WirePart))
                        continue;
                    cn.addAll(sp.getNodes());
                }
            }
            if (cn.size() != 0)
                join(cn);
        }
        for ( SchematicNode node : nodes) {
            List<SchematicNode> net = s.getNode(node.getConnectionX(), node.getConnectionY());
            join(net);
        }
        System.out.println(s);
    }

    public Circuit compileCircuit() throws SimulationException, ModificationException {
        ComponentCreator cr = new ComponentCreator();
        Circuit output = new Circuit(name);
        HashMap<ComponentPart, Component> compMap = new HashMap<>();
        HashMap<SchematicNode, Node> nodeMap = new HashMap<>();
        HashMap<SchematicNet, Net> netMap = new HashMap<>();
        int nc = 0;
        for (SchematicNode n : nodes) {
            if ( n instanceof PinNode ){
                ComponentPart p = ((PinNode) n).getComponent();
                SchematicComponent sc = (SchematicComponent)p.getSubDrawing();
                Component c = compMap.get(p);
                if ( c == null ) {
                    c = cr.createComponent(sc.getSimName(),p.getSimConfig());
                    compMap.put(p,c);
                    output.addComponent(c);
                }
                Node cn = c.getNodeByName(((PinNode) n).getPinPart().getName());
                nodeMap.put(n,cn);
                SchematicNet snet = n.getNet();
                Net cnet = netMap.get(snet);
                if ( cnet == null ) {
                    cnet = new Net("N"+nc++);
                    netMap.put(snet,cnet);
                }
                output.connectNode(cnet,cn);
            }
        }
        return output;
    }
}
