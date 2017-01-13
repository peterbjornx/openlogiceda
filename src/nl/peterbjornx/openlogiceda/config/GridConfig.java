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

/**
 * @author Peter Bosch
 */
public class GridConfig {
    /**
     * Whether or not to use a screen filling cursor
     */
    public static boolean longCursor = true;
    /**
     * The radius of the grid dots
     */
    public static float gridRadius = 2.0f;
    /**
     * The width of the cursor lines
     */
    public static float cursorWidth = 2.0f;
    /**
     * The spacing of the grid dots
     */
    public static int gridSpacing = 200;

    public static boolean isLongCursor() {
        return longCursor;
    }

    public static void setLongCursor(boolean longCursor) {
        GridConfig.longCursor = longCursor;
    }

    public static float getGridRadius() {
        return gridRadius;
    }

    public static void setGridRadius(float gridRadius) {
        GridConfig.gridRadius = gridRadius;
    }

    public static float getCursorWidth() {
        return cursorWidth;
    }

    public static void setCursorWidth(float cursorWidth) {
        GridConfig.cursorWidth = cursorWidth;
    }

    public static int getGridSpacing() {
        return gridSpacing;
    }

    public static void setGridSpacing(int gridSpacing) {
        GridConfig.gridSpacing = gridSpacing;
    }

    public static boolean getLongCursor() {
        return longCursor;
    }


}
