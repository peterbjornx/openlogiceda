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

import nl.peterbjornx.openlogiceda.config.GlobalConfig;
import nl.peterbjornx.openlogiceda.config.KeyBindings;
import nl.peterbjornx.openlogiceda.gui.view.DrawingView;
import nl.peterbjornx.openlogiceda.model.draw.Drawing;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;
import nl.peterbjornx.openlogiceda.model.schem.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

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
    private File openFile = null;
    private Stack<String> undoStack = new Stack<>();
    private Stack<String> redoStack = new Stack<>();
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
        } else if ( kc == KeyBindings.getComponentEdit()) {
            edit();
            return true;
        } else if ( kc == KeyBindings.getComponentDelete()) {
            delete();
            return true;
        }
        return false;
    }

    /**
     * Initiates the edit action
     */
    private void edit() {
        markUndo();
        doAction(null, l -> {
            ((CompSymbolPart) l.get(0)).edit(this);
            repaint();
        }, false);
    }

    /**
     * Initiates the rotate action
     */
    private void rotate() {
        markUndo();
        doAction(null, parts -> {
            for ( DrawingPart _p : parts ){
                CompSymbolPart p = (CompSymbolPart) _p;
                p.setOrientation(Rotation.getNext(p.getOrientation()));
                repaint();
            }
        }, true);
    }

    /**
     * Initiates the delete action
     */
    private void delete() {
        markUndo();
        doAction(null, parts -> {
            for ( DrawingPart _p : parts ){
                CompSymbolPart p = (CompSymbolPart) _p;
                deleteSelection();
                repaint();
            }
        }, true);
    }

    /**
     * Initiates the move action
     */
    private void move() {
        markUndo();
        doAction(null, parts -> {
            editState = STATE_MOVE;
            repaint();
        },true);
    }

    /**
     * Initiates the move action
     */
    private void copy() {
        markUndo();
        doAction(null, orig -> {
            List<DrawingPart> list = new LinkedList<>();
            if (orig.size() == 0) {
                select(null);
                orig = getSelectedParts();
            }
            for (DrawingPart p : orig) {
                DrawingPart c = p.copy();
                addPart(c);
                list.add(c);
            }
            clearSelection();
            selectParts(list);
            editState = STATE_ADD;
            repaint();
        }, true);
    }

    /**
     * Undoes the last change
     */
    public void undo() {
        if (undoStack.isEmpty())
            return;
        SchematicComponent c= (SchematicComponent) getDrawing();
        String lastChange = c.store();
        redoStack.push(lastChange);
        setDrawing(SchematicComponent.load(undoStack.pop()));
        repaint();
    }

    /**
     * Redoes the last undone change
     */
    public void redo() {
        if (redoStack.isEmpty())
            return;
        SchematicComponent c= (SchematicComponent) getDrawing();
        String lastChange = c.store();
        undoStack.push(lastChange);
        setDrawing(SchematicComponent.load(redoStack.pop()));
        repaint();
    }

    /**
     * Stores a change for undo
     */
    private void markUndo() {
        if (!redoStack.isEmpty())
            redoStack.clear();
        SchematicComponent c= (SchematicComponent) getDrawing();
        String lastChange = c.store();
        undoStack.push(lastChange);
    }


    @Override
    protected void buildContextMenu(JPopupMenu menu) {
        if (editMode != MODE_SELECT) {
            menu.add("End mode").addActionListener(e -> setEditMode(MODE_SELECT));
            menu.addSeparator();
        }
        List<DrawingPart> parts = getSelectedParts();
        if ( parts.size() <= 1 )
            parts = getParts(cursorX, cursorY);
        if ( parts.size() == 1 )
            buildPartMenu((CompSymbolPart) parts.get(0),menu);
        else if ( parts.size() > 1 ) {
            for (DrawingPart p : parts){
                JMenu submenu = new JMenu(p.toString());
                menu.add(submenu);
                buildPartMenu((CompSymbolPart) p,submenu);
            }
            menu.addSeparator();
            buildBlockMenu(parts,menu);
        }
        super.buildContextMenu(menu);
    }

    public boolean close() {
        if (!saveChangesDialog())
            return false;
        redoStack.clear();
        undoStack.clear();
        cancel();
        setEditMode(MODE_SELECT);
        clearSelection();
        return true;
    }

    public void openComponent() {
        if (openFile != null && !close())
            return;
        if (!openDialog())
            return;
        try {
            setDrawing(SchematicComponent.load(openFile));
        } catch (Exception e) {
            //TODO: Show error dialog
            e.printStackTrace();
        }
    }

    public void saveComponent() {
        if (openFile == null && !saveAsDialog())
            return;
        try {
            SchematicComponent c = (SchematicComponent) getDrawing();
            c.store(openFile);
        } catch (Exception e) {
            //TODO: Show error dialog
            e.printStackTrace();
        }
    }

    public boolean saveChangesDialog() {
        int option = JOptionPane.showConfirmDialog(this,"Do you want to save the current document?",
                "Possible loss of data!",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        switch (option) {
            case JOptionPane.YES_OPTION:
                saveComponent();
            case JOptionPane.NO_OPTION:
                return true;
        }
        return false;
    }

    private boolean saveAsDialog() {
        if (GlobalConfig.getUseAWTFileDialog()) {
            FileDialog chooser = new FileDialog((Frame)null,"Test");
            chooser.setFilenameFilter(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.toLowerCase().endsWith(".cmp"))
                        return true;
                    else if (name.toLowerCase().endsWith(".xml"))
                        return true;
                    else
                        return false;
                }
            });
            chooser.setMode(FileDialog.SAVE);
            chooser.setMultipleMode(false);
            chooser.setLocationRelativeTo(this);
            chooser.setVisible(true);
            if ( chooser.getFile() == null)
                return false;
            openFile = new File(chooser.getDirectory()+chooser.getFile());
            return true;
        } else {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Schematic Components", "cmp");
            chooser.setFileFilter(filter);
            chooser.setDialogTitle("Save As");
            chooser.setMultiSelectionEnabled(false);
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                openFile = chooser.getSelectedFile();
                return true;
            }
            return false;
        }
    }

    private boolean openDialog() {
        if (GlobalConfig.getUseAWTFileDialog()) {
            FileDialog chooser = new FileDialog((Frame)null,"Test");
            chooser.setFilenameFilter(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.toLowerCase().endsWith(".cmp"))
                        return true;
                    else if (name.toLowerCase().endsWith(".xml"))
                        return true;
                    else
                        return false;
                }
            });
            chooser.setMode(FileDialog.LOAD);
            chooser.setMultipleMode(false);
            chooser.setLocationRelativeTo(this);
            chooser.setVisible(true);
            if ( chooser.getFile() == null)
                return false;
            openFile = new File(chooser.getDirectory()+chooser.getFile());
            return true;
        } else {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Schematic Components", "cmp","xml");
            chooser.setFileFilter(filter);
            chooser.setDialogTitle("Open");
            chooser.setMultiSelectionEnabled(false);
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                openFile = chooser.getSelectedFile();
                return true;
            }
            return false;
        }
    }

    private void buildPartMenu(CompSymbolPart part, JComponent menu) {
        JMenuItem item = new JMenuItem("Move");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyBindings.getComponentMove(),0));
        item.addActionListener(e->{
            setSelectMultiple(false);
            selectPart(part);
            move();
        });
        menu.add(item);
        item = new JMenuItem("Rotate");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyBindings.getComponentRotate(),0));
        item.addActionListener(e->{
            setSelectMultiple(false);
            selectPart(part);
            rotate();
        });
        menu.add(item);
        item = new JMenuItem("Copy");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyBindings.getComponentCopy(),0));
        item.addActionListener(e->{
            setSelectMultiple(false);
            selectPart(part);
            copy();
        });
        menu.add(item);
        item = new JMenuItem("Edit");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyBindings.getComponentEdit(),0));
        item.addActionListener(e->{
            setSelectMultiple(false);
            selectPart(part);
            edit();
        });
        menu.add(item);
        item = new JMenuItem("Delete");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyBindings.getComponentDelete(),0));
        item.addActionListener(e->{
            setSelectMultiple(false);
            selectPart(part);
            delete();
        });
        menu.add(item);
    }

    private void buildBlockMenu(List<DrawingPart> parts, JComponent menu) {
        JMenuItem item = new JMenuItem("Move");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyBindings.getComponentMove(),0));
        List<DrawingPart> newParts = new LinkedList<>();
        for (DrawingPart p : parts)
            newParts.add(p);
        item.addActionListener(e->{
            clearSelection();
            setSelectMultiple(true);
            selectParts(newParts);
            move();
            setSelectMultiple(false);
        });
        menu.add(item);
        item = new JMenuItem("Rotate");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyBindings.getComponentRotate(),0));
        item.addActionListener(e->{
            clearSelection();
            setSelectMultiple(true);
            selectParts(newParts);
            rotate();
            setSelectMultiple(false);
        });
        menu.add(item);
        item = new JMenuItem("Copy");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyBindings.getComponentCopy(),0));
        item.addActionListener(e->{
            clearSelection();
            setSelectMultiple(true);
            selectParts(newParts);
            copy();
            setSelectMultiple(false);
        });
        menu.add(item);
        item = new JMenuItem("Delete");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyBindings.getComponentDelete(),0));
        item.addActionListener(e->{
            clearSelection();
            setSelectMultiple(true);
            selectParts(newParts);
            delete();
            setSelectMultiple(false);
        });
        menu.add(item);
    }

    @Override
    protected boolean onMouseClick(int button, int x, int y) {
        if ( editState == STATE_ADD || editState == STATE_MOVE || editState == STATE_ADD_SHAPE ) {
            editState = STATE_NORMAL;
        } else if ( editState == STATE_NORMAL ) {
            if (super.onMouseClick(button, x, y))
                return true;
            switch (editMode) {
                case MODE_PIN:
                    add(new PinPart("", cursorX, cursorY));
                    return true;
                case MODE_RECT:
                    addShape(new CompRectPart());
            }
        }
        return false;
    }

    public void add(CompSymbolPart p){
        markUndo();
        editState = STATE_ADD;
        addPart(p);
        setSelectMultiple(false);
        selectPart(p);
        p.edit(this);
    }

    public void addShape(CompSymbolPart p){
        markUndo();
        editState = STATE_ADD_SHAPE;
        p.setX(cursorX);
        p.setY(cursorY);
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
        } else if ( editState == STATE_ADD_SHAPE ) {
            List<DrawingPart> parts = getSelectedParts();
            for (DrawingPart _p : parts ){
                int top = _p.getTop();
                int left = _p.getLeft();
                int right = roundToGrid(x);
                int bottom = roundToGrid(y);
                _p.setX((left+right)/2);
                _p.setY((top+bottom)/2);
                if ( right >= left ){
                    _p.setLeftExtent(_p.getX() - left);
                    _p.setRightExtent(right - _p.getX());
                } else {
                    _p.setRightExtent(left - _p.getX());
                    _p.setLeftExtent(_p.getX() - right);
                }
                if ( bottom >= top ){
                    _p.setTopExtent(_p.getY() - top);
                    _p.setBottomExtent(bottom - _p.getY());
                } else {
                    _p.setBottomExtent(top - _p.getY());
                    _p.setTopExtent(_p.getY() - bottom);
                }

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
        } else if ( editState == STATE_ADD_SHAPE ) {
            deleteSelection();
        }
        editState = STATE_NORMAL;
    }

    public enum EditState {
        STATE_NORMAL,
        STATE_MOVE,
        STATE_ADD,
        STATE_ADD_SHAPE
    }
}
