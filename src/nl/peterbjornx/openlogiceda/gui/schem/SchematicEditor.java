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

import nl.peterbjornx.openlogiceda.gui.schem.dialog.SettingDialog;
import nl.peterbjornx.openlogiceda.gui.view.DrawingView;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * @author Peter Bosch
 */
public class SchematicEditor {
    private JPanel mainPane;
    private JButton button1;
    private JButton button2;
    private JToggleButton selectModeBtn;
    private JToggleButton compModeBtn;
    private SchematicView schematicView;
    private JToggleButton rectModeBtn;
    private JToggleButton textModeBtn;
    private JToggleButton lineModeBtn;
    private JToggleButton wireModeBtn;
    private ButtonGroup modeGroup;
    private JMenuBar menuBar;

    public SchematicEditor() {
        menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        menuBar.add(file);
        file.setMnemonic('F');
        JMenuItem open = file.add("Open");
        open.setIcon(new ImageIcon(getClass().getResource("/res/open.png")));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,KeyEvent.CTRL_MASK));
        open.addActionListener(e-> schematicView.openComponent());
        JMenuItem save = file.add("Save");
        save.setIcon(new ImageIcon(getClass().getResource("/res/save.png")));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        save.addActionListener(e-> schematicView.saveComponent());
        file.addSeparator();
        JMenuItem quit = file.add("Quit");
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_MASK));
        quit.addActionListener(e->quitAction());
        JMenu edit = new JMenu("Edit");
        menuBar.add(edit);
        edit.setMnemonic('E');
        JMenuItem undo = edit.add("Undo");
        undo.setIcon(new ImageIcon(getClass().getResource("/res/undo.png")));
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
        undo.addActionListener(e-> schematicView.undo());
        JMenuItem redo = edit.add("Redo");
        redo.setIcon(new ImageIcon(getClass().getResource("/res/redo.png")));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_MASK));
        redo.addActionListener(e-> schematicView.redo());
        edit.addSeparator();
        JMenuItem prop = edit.add("Schematic properties");
        //redo.setIcon(new ImageIcon(getClass().getResource("/res/redo.png")));
        //redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_MASK));
        prop.addActionListener(e-> schematicView.props());
        edit.addSeparator();
        JMenuItem prefs = edit.add("Preferences");
        //redo.setIcon(new ImageIcon(getClass().getResource("/res/redo.png")));
        //redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_MASK));
        prefs.addActionListener(e-> SettingDialog.main());
        edit.addSeparator();

        selectModeBtn.addActionListener(e -> updateFromMode());
        compModeBtn.addActionListener(e -> updateFromMode());
        textModeBtn.addActionListener(e -> updateFromMode());
        rectModeBtn.addActionListener(e -> updateFromMode());
        lineModeBtn.addActionListener(e -> updateFromMode());
        wireModeBtn.addActionListener(e -> updateFromMode());
        schematicView.addEditModeListener(c -> updateToMode());
        selectModeBtn.setActionCommand("S");
        compModeBtn.setActionCommand("A");
        textModeBtn.setActionCommand("T");
        rectModeBtn.setActionCommand("R");
        lineModeBtn.setActionCommand("L");
    }

    private void quitAction() {
        schematicView.close();
        //TODO: Actually close editor
    }

    private void updateToMode() {
        switch ( schematicView.getEditMode() ) {
            case DrawingView.MODE_SELECT:
                selectModeBtn.setSelected(true);
                break;
            case SchematicView.MODE_COMP:
                compModeBtn.setSelected(true);
                break;
            case SchematicView.MODE_TEXT:
                textModeBtn.setSelected(true);
                break;
            case SchematicView.MODE_RECT:
                rectModeBtn.setSelected(true);
                break;
            case SchematicView.MODE_LINE:
                lineModeBtn.setSelected(true);
                break;
            case SchematicView.MODE_WIRE:
                wireModeBtn.setSelected(true);
                break;
        }
    }

    private void updateFromMode() {
        switch( modeGroup.getSelection().getActionCommand() ) {
            case "S":
                schematicView.setEditMode(DrawingView.MODE_SELECT);
                break;
            case "A":
                schematicView.setEditMode(SchematicView.MODE_COMP);
                break;
            case "R":
                schematicView.setEditMode(SchematicView.MODE_RECT);
                break;
            case "T":
                schematicView.setEditMode(SchematicView.MODE_TEXT);
                break;
            case "L":
                schematicView.setEditMode(SchematicView.MODE_LINE);
                break;
            case "W":
                schematicView.setEditMode(SchematicView.MODE_WIRE);
                break;
        }
    }

    public JMenuBar getMenuBar() {return menuBar;}

    public JPanel getMainPane() {
        return mainPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void postInit() {
        schematicView.requestFocusInWindow();
    }
}
