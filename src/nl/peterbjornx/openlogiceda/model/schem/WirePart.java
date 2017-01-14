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


import nl.peterbjornx.openlogiceda.config.SchematicColours;
import nl.peterbjornx.openlogiceda.config.SchematicConfig;
import nl.peterbjornx.openlogiceda.gui.schem.BaseSchematicView;
import nl.peterbjornx.openlogiceda.gui.view.TwoDGraphics;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.awt.*;

/**
 * @author Peter Bosch
 */
public class WirePart extends LinePart {

    private int length;

    @Override
    public void edit(BaseSchematicView editor) {

    }

    @Override
    public void paintPart(TwoDGraphics g, double zoom) {
        /*if ( selected )
            g.setColor(new Color(255- lineColour.getRed(),
                    255- lineColour.getGreen(),
                    255- lineColour.getBlue()));
        else*/
        g.setColor(SchematicColours.getWireColour());
        g.setStroke(SchematicConfig.getWireWidth(),false);
        int xx,yx;
        if (leftExtent>rightExtent)
            xx = -leftExtent;
        else
            xx = rightExtent;
        if (topExtent>bottomExtent)
            yx = -topExtent;
        else
            yx = bottomExtent;
        g.drawLine(0,0,xx,yx);
        if (selected){
            g.setColor(Color.black);
            g.drawLine(0,0,0,0);
            g.drawLine(xx,yx,xx,yx);
        }
    }

    @Override
    public DrawingPart copy() {
        WirePart rect = new WirePart();
        rect.setX(x);
        rect.setY(y);
        rect.setOrientation(getOrientation());
        rect.setBottomExtent(getBottomExtent());
        rect.setLeftExtent(getLeftExtent());
        rect.setTopExtent(getTopExtent());
        rect.setRightExtent(getRightExtent());
        return rect;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "("+x+","+y+")-("+getBX()+","+getBY()+")";
    }
}
