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
import nl.peterbjornx.openlogiceda.gui.view.TwoDGraphics;
import nl.peterbjornx.openlogiceda.model.draw.CompositePart;
import nl.peterbjornx.openlogiceda.model.draw.Drawing;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.io.File;

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
        g.setVariable("SIM", simText);
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

    }

    /**
     * Creates a copy of this part
     */
    @Override
    public DrawingPart copy() {
       ComponentPart p = new ComponentPart(name);
       p.setX(x);
       p.setY(y);
       return p;
    }
}
