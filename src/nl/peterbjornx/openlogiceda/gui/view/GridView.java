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
    private int gridSpacing = 400;

    /**
     * Draws a grid point
     */
    private void drawGridPoint( int x, int y ){
        graphics.setColor( gridColour );
        graphics.fillRect( x - gridRadius, y - gridRadius, gridRadius * 2, gridRadius * 2 );
    }

    private int roundToGrid( int c ) {
        return ( c / gridSpacing ) * gridSpacing;
    }

    @Override
    protected void paintView() {
        for ( int x = roundToGrid(viewportLeft); x <= roundToGrid(viewportRight); x+=gridSpacing )
            for ( int y = roundToGrid(viewportTop); y <= roundToGrid(viewportBottom); y+=gridSpacing )
                drawGridPoint( roundToGrid(x), roundToGrid(y) );
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
