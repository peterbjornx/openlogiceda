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

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import nl.peterbjornx.openlogiceda.gui.schem.BaseSchematicView;
import nl.peterbjornx.openlogiceda.gui.view.TwoDGraphics;
import nl.peterbjornx.openlogiceda.model.schem.BaseSchematicPart;

import java.awt.*;

/**
 * Implements a part that is made of a collection of other parts
 * @author Peter Bosch
 */
public abstract class CompositePart extends BaseSchematicPart {

    /**
     * The drawing specifying the components of this part
     */
    @XStreamOmitField
    private Drawing subDrawing;

    /**
     * Creates a composite part based on a drawing
     * @param subDrawing The drawing to use
     */
    public CompositePart(Drawing subDrawing) {
        setSubDrawing(subDrawing);
    }

    protected CompositePart() {

    }

    public void setSubDrawing(Drawing d){
        this.subDrawing = d;
        java.util.List<DrawingPart> parts = d.getParts();
        for ( DrawingPart p : parts ){
            leftExtent = Integer.max(d.getWidth() / 2 - p.getLeft(),leftExtent);
            rightExtent = Integer.max(p.getRight() - d.getWidth() / 2,rightExtent);
            topExtent = Integer.max(d.getHeight() / 2 - p.getTop(),topExtent);
            bottomExtent = Integer.max(p.getBottom()-d.getHeight()/2,bottomExtent);
        }}

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        java.util.List<DrawingPart> parts = subDrawing.getParts();
        for ( DrawingPart p : parts )
            p.setSelected(selected);
    }

    @Override
    public void paintPart(TwoDGraphics g, double zoom) {
        java.util.List<DrawingPart> parts = subDrawing.getParts();
        setVariables(g);
        for ( DrawingPart d : parts ){
            TwoDGraphics p = g.create();
            p.translate(d.getX()-subDrawing.getWidth()/2,d.getY()-subDrawing.getHeight()/2);
            d.paintPart(p, zoom);
        }
    }

    public Drawing getSubDrawing() {
        return subDrawing;
    }

    public void setVariables(TwoDGraphics g) {}
}
