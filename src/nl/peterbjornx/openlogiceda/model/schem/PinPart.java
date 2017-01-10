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

import java.awt.*;

/**
 * A pin in a component
 * @author Peter Bosch
 */
public class PinPart extends CompSymbolPart{

    /**
     * The length of the actual pin line segment
     */
    private static final int PIN_LENGTH = 200;

    /**
     * The length of the actual pin line segment
     */
    private static final int PIN_SPACING = 40;

    /**
     * The name of the pin
     */
    private String name;

    private static Font labelFont = new Font(Font.MONOSPACED, Font.PLAIN, 40);
    private static Color pinColour = Color.BLACK;
    private static Color selectedPinColour = Color.GREEN;

    public PinPart(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        updateSize();
    }

    private void updateSize() {
        switch (orientation) {
            case EAST:
                rightExtent = PIN_LENGTH;
                topExtent = PIN_SPACING;
                bottomExtent = PIN_SPACING;
                leftExtent = labelFont.getSize() * name.length();
                break;
            case WEST:
                leftExtent = PIN_LENGTH;
                topExtent = PIN_SPACING;
                bottomExtent = PIN_SPACING;
                rightExtent = labelFont.getSize() * name.length();
                break;
            case NORTH:
                topExtent = PIN_LENGTH;
                rightExtent = PIN_SPACING;
                leftExtent = PIN_SPACING;
                bottomExtent = labelFont.getSize() * name.length();
                break;
            case SOUTH:
                bottomExtent = PIN_LENGTH;
                rightExtent = PIN_SPACING;
                leftExtent = PIN_SPACING;
                topExtent = labelFont.getSize() * name.length();
                break;
        }

    }

    /**
     * Sets the orientation of this part
     */
    @Override
    public void setOrientation(Rotation orientation) {
        super.setOrientation(orientation);
        updateSize();
    }

    @Override
    public void paintPart(Graphics2D g, double zoom) {
        g.setStroke(new BasicStroke((float) (3/zoom)));
        g.setFont(labelFont);
        switch( orientation ) {
            case EAST:
                if (selected)
                    g.setColor(selectedPinColour);
                else
                    g.setColor(pinColour);
                g.drawLine(0,0,PIN_LENGTH,0);
                g.drawString(name, -leftExtent, labelFont.getSize()/4);
                break;
        }

    }

}
