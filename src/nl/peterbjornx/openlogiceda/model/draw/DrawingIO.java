package nl.peterbjornx.openlogiceda.model.draw;/*
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
import nl.peterbjornx.openlogiceda.model.schem.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Peter Bosch
 */
public class DrawingIO {

    /**
     * The XStream used for serialization
     */
    private XStream serializer;

    public DrawingIO(){
        serializer = new XStream();
        serializer.processAnnotations(SchematicComponent.class);
        serializer.processAnnotations(PinPart.class);
        serializer.processAnnotations(BaseSchematicPart.class);
        serializer.processAnnotations(Drawing.class);
        serializer.processAnnotations(DrawingPart.class);
        serializer.processAnnotations(CompRectPart.class);
        serializer.processAnnotations(TextPart.class);
    }

    /**
     * Loads a component from a string
     */
    public Drawing load( String xml ) {
        return (Drawing) serializer.fromXML(xml);
    }

    /**
     * Loads a component from disk
     */
    public Drawing load( File file ) {
        return (Drawing) serializer.fromXML(file);
    }

    /**
     * Saves a component to disk
     */
    public void store( File file, Drawing d ) throws IOException {
        FileWriter writer = new FileWriter(file);
        serializer.toXML(d,writer);
        writer.close();
    }

    /**
     * Saves a component to a string
     */
    public String store( Drawing d ) {
        return serializer.toXML(d);
    }


    public XStream getSerializer() {
        return serializer;
    }
}
