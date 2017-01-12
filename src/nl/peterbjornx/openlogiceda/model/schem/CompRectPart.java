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
import nl.peterbjornx.openlogiceda.gui.schem.ComponentView;
import nl.peterbjornx.openlogiceda.gui.view.TwoDGraphics;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.awt.*;

/**
 * @author Peter Bosch
 */
@XStreamAlias("comprect")
public class CompRectPart extends  CompSymbolPart {
    private Color normalColour = Color.BLUE;
    private int lineWidth = 15;

    @Override
    public void edit(ComponentView editor) {

    }

    @Override
    public void paintPart(TwoDGraphics g, double zoom) {
        if ( selected )
            g.setColor(new Color(255-normalColour.getRed(),
                    255-normalColour.getGreen(),
                    255-normalColour.getBlue()));
        else
            g.setColor(normalColour);
        g.setStroke(lineWidth,false);
        g.drawRect(-leftExtent,-topExtent,leftExtent+rightExtent,topExtent+bottomExtent);
    }

    @Override
    public DrawingPart copy() {
        CompRectPart rect = new CompRectPart();
        rect.setOrientation(getOrientation());
        rect.setBottomExtent(getBottomExtent());
        rect.setLeftExtent(getLeftExtent());
        rect.setTopExtent(getTopExtent());
        rect.setRightExtent(getRightExtent());
        return rect;
    }
}
