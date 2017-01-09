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

import java.awt.*;

/**
 * Implements a part that is made of a collection of other parts
 * @author Peter Bosch
 */
public class CompositePart extends DrawingPart {

    /**
     * The drawing specifying the components of this part
     */
    private Drawing subDrawing;

    /**
     * Creates a composite part based on a drawing
     * @param subDrawing The drawing to use
     */
    public CompositePart(Drawing subDrawing) {
        this.subDrawing = subDrawing;
        java.util.List<DrawingPart> parts = subDrawing.getParts();
        for ( DrawingPart p : parts ){
            leftExtent = Integer.min(p.getLeft(),leftExtent);
            rightExtent = Integer.max(p.getRight(),rightExtent);
            topExtent = Integer.min(p.getTop(),topExtent);
            bottomExtent = Integer.max(p.getTop(),bottomExtent);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        java.util.List<DrawingPart> parts = subDrawing.getParts();
        for ( DrawingPart p : parts )
            p.setSelected(selected);
    }

    @Override
    public void paintPart(Graphics2D g, double zoom) {
        java.util.List<DrawingPart> parts = subDrawing.getParts();
        for ( DrawingPart d : parts ){
            Graphics2D p = (Graphics2D) g.create();
            p.translate(d.getX(),d.getY());
            d.paintPart(p, zoom);
        }
    }
}
