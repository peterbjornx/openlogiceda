package nl.peterbjornx.openlogiceda.gui.view;/*
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
 * A version of Two2DView that has a configurable grid
 * @author Peter Bosch
 */
public class GridView extends TwoDView {

    /**
     * The colour of the grid
     */
    private Color gridColour = Color.BLACK;

    /**
     * The radius of the grid dots
     */
    private int gridRadius = 10;

    /**
     * The spacing of the grid dots
     */
    private int gridSpacing = 200;
    private int lastMouseX;
    private int lastMouseY;
    private Color cursorColour = Color.BLACK;

    /**
     * Draws a grid point
     */
    private void drawGridPoint( int x, int y ){
        graphics.setStroke(2,true);
        graphics.setColor(gridColour);
        graphics.drawLine(x,y,x,y);
    }

    private int roundDownToGrid(int c ) {
        return ( c / gridSpacing ) * gridSpacing;
    }

    protected int roundToGrid( int c ) {
        return ((c + gridSpacing/2) / gridSpacing) * gridSpacing;
    }

    @Override
    protected void paintView() {
        for (int x = roundDownToGrid(viewportLeft); x <= roundDownToGrid(viewportRight); x+=gridSpacing )
            for (int y = roundDownToGrid(viewportTop); y <= roundDownToGrid(viewportBottom); y+=gridSpacing )
                drawGridPoint( roundDownToGrid(x), roundDownToGrid(y) );
        drawCursor();
    }

    private void drawCursor() {
        graphics.setColor(cursorColour);
        int size = (int) (10/viewportZoom);
        graphics.setStroke(1,true);
        graphics.drawLine(lastMouseX - size, lastMouseY, lastMouseX + size, lastMouseY);
        graphics.drawLine(lastMouseX, lastMouseY - size, lastMouseX, lastMouseY + size);
    }

    /**
     * Called when the user moves the mouse
     *
     * @param x The x coordinate of the cursor in view coordinates
     * @param y The y coordinate of the cursor in view coordinates
     * @return Whether the event was handled
     */
    @Override
    protected boolean onMouseMove(int x, int y) {
        boolean s = super.onMouseMove(x,y);
        lastMouseX = roundToGrid(x);
        lastMouseY = roundToGrid(y);
        repaint();
        return s;
    }

    /**
     * Sets the colour of the grid
     */
    public Color getGridColour() {
        return gridColour;
    }

    /**
     * Gets the colour of the grid
     */
    public void setGridColour(Color gridColour) {
        this.gridColour = gridColour;
    }
}
