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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Bosch
 */
public abstract class SchematicNode {

    private SchematicNet net;
    private int connectionX;
    private int connectionY;

    public SchematicNet getNet() {
        return net;
    }

    public void setNet(SchematicNet net) {
        this.net = net;
    }

    public int getConnectionX() {
        return connectionX;
    }

    public void setConnectionX(int connectionX) {
        this.connectionX = connectionX;
    }

    public int getConnectionY() {
        return connectionY;
    }

    public void setConnectionY(int connectionY) {
        this.connectionY = connectionY;
    }

    public List<SchematicNode> getConnectedNodes() {
        return new LinkedList<>();
    }
}
