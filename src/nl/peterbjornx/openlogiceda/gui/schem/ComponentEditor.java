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

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import nl.peterbjornx.openlogiceda.gui.schem.dialog.SettingDialog;
import nl.peterbjornx.openlogiceda.gui.view.DrawingView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
    private JFrame frame;

    public ComponentEditor() {
        menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        menuBar.add(file);
        file.setMnemonic('F');
        JMenuItem neww = file.add("New");
        neww.setIcon(new ImageIcon(getClass().getResource("/res/new.png")));
        neww.addActionListener(e -> componentView.newComponent());
        JMenuItem open = file.add("Open");
        open.setIcon(new ImageIcon(getClass().getResource("/res/open.png")));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        open.addActionListener(e -> componentView.openComponent());
        JMenuItem save = file.add("Save");
        save.setIcon(new ImageIcon(getClass().getResource("/res/save.png")));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        save.addActionListener(e -> componentView.saveComponent());
        JMenuItem saveAs = file.add("Save As");
        saveAs.setIcon(new ImageIcon(getClass().getResource("/res/save.png")));
        //saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        saveAs.addActionListener(e -> componentView.saveAsComponent());
        file.addSeparator();
        JMenuItem quit = file.add("Quit");
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
        quit.addActionListener(e -> quitAction());
        JMenu edit = new JMenu("Edit");
        menuBar.add(edit);
        edit.setMnemonic('E');
        JMenuItem undo = edit.add("Undo");
        undo.setIcon(new ImageIcon(getClass().getResource("/res/undo.png")));
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));
        undo.addActionListener(e -> componentView.undo());
        JMenuItem redo = edit.add("Redo");
        redo.setIcon(new ImageIcon(getClass().getResource("/res/redo.png")));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK));
        redo.addActionListener(e -> componentView.redo());
        edit.addSeparator();
        JMenuItem prop = edit.add("Component properties");
        //redo.setIcon(new ImageIcon(getClass().getResource("/res/redo.png")));
        //redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_MASK));
        prop.addActionListener(e -> componentView.props());
        edit.addSeparator();
        JMenuItem prefs = edit.add("Preferences");
        //redo.setIcon(new ImageIcon(getClass().getResource("/res/redo.png")));
        //redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_MASK));
        prefs.addActionListener(e -> SettingDialog.main());
        edit.addSeparator();

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
        frame = new JFrame("Schematic editor");
        frame.add(getMainPane());
        frame.setJMenuBar(getMenuBar());
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             *
             * @param e
             */
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (componentView.close())
                    frame.dispose();
            }
        });
        postInit();
    }

    private void quitAction() {
        componentView.close();
        frame.dispose();
    }

    private void updateToMode() {
        switch (componentView.getEditMode()) {
            case DrawingView.MODE_SELECT:
                selectModeBtn.setSelected(true);
                break;
            case ComponentView.MODE_PIN:
                pinModeBtn.setSelected(true);
                break;
            case ComponentView.MODE_TEXT:
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
        switch (modeGroup.getSelection().getActionCommand()) {
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
                componentView.setEditMode(ComponentView.MODE_TEXT);
                break;
            case "L":
                componentView.setEditMode(ComponentView.MODE_LINE);
                break;
        }
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public JPanel getMainPane() {
        return mainPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void postInit() {
        componentView.requestFocusInWindow();
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new GTKLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        ComponentEditor ed = new ComponentEditor();
    }

}
