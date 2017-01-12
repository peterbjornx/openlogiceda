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

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import nl.peterbjornx.openlogiceda.gui.view.TwoDGraphics;

/**
 * Represents a part of a drawing, such as a component or a label
 * @author Peter Bosch
 */
public abstract class DrawingPart {

    /**
     * The x coordinate of the part
     */
    @XStreamAsAttribute
    protected int x;

    /**
     * The y coordinate of the part
     */
    @XStreamAsAttribute
    protected int y;

    /**
     * The size of the part to the left of the center
     */
    protected int leftExtent = 0;

    /**
     * The size of the part to the right of the center
     */
    protected int rightExtent = 0;

    /**
     * The size of the part above the center
     */
    protected int topExtent = 0;

    /**
     * The size of the part below the center
     */
    protected int bottomExtent = 0;

    /**
     * Whether this part was selected
     */
    @XStreamOmitField
    protected boolean selected = false;

    /**
     * Tests whether a point is inside the part
     */
    public boolean contains(int x, int y ) {
        x -= this.x;
        y -= this.y;
        return x >= -leftExtent && x <= rightExtent && y >= -topExtent && y <= bottomExtent;
    }

    /**
     * Gets the center X coordinate of a part
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the center X coordinate of a part
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the center Y coordinate of a part
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the center Y coordinate of a part
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the parts width
     */
    public int getWidth() {
        return leftExtent + rightExtent;
    }

    /**
     * Gets the part's height
     */
    public int getHeight() {
        return topExtent + bottomExtent;
    }

    /**
     * Gets the leftmost coordinate in the part
     */
    public int getLeft() {
        return x - leftExtent;
    }

    /**
     * Gets the rightmost coordinate in the part
     */
    public int getRight() {
        return x + rightExtent;
    }

    /**
     * Gets the topmost coordinate in the part
     */
    public int getTop() {
        return y - topExtent;
    }

    /**
     * Gets the bottommost coordinate in the part
     */
    public int getBottom() {
        return y + bottomExtent;
    }

    /**
     * Gets the size of the part to the left of the center
     */
    public int getLeftExtent() {
        return leftExtent;
    }

    /**
     * Sets the size of the part to the left of the center
     */
    public void setLeftExtent(int leftExtent) {
        this.leftExtent = leftExtent;
    }

    /**
     * Gets the size of the part to the right of the center
     */
    public int getRightExtent() {
        return rightExtent;
    }

    /**
     * Sets the size of the part to the right of the center
     */
    public void setRightExtent(int rightExtent) {
        this.rightExtent = rightExtent;
    }

    /**
     * Gets the size of the part above the center
     */
    public int getTopExtent() {
        return topExtent;
    }

    /**
     * Sets the size of the part above the center
     */
    public void setTopExtent(int topExtent) {
        this.topExtent = topExtent;
    }

    /**
     * Gets the size of the part below the center
     */
    public int getBottomExtent() {
        return bottomExtent;
    }

    /**
     * Sets the size of the part below the center
     */
    public void setBottomExtent(int bottomExtent) {
        this.bottomExtent = bottomExtent;
    }

    /**
     * Is this part selected?
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Indicate whether or not this part is selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Renders the part
     * @param g The graphics context used to render the part
     * @param zoom The current zoom level
     */
    public abstract void paintPart(TwoDGraphics g , double zoom);

    /**
     * Creates a copy of this part
     */
    public abstract DrawingPart copy();
}
