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

/**
 * Represents a component in the schematic
 * @author Peter Bosch
 */
@XStreamAlias("scomponent")
public class SchematicComponent extends Drawing {

    /**
     * This component's name
     */
    private String name;
    private static DrawingIO io;

    static {
        io = new DrawingIO();
        io.getSerializer().processAnnotations(SchematicComponent.class);
        io.getSerializer().processAnnotations(PinPart.class);
        io.getSerializer().processAnnotations(BaseSchematicPart.class);
        io.getSerializer().processAnnotations(Drawing.class);
        io.getSerializer().processAnnotations(DrawingPart.class);
        io.getSerializer().processAnnotations(RectanglePart.class);
        io.getSerializer().processAnnotations(TextPart.class);
        io.getSerializer().processAnnotations(LinePart.class);
    }

    private String simName = "";

    /**
     * Creates a new component
     * @param name The name of the component
     */
    public SchematicComponent( String name ) {
        super(10000,10000);
        this.name = name;
    }

    public static DrawingIO getIOStatic() {
        return io;
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

    public String getSimName() {
        return simName;
    }

    public void setSimName(String simName) {
        this.simName = simName;
    }
}
