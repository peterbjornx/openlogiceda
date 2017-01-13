package nl.peterbjornx.openlogiceda.config;/*
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
 * @author Peter Bosch
 */
public class SchematicColours {
    /**
     * The colour of the grid
     */
    public static Color gridColour = Color.BLACK;
    /**
     * The colour used to render the cursor
     */
    public static Color cursorColour = Color.BLACK;
    /**
     * The default colour for shape parts
     */
    private static Color defaultShapeColour = Color.BLUE;
    /**
     * The colour of the pin
     */
    public static Color pinColour = Color.BLACK;

    /**
     * The default colour for text parrts
     */
    private static Color defaultTextColour = Color.BLACK;
    private static Color backgroundColour = Color.WHITE;
    private static Color wireColour = Color.GREEN;

    public static Color getDefaultShapeColour() {
        return defaultShapeColour;
    }

    public static Color getDefaultTextColour() {
        return defaultTextColour;
    }

    public static Color getBackgroundColour() {
        return backgroundColour;
    }

    public static Color getGridColour() {
        return gridColour;
    }

    public static Color getCursorColour() {
        return cursorColour;
    }

    public static void setGridColour(Color gridColour) {
        SchematicColours.gridColour = gridColour;
    }

    public static void setCursorColour(Color cursorColour) {
        SchematicColours.cursorColour = cursorColour;
    }

    public static void setDefaultShapeColour(Color defaultShapeColour) {
        SchematicColours.defaultShapeColour = defaultShapeColour;
    }

    public static void setPinColour(Color pinColour) {
        SchematicColours.pinColour = pinColour;
    }

    public static void setDefaultTextColour(Color defaultTextColour) {
        SchematicColours.defaultTextColour = defaultTextColour;
    }

    public static void setBackgroundColour(Color backgroundColour) {
        SchematicColours.backgroundColour = backgroundColour;
    }

    public static Color getPinColour() {
        return pinColour;
    }

    public static Color getWireColour() {
        return wireColour;
    }
}
