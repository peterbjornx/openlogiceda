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
import nl.peterbjornx.openlogiceda.config.SchematicColours;
import nl.peterbjornx.openlogiceda.gui.schem.BaseSchematicView;
import nl.peterbjornx.openlogiceda.gui.schem.dialog.LineDialog;
import nl.peterbjornx.openlogiceda.gui.view.TwoDGraphics;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.awt.*;

/**
 * @author Peter Bosch
 */
@XStreamAlias("line")
public class LinePart extends BaseSchematicPart {
    protected Color lineColour = SchematicColours.getDefaultShapeColour();
    protected int lineWidth = 15;

    @Override
    public void edit(BaseSchematicView editor) {
        LineDialog.main(editor,this);
    }

    @Override
    public void paintPart(TwoDGraphics g, double zoom) {
        if ( selected )
            g.setColor(new Color(255- lineColour.getRed(),
                    255- lineColour.getGreen(),
                    255- lineColour.getBlue()));
        else
            g.setColor(lineColour);
        g.setStroke(lineWidth,false);
        if (orientation == Rotation.EAST || orientation == Rotation.WEST)
            g.drawLine(-leftExtent,-topExtent,rightExtent,bottomExtent);
        else
            g.drawLine(-leftExtent,bottomExtent,rightExtent,-topExtent);
    }

    @Override
    public DrawingPart copy() {
        LinePart rect = new LinePart();
        rect.setX(x);
        rect.setY(y);
        rect.setOrientation(getOrientation());
        rect.setBottomExtent(getBottomExtent());
        rect.setLeftExtent(getLeftExtent());
        rect.setTopExtent(getTopExtent());
        rect.setRightExtent(getRightExtent());
        return rect;
    }

    public Color getLineColour() {
        return lineColour;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineColour(Color lineColour) {
        this.lineColour = lineColour;
    }

    @Override
    public String toString() {
        return "Line ("+x+","+y+")";
    }
}
