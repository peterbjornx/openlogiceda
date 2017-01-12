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
import nl.peterbjornx.openlogiceda.model.schem.SchematicComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Bosch
 */
public class DrawingView extends GridView {

    private static final int CONTEXT_BUTTON = MouseEvent.BUTTON3;
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
     * Whether the user activated select multiple mode
     */
    private boolean userSelectMultiple = false;

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
        return Collections.unmodifiableList(drawing.getSelectedParts());
    }

    public void setDrawing(SchematicComponent drawing) {
        this.drawing = drawing;
    }

    public Drawing getDrawing() {
        return drawing;
    }

    @Override
    protected boolean onMouseClick(int button, int x, int y) {
        if (super.onMouseClick(button, x, y))
            return true;
        if (button == CONTEXT_BUTTON){
            contextMenu();
            return true;
        }
        switch ( editMode ) {
            case MODE_SELECT:
                select(null);
                repaint();
                return true;
        }
        return false;
    }

    @Override
    protected boolean onMouseDown(int button, int x, int y) {
        return super.onMouseDown(button, x, y) || button == CONTEXT_BUTTON;
    }

    /**
     * Executes the select action
     */
    protected boolean select(JMenu disambiguation) {
        return select(disambiguation, null);
    }

    /**
     * Executes the select action
     */
    protected boolean select(JMenu disambiguation, SelectDoneListener listener) {
        List<DrawingPart> parts = drawing.getParts(cursorX,cursorY);
        if ( parts.isEmpty()) {
            if ( !selectMultiple ) {
                clearSelection();
                return true;
            }
            return false;
        }
        if ( parts.size() > 1 )
            selectDisambiguation(disambiguation, parts);
        else
            selectPart(parts.get(0));
        if (listener != null)
            listener.handle(parts);
        return true;
    }

    /**
     * Executes the given action for the selected objects, if there
     * are no selected objects, try to select the objects currently
     * under the cursor.
     */
    protected void doAction(JMenu disambiguation, SelectDoneListener listener, boolean multiple) {
        List<DrawingPart> parts = getSelectedParts();
        if ( (multiple && parts.isEmpty()) || ((!multiple) && parts.size() != 1) ) {
            setSelectMultiple(multiple);
            select(null, p-> {
                listener.handle(p);
                restoreSelectMultiple();
            });
        } else
            listener.handle(parts);

    }

    /**
     * Select a part by disambiguation menu
     */
    protected void selectDisambiguation(JMenu parent, List<DrawingPart> parts)
    {
        disambiguationMenu(parent, parts, this::selectPart);
    }

    /**
     * Shows a menu to select a part
     */
    protected void disambiguationMenu(JMenu parent,List<DrawingPart> parts, DisambiguationListener listener) {
        JPopupMenu menu = null;
        if ( parent == null )
            menu = new JPopupMenu();
        for (DrawingPart p : parts) {
            JMenuItem it = new JMenuItem(p.toString());
            it.addActionListener(e -> listener.handle(p));
            if ( parent == null )
                menu.add(it);
            else
                parent.add(it);
        }
        if ( parent == null ) {
            Point mouse = getMousePosition();
            menu.show(this, mouse.x, mouse.y);
        }
    }

    /**
     * Shows the context menu
     */
    private void contextMenu() {
        JPopupMenu menu = new JPopupMenu();
        buildContextMenu(menu);
        menu.addSeparator();
        menu.add("Close");
        Point mouse = getMousePosition();
        menu.show(this, mouse.x, mouse.y);
    }

    /**
     * Override this to add popup menu actions
     */
    protected void buildContextMenu(JPopupMenu menu){};

    @Override
    protected boolean onKeyDown(int kc) {
        if ( super.onKeyDown( kc ))
            return true;
        if ( kc == KeyBindings.getDrawingSelectMultiple() ) {
            userSelectMultiple = selectMultiple = true;
            return true;
        }
        return false;
    }

    @Override
    protected boolean onKeyUp(int kc) {
        if ( super.onKeyUp( kc ))
            return true;
        if ( kc == KeyBindings.getDrawingSelectMultiple() ) {
            userSelectMultiple = selectMultiple = false;
            return true;
        }
        return false;
    }

    /**
     * Set selectMultiple to the users entered state
     */
    protected void restoreSelectMultiple() {
        selectMultiple = userSelectMultiple;
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
     * Select the parts
     */
    public void selectParts(List<DrawingPart> list) {
        drawing.selectParts(list);
        repaint();
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
        drawCursor();
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

    public interface DisambiguationListener {
        void handle(DrawingPart part);
    }

    public interface SelectDoneListener {
        void handle(List<DrawingPart> part);
    }
}
