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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import nl.peterbjornx.openlogiceda.model.draw.Drawing;
import nl.peterbjornx.openlogiceda.model.draw.DrawingIO;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Bosch
 */
@XStreamAlias("schematic")
public class Schematic extends Drawing {

    /**
     * This component's name
     */
    private String name;
    private static DrawingIO io;

    static {
        io = new DrawingIO();
        io.getSerializer().processAnnotations(Schematic.class);
        io.getSerializer().processAnnotations(ComponentPart.class);
        io.getSerializer().processAnnotations(BaseSchematicPart.class);
        io.getSerializer().processAnnotations(Drawing.class);
        io.getSerializer().processAnnotations(DrawingPart.class);
        io.getSerializer().processAnnotations(RectanglePart.class);
        io.getSerializer().processAnnotations(TextPart.class);
        io.getSerializer().processAnnotations(LinePart.class);
        io.getSerializer().processAnnotations(WirePart.class);
    }

    public List<SchematicNode> getNode(int x, int y) {
        LinkedList<SchematicNode> nodes = new LinkedList<>();
        List<DrawingPart> e =  getParts();
        for (DrawingPart p: e) {
            if (!(p instanceof BaseSchematicPart))
                continue;
            SchematicNode node = ((BaseSchematicPart) p).getNodeAt(x,y);
            if ( node == null )
                continue;
            node.setConnectionX(x);
            node.setConnectionY(y);
            nodes.add(node);
        }
        return nodes;
    }

    public List<SchematicNode> getNodes() {
        LinkedList<SchematicNode> nodes = new LinkedList<>();
        List<DrawingPart> e = getParts();
        for (DrawingPart p: e) {
            if (!(p instanceof BaseSchematicPart))
                continue;
            nodes.addAll(((BaseSchematicPart) p).getNodes());
        }
        return nodes;
    }

    /**
     * Creates a new component
     * @param name The name of the component
     */
    public Schematic( String name ) {
        super(11690,8270);
        this.name = name;
    }

    /**
     * Do post read fixups
     */
    @SuppressWarnings("unused")
    protected Object readResolve() {
        return super.readResolve();
    }

    @Override
    public DrawingIO getIO() {
        return io;
    }

    /**
     * Get this component's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set this component's name
     */
    public void setName(String name) {
        this.name = name;
    }
}
