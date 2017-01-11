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

import nl.peterbjornx.openlogiceda.gui.view.DrawingView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author Peter Bosch
 */
public class ComponentEditor {
    private JPanel mainPane;
    private JButton button1;
    private JButton button2;
    private JToggleButton selectModeBtn;
    private JToggleButton pinModeBtn;
    private ComponentView componentView;
    private JToggleButton rectModeBtn;
    private JToggleButton textModeBtn;
    private JToggleButton lineModeBtn;
    private ButtonGroup modeGroup;
    private JMenuBar menuBar;

    public ComponentEditor() {
        menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        menuBar.add(file);
        file.setMnemonic('F');
        JMenuItem open = file.add("Open");
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,KeyEvent.CTRL_MASK));
        open.addActionListener(e->componentView.openComponent());
        JMenuItem save = file.add("Save");
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        save.addActionListener(e->componentView.saveComponent());
        file.addSeparator();
        JMenuItem quit = file.add("Quit");
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_MASK));
        save.addActionListener(e->quitAction());

        selectModeBtn.addActionListener(e -> updateFromMode());
        pinModeBtn.addActionListener(e -> updateFromMode());
        textModeBtn.addActionListener(e -> updateFromMode());
        rectModeBtn.addActionListener(e -> updateFromMode());
        lineModeBtn.addActionListener(e -> updateFromMode());
        componentView.addEditModeListener(c -> updateToMode());
        selectModeBtn.setActionCommand("S");
        pinModeBtn.setActionCommand("P");
        textModeBtn.setActionCommand("T");
        rectModeBtn.setActionCommand("R");
        lineModeBtn.setActionCommand("L");
    }

    private void quitAction() {
        componentView.saveChangesDialog();
        //TODO: Actually close editor
    }

    private void updateToMode() {
        switch ( componentView.getEditMode() ) {
            case DrawingView.MODE_SELECT:
                selectModeBtn.setSelected(true);
                break;
            case ComponentView.MODE_PIN:
                pinModeBtn.setSelected(true);
                break;
            case ComponentView.MODE_LABEL:
                textModeBtn.setSelected(true);
                break;
            case ComponentView.MODE_RECT:
                rectModeBtn.setSelected(true);
                break;
            case ComponentView.MODE_LINE:
                lineModeBtn.setSelected(true);
                break;
        }
    }

    private void updateFromMode() {
        switch( modeGroup.getSelection().getActionCommand() ) {
            case "S":
                componentView.setEditMode(DrawingView.MODE_SELECT);
                break;
            case "P":
                componentView.setEditMode(ComponentView.MODE_PIN);
                break;
            case "R":
                componentView.setEditMode(ComponentView.MODE_RECT);
                break;
            case "T":
                componentView.setEditMode(ComponentView.MODE_LABEL);
                break;
            case "L":
                componentView.setEditMode(ComponentView.MODE_LINE);
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
        componentView.requestFocusInWindow();
    }
}
