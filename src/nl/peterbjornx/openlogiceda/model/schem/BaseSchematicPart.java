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

import nl.peterbjornx.openlogiceda.gui.schem.BaseSchematicView;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This represents a part of a schematic drawing
 * @author Peter Bosch
 */
public abstract class BaseSchematicPart extends DrawingPart {

    /**
     * The orientation of this part
     */
    protected Rotation orientation = Rotation.EAST;

    /**
     * Gets the orientation of this part
     */
    public Rotation getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation of this part
     */
    public void setOrientation(Rotation orientation) {
        this.orientation = orientation;
    }

    /**
     * Open the property window for this part.
     */
    public abstract void edit(BaseSchematicView editor);

    /**
     * Gets the node at the given coordinates
     */
    public SchematicNode getNodeAt(int x,int y){return null;}

    /**
     * Gets all the nodes on this part
     */
    public List<SchematicNode> getNodes(){return new LinkedList<>();}
}
