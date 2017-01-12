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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a drawing
 * @author Peter Bosch
 */
public class Drawing {

    /**
     * The parts on this drawing
     */
    @XStreamImplicit
    private List<DrawingPart> parts = new LinkedList<>();

    /**
     * The list of selected parts
     */
    @XStreamOmitField
    private List<DrawingPart> selectedParts = new LinkedList<>();

    /**
     * The width of this drawing
     */
    @XStreamAsAttribute
    private int width;

    /**
     * The height of this drawing
     */
    @XStreamAsAttribute
    private int height;

    /**
     * Creates a new drawing of the given size
     */
    public Drawing( int width, int height ){
        this.width = width;
        this.height = height;
    }

    /**
     * Add a part
     */
    public void addPart( DrawingPart part ) {
        parts.add( part );
    }

    /**
     * Delete a part
     */
    public void deletePart(DrawingPart part) {
        parts.remove( part );
    }

    /**
     * Selects a part
     */
    public void selectPart( DrawingPart part ) {
        selectedParts.add(part);
        part.setSelected(true);
    }

    /**
     * Unselects a part
     */
    public void unselectPart(DrawingPart part ) {
        selectedParts.remove(part);
        part.setSelected(false);
    }

    /**
     * Clear selection
     */
    public void clearSelection() {
        while ( !selectedParts.isEmpty() )
            unselectPart(selectedParts.get(0));
    }

    /**
     * Deletes all parts in the selection
     */
    public void deleteSelection() {
        while ( !selectedParts.isEmpty() ) {
            DrawingPart d = selectedParts.get(0);
            unselectPart(d);
            deletePart(d);
        }

    }

    /**
     * Finds the parts at the given coordinates
     */
    public List<DrawingPart> getParts( int x, int y ) {
        List<DrawingPart> foundParts = new LinkedList<>();
        for ( DrawingPart part : parts )
            if ( part.contains(x, y) )
                foundParts.add(part);
        return foundParts;
    }

    /**
     * Get the parts on this drawing
     */
    public List<DrawingPart> getParts() {
        return parts;
    }

    /**
     * Get the parts that were selected
     */
    public List<DrawingPart> getSelectedParts() {
        return selectedParts;
    }

    /**
     * Gets the width of this drawing
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of this drawing
     */
    public int getHeight() {
        return height;
    }

    /**
     * Select the parts
     */
    public void selectParts(List<DrawingPart> list) {
        for ( DrawingPart part : list )
            selectPart(part);
    }

    /**
     * Do post read fixups
     */
    @SuppressWarnings("unused")
    private Object readResolve() {
        selectedParts = new LinkedList<>();
        return this;
    }

}
