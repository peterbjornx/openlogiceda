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

import nl.peterbjornx.openlogiceda.config.SchematicColours;

import java.awt.*;

import static nl.peterbjornx.openlogiceda.config.SchematicColours.getBackgroundColour;

/**
 * A version of Two2DView that has a configurable grid
 * @author Peter Bosch
 */
public class GridView extends TwoDView {

    /**
     * Whether or not to use a screen filling cursor
     */
    private boolean longCursor = true;

    /**
     * The radius of the grid dots
     */
    private float gridRadius = 2.0f;

    /**
     * The width of the cursor lines
     */
    private float cursorWidth = 2.0f;

    /**
     * The spacing of the grid dots
     */
    private int gridSpacing = 200;

    /**
     * The last detected X coordinate of the mouse in view coordinates
     */
    protected int cursorX;

    /**
     * The last detected Y coordinate of the mouse in view coordinates
     */
    protected int cursorY;

    /**
     * Draws a grid point
     */
    private void drawGridPoint( int x, int y ){
        graphics.setStroke(gridRadius,false);
        graphics.setColor(SchematicColours.gridColour);
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
    }

    protected void drawCursor() {
        graphics.setColor(getBackgroundColour());
        graphics.setXORMode(SchematicColours.cursorColour);
        graphics.setStroke(cursorWidth,false);
        if ( longCursor ) {
            graphics.drawLine(viewportLeft, cursorY, viewportRight, cursorY);
            graphics.drawLine(cursorX, viewportTop, cursorX, viewportBottom);
        } else {
            int size = (int) (10 / viewportZoom);
            graphics.drawLine(cursorX - size, cursorY, cursorX + size, cursorY);
            graphics.drawLine(cursorX, cursorY - size, cursorX, cursorY + size);
        }
        graphics.setPaintMode();
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
        cursorX = roundToGrid(x);
        cursorY = roundToGrid(y);
        repaint();
        return s;
    }

    /**
     * Called when the user drags the mouse
     * @param dx The difference of the x coordinate since the last event
     * @param dy The difference of the y coordinate since the last event
     * @return Whether the event was handled
     */
    @Override
    protected boolean onMouseDrag(int dx, int dy) {
        boolean s = super.onMouseDrag(dx,dy);
        cursorX += dx;
        cursorY += dy;
        repaint();
        return s;
    }

    /**
     * Sets the colour of the grid
     */
    public Color getGridColour() {
        return SchematicColours.gridColour;
    }

    /**
     * Gets the colour of the grid
     */
    public void setGridColour(Color gridColour) {
        SchematicColours.gridColour = gridColour;
    }
}
