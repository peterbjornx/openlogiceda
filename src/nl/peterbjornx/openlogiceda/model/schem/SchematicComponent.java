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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import nl.peterbjornx.openlogiceda.model.draw.Drawing;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents a component in the schematic
 * @author Peter Bosch
 */
@XStreamAlias("scomponent")
public class SchematicComponent extends Drawing {

    /**
     * The XStream used for serialization
     */
    private static XStream serializer;

    /**
     * This component's name
     */
    private String name;

    /**
     * Creates a new component
     * @param name The name of the component
     */
    public SchematicComponent( String name ) {
        super(10000,10000);
        this.name = name;
    }

    static {
        serializer = new XStream();
        serializer.processAnnotations(SchematicComponent.class);
        serializer.processAnnotations(PinPart.class);
        serializer.processAnnotations(CompSymbolPart.class);
        serializer.processAnnotations(Drawing.class);
        serializer.processAnnotations(DrawingPart.class);
    }

    /**
     * Loads a component from disk
     */
    public static SchematicComponent load( File file ) {
        return (SchematicComponent) serializer.fromXML(file);
    }

    /**
     * Saves a component to disk
     */
    public void store( File file ) throws IOException {
        FileWriter writer = new FileWriter(file);
        serializer.toXML(this,writer);
        writer.close();
    }
}
