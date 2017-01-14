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
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import nl.peterbjornx.openlogiceda.gui.schem.BaseSchematicView;
import nl.peterbjornx.openlogiceda.gui.schem.dialog.CompPartDialog;
import nl.peterbjornx.openlogiceda.gui.view.TwoDGraphics;
import nl.peterbjornx.openlogiceda.model.draw.CompositePart;
import nl.peterbjornx.openlogiceda.model.draw.Drawing;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Bosch
 */
@XStreamAlias("component")
public class ComponentPart extends CompositePart {
    private String reference = "U?";
    @XStreamOmitField
    private SchematicComponent component;
    private String simText = "";
    private String name;
    private String simConfig;

    /**
     * Creates a composite part based on a drawing
     *
     * @param name The drawing to use
     */
    public ComponentPart(String name) {
        super();
        SchematicComponent component = loadComponent(name);
        this.component = component;
        setSubDrawing(component);
        this.name =name;
    }
    public ComponentPart() {
        super();
        setSubDrawing(new SchematicComponent("jkhjhk"));
    }

    private SchematicComponent loadComponent(String name) {
        return (SchematicComponent) SchematicComponent.getIOStatic().load(new File("test/"+name+".cmp"));
    }

    protected Object readResolve(){
        component = loadComponent(name);
        setSubDrawing(component);
        return this;
    }

    @Override
    public void setVariables(TwoDGraphics g) {
        super.setVariables(g);
        g.setVariable("REF", reference);
        g.setVariable("NAME", component.getName());
        g.setVariable("SIM", simText+simConfig);
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSimText() {
        return simText;
    }

    public void setSimText(String simText) {
        this.simText = simText;
    }

    @Override
    public void edit(BaseSchematicView editor) {
        CompPartDialog.main(editor,this);
    }

    @Override
    public SchematicNode getNodeAt(int x, int y) {
        List<DrawingPart> e =  getSubDrawing().getParts();
        for (DrawingPart p: e){
            if ( !(p instanceof  BaseSchematicPart))
                continue;
            SchematicNode found = ((BaseSchematicPart) p).getNodeAt(
                    x-this.x+getSubDrawing().getWidth()/2,
                    y-this.y+getSubDrawing().getHeight()/2);
            if (found != null)
                return found;
        }
        return null;
    }

    @Override
    public List<SchematicNode> getNodes() {
        LinkedList<SchematicNode> nodes = new LinkedList<>();
        List<DrawingPart> e =  getSubDrawing().getParts();
        for (DrawingPart p: e) {
            if (!(p instanceof BaseSchematicPart))
                continue;
            nodes.addAll(((BaseSchematicPart) p).getNodes());
        }
        for (SchematicNode n : nodes) {
            n.setConnectionX(n.getConnectionX() + x - getSubDrawing().getWidth()/2);
            n.setConnectionY(n.getConnectionY() + y - getSubDrawing().getHeight()/2);
            if (n instanceof PinNode)
                ((PinNode) n).setComponent(this);
        }
        return nodes;
    }

    /**
     * Creates a copy of this part
     */
    @Override
    public DrawingPart copy() {
       ComponentPart p = new ComponentPart(name);
       p.setX(x);
       p.setY(y);
       p.setSimConfig(simConfig);
       return p;
    }

    public String getName() {
        return name;
    }

    public String getSimConfig() {
        return simConfig;
    }

    public void setName(String name) {
        SchematicComponent component = loadComponent(name);
        this.component = component;
        setSubDrawing(component);
        this.name =name;
    }

    public void setSimConfig(String simConfig) {
        this.simConfig = simConfig;
    }
}
