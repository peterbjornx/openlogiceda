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
 * Represents a part of a drawing, such as a component or a label
 * @author Peter Bosch
 */
public abstract class DrawingPart {

    /**
     * The x coordinate of the parts centre
     */
    protected int x;

    /**
     * The y coordinate of the parts centre
     */
    protected int y;

    /**
     * The width of the part
     */
    protected int width;

    /**
     * The height of the part
     */
    protected int height;

    /**
     * Whether this part was selected
     */
    protected boolean selected = false;

    /**
     * Tests whether a point is inside the part
     */
    public boolean contains(int x, int y ) {
        x -= this.x;
        y -= this.y;
        return Math.abs(x) <= width && Math.abs(y) <= height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isSelected() {
        return selected;
    }

    public abstract void paintPart(Graphics2D g , double zoom);

}