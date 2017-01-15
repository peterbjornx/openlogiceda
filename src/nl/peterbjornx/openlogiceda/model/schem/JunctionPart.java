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

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import nl.peterbjornx.openlogiceda.config.SchematicColours;
import nl.peterbjornx.openlogiceda.gui.schem.BaseSchematicView;
import nl.peterbjornx.openlogiceda.gui.view.TwoDGraphics;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Bosch
 */
public class JunctionPart extends BaseSchematicPart {
    @XStreamOmitField
    private JunctionNode node = new JunctionNode();

    public JunctionPart(){
        super();
        leftExtent = rightExtent = topExtent = bottomExtent = 10;
    }

    /**
     * Open the property window for this part.
     *
     * @param editor
     */
    @Override
    public void edit(BaseSchematicView editor) {

    }

    /**
     * Renders the part
     *
     * @param g    The graphics context used to render the part
     * @param zoom The current zoom level
     */
    @Override
    public void paintPart(TwoDGraphics g, double zoom) {
        g.setColor(SchematicColours.getWireColour());
        g.fillOval(-leftExtent,-topExtent,leftExtent+rightExtent,topExtent+bottomExtent);
    }

    public Object readResolve(){
        node = new JunctionNode();
        return this;
    }

    /**
     * Creates a copy of this part
     */
    @Override
    public DrawingPart copy() {
        JunctionPart jp = new JunctionPart();
        jp.setX(x);
        jp.setY(y);
        return jp;
    }

    @Override
    public SchematicNode getNodeAt(int x, int y) {
        if ( x == this.x && y == this.y )
            return node;
        return null;
    }

    @Override
    public List<SchematicNode> getNodes() {
        node.setConnectionX(x);
        node.setConnectionY(y);
        List<SchematicNode> nodes = new LinkedList<>();
        nodes.add(node);
        nodes.add(node);
        return nodes;
    }
}
