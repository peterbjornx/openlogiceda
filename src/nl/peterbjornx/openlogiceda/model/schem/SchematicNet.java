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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Peter Bosch
 */
public class SchematicNet {

    private Set<SchematicNode> nodes = new HashSet<>();

    public void addNode( SchematicNode node ) {
        nodes.add(node);
        node.setNet(this);
    }

    public Set<SchematicNode> getNodes() {
        return nodes;
    }

    public void joinNets( SchematicNet b ) {
        if (this == b)
            return;
        for (SchematicNode node : b.nodes) {
            node.setNet(this);
            nodes.add( node );
        }
    }
}
