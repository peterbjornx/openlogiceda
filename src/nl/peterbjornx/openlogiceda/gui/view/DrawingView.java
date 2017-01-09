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
import nl.peterbjornx.openlogiceda.model.draw.Drawing;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Peter Bosch
 */
public class DrawingView extends GridView {

    public static final int MODE_SELECT = 0;
    private Drawing drawing;
    protected int editMode = MODE_SELECT;

    /**
     * Whether multiple selection is enabled
     */
    private boolean selectMultiple = false;

    public DrawingView(Drawing drawing) {
        this.drawing = drawing;
        setViewSize( drawing.getWidth(), drawing.getHeight() );
    }

    @Override
    protected boolean onMouseClick(int button, int x, int y) {
        if ( super.onMouseClick(button, x, y) )
            return true;
        List<DrawingPart> parts = drawing.getParts(x,y);
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

    public void addPart(DrawingPart part) {
        drawing.addPart(part);
        repaint();
    }

    public void selectPart(DrawingPart part) {
        if (!selectMultiple)
            clearSelection();
        drawing.selectPart(part);
        repaint();
    }

    public void unselectPart(DrawingPart part) {
        drawing.unselectPart(part);
        repaint();
    }

    public void clearSelection() {
        drawing.clearSelection();
        repaint();
    }

    public List<DrawingPart> getParts(int x, int y) {
        return drawing.getParts(x, y);
    }

    public List<DrawingPart> getParts() {
        return drawing.getParts();
    }

    @Override
    protected void paintView() {
        super.paintView();
        List<DrawingPart> parts = drawing.getParts();
        for ( DrawingPart d : parts ){
            Graphics2D p = (Graphics2D) graphics.create();
            p.translate(d.getX(),d.getY());
            d.paintPart(p,viewportZoom);
        }
    }

    public boolean isSelectMultiple() {
        return selectMultiple;
    }

    public void setSelectMultiple(boolean selectMultiple) {
        this.selectMultiple = selectMultiple;
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
    }
}
