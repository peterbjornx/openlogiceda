package nl.peterbjornx.openlogiceda.gui.schem;/*
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
import nl.peterbjornx.openlogiceda.gui.schem.dialog.PinDialog;
import nl.peterbjornx.openlogiceda.gui.view.DrawingView;
import nl.peterbjornx.openlogiceda.model.draw.Drawing;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;
import nl.peterbjornx.openlogiceda.model.schem.CompSymbolPart;
import nl.peterbjornx.openlogiceda.model.schem.PinPart;
import nl.peterbjornx.openlogiceda.model.schem.Rotation;
import nl.peterbjornx.openlogiceda.model.schem.SchematicComponent;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import static nl.peterbjornx.openlogiceda.gui.schem.ComponentView.EditState.*;

/**
 * @author Peter Bosch
 */
public class ComponentView extends DrawingView {
    public final static int MODE_PIN = 1;
    public final static int MODE_RECT = 2;
    public final static int MODE_LINE = 3;
    public final static int MODE_LABEL = 4;
    private EditState editState = STATE_NORMAL;
    /**
     * Creates a new drawing view
     */
    public ComponentView() {
        super(new SchematicComponent("testing"));
        addEditModeListener(c->{
            cancel();
        });
    }

    @Override
    protected boolean onKeyDown(int kc) {
        if (super.onKeyDown(kc))
            return true;
        if ( kc == KeyBindings.getComponentModeSelect()) {
            setEditMode(MODE_SELECT);
            return true;
        } else if ( kc == KeyBindings.getComponentModePin()) {
            setEditMode(MODE_PIN);
            return true;
        } else if ( kc == KeyBindings.getComponentRotate()) {
            rotate();
            return true;
        } else if ( kc == KeyBindings.getComponentMove()) {
            move();
            return true;
        } else if ( kc == KeyBindings.getComponentCopy()) {
            copy();
            return true;
        }
        return false;
    }

    /**
     * Initiates the rotate action
     */
    private void rotate() {
        List<DrawingPart> parts = getSelectedParts();
        if ( parts.size() == 0 ) {
            select(null);
            parts = getSelectedParts();
        }
        for ( DrawingPart _p : parts ){
            CompSymbolPart p = (CompSymbolPart) _p;
            p.setOrientation(Rotation.getNext(p.getOrientation()));
        }
        repaint();
    }

    /**
     * Initiates the move action
     */
    private void move() {
        List<DrawingPart> parts = getSelectedParts();
        if ( parts.size() == 0 ) {
            select(null);
        }
        editState = STATE_MOVE;
        repaint();
    }

    /**
     * Initiates the move action
     */
    private void copy() {
        List<DrawingPart> list = new LinkedList<>();
        List<DrawingPart> orig = getSelectedParts();
        if ( orig.size() == 0 ) {
            select(null);
            orig = getSelectedParts();
        }
        for ( DrawingPart p : orig ) {
            DrawingPart c = p.copy();
            addPart(c);
            list.add(c);
        }
        clearSelection();
        selectParts(list);
        editState = STATE_ADD;
        repaint();
    }

    @Override
    protected boolean onMouseClick(int button, int x, int y) {
        if ( editState == STATE_ADD || editState == STATE_MOVE ) {
            editState = STATE_NORMAL;
        } else if ( editState == STATE_NORMAL ) {
            if (super.onMouseClick(button, x, y))
                return true;
            switch (editMode) {
                case MODE_PIN:
                    add(new PinPart("", cursorX, cursorY));
                    return true;
            }
        }
        return false;
    }

    public void add(CompSymbolPart p){
        editState = STATE_ADD;
        addPart(p);
        setSelectMultiple(false);
        selectPart(p);
        p.edit(this);
    }

    @Override
    protected boolean onMouseMove(int x, int y) {
        int oldX = cursorX;//TODO: Make this better
        int oldY = cursorY;
        if (super.onMouseMove(x, y))
            return true;
        if ( editState == STATE_ADD || editState == STATE_MOVE ) {
            List<DrawingPart> parts = getSelectedParts();
            for (DrawingPart _p : parts ){
                _p.setX( roundToGrid(x) - oldX + _p.getX() );
                _p.setY( roundToGrid(y) - oldY + _p.getY() );
            }
            repaint();
            return true;
        }
        return false;
    }

    @Override
    protected boolean onMouseDown(int button, int x, int y) {
        if (super.onMouseDown(button, x, y))
            return true;
        return false;
    }

    /**
     * Cancels whatever editor action is running
     */
    public void cancel() {
        if ( editState == STATE_ADD ) {
            deleteSelection();
        }
        editState = STATE_NORMAL;
    }

    public enum EditState {
        STATE_NORMAL,
        STATE_MOVE,
        STATE_ADD
    }
}
