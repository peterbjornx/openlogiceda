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

import nl.peterbjornx.openlogiceda.config.KeyBindings;
import nl.peterbjornx.openlogiceda.gui.view.event.EditModeListener;
import nl.peterbjornx.openlogiceda.gui.view.event.PartSelectionListener;
import nl.peterbjornx.openlogiceda.model.draw.Drawing;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Bosch
 */
public class DrawingView extends GridView {

    /**
     * The listeners for edit mode changes
     */
    private List<EditModeListener> editModeListeners = new LinkedList<>();

    /**
     * The listeners for selection changes
     */
    private List<PartSelectionListener> selectionListeners = new LinkedList<>();

    /**
     * ID for the selection mode, this is the default mode
     */
    public static final int MODE_SELECT = 0;

    /**
     * The drawing we are viewing/editing
     */
    private Drawing drawing;

    /**
     * The selected edit mode
     */
    protected int editMode = MODE_SELECT;

    /**
     * Whether multiple selection is enabled
     */
    private boolean selectMultiple = false;

    /**
     * Creates a new drawing view
     * @param drawing The drawing to view/edit
     */
    public DrawingView(Drawing drawing) {
        this.drawing = drawing;
        setViewSize( drawing.getWidth(), drawing.getHeight() );
    }

    /**
     * Get the parts that were selected
     */
    public List<DrawingPart> getSelectedParts() {
        return drawing.getSelectedParts();
    }

    @Override
    protected boolean onMouseClick(int button, int x, int y) {
        if ( super.onMouseClick(button, x, y) )
            return true;
        int rx = roundToGrid(x);
        int ry = roundToGrid(y);
        List<DrawingPart> parts = drawing.getParts(rx,ry);
        switch ( editMode ) {
            case MODE_SELECT:
                if ( parts.isEmpty()) {
                    if ( !selectMultiple )
                        clearSelection();
                    return true;
                }
                if ( button == MouseEvent.BUTTON1 )
                    //TODO: Add disambiguation menu option
                    selectPart( parts.get(0) );
                return true;
        }
        return false;
    }

    @Override
    protected boolean onKeyDown(int kc) {
        if ( super.onKeyDown( kc ))
            return true;
        if ( kc == KeyBindings.getDrawingSelectMultiple() ) {
            selectMultiple = true;
            return true;
        }
        return false;
    }

    @Override
    protected boolean onKeyUp(int kc) {
        if ( super.onKeyUp( kc ))
            return true;
        if ( kc == KeyBindings.getDrawingSelectMultiple() ) {
            selectMultiple = false;
            return true;
        }
        return false;
    }

    /**
     * Add a part
     */
    public void addPart(DrawingPart part) {
        drawing.addPart(part);
        repaint();
    }

    /**
     * Selects a part
     */
    public void selectPart(DrawingPart part) {
        if (!selectMultiple)
            clearSelection();
        drawing.selectPart(part);
        repaint();
        fireSelectionListener();
    }

    /**
     * Unselects a part
     */
    public void unselectPart(DrawingPart part) {
        drawing.unselectPart(part);
        repaint();
        fireSelectionListener();
    }

    /**
     * Clear selection
     */
    public void clearSelection() {
        drawing.clearSelection();
        repaint();
        fireSelectionListener();
    }

    /**
     * Deletes all parts in the selection
     */
    public void deleteSelection() {
        drawing.deleteSelection();
        repaint();
        fireSelectionListener();
    }

    /**
     * Gets the parts at the given coordinate
     */
    public List<DrawingPart> getParts(int x, int y) {
        return drawing.getParts(x, y);
    }

    /**
     * Gets the parts in the drawing
     */
    public List<DrawingPart> getParts() {
        return drawing.getParts();
    }

    @Override
    protected void paintView() {
        super.paintView();
        List<DrawingPart> parts = drawing.getParts();
        for ( DrawingPart d : parts ){
            TwoDGraphics p = graphics.create();
            p.translate(d.getX(),d.getY());
            d.paintPart(p,viewportZoom);
        }
    }

    /**
     * Call the selection change listeners
     */
    private void fireSelectionListener() {
        for ( PartSelectionListener list : selectionListeners )
            list.onSelectionChanged( this );
    }

    /**
     * Call the edit mode change listeners
     */
    private void fireEditModeListener() {
        for ( EditModeListener list : editModeListeners )
            list.onEditModeChanged( this );
    }

    /**
     * Gets whether or not we are currently selecting multiple objects.
     * If we are not, selectPart will unselect any previously
     * selected parts.
     */
    public boolean getSelectMultiple() {
        return selectMultiple;
    }

    /**
     * Sets whether or not we are currently selecting multiple objects.
     * If we are not, selectPart will unselect any previously
     * selected parts.
     */
    public void setSelectMultiple(boolean selectMultiple) {
        this.selectMultiple = selectMultiple;
    }

    /**
     * Registers a listener for changes to the edit mode
     */
    public void addEditModeListener(EditModeListener editModeListener) {
        editModeListeners.add(editModeListener);
    }

    /**
     * Registers a listener for changes to the selected part list
     */
    public void addPartSelectionListener(PartSelectionListener selectionListener) {
        selectionListeners.add(selectionListener);
    }

    /**
     * Gets the edit mode
     */
    public int getEditMode() {
        return editMode;
    }

    /**
     * Sets the edit mode
     */
    public void setEditMode(int editMode) {
        this.editMode = editMode;
        fireEditModeListener();
    }
}
