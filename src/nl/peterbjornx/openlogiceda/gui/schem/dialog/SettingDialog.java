package nl.peterbjornx.openlogiceda.gui.schem.dialog;

import nl.peterbjornx.openlogiceda.config.SchematicColours;
import nl.peterbjornx.openlogiceda.gui.schem.JColourButton;

import javax.swing.*;
import javax.xml.validation.Schema;
import java.awt.event.*;

public class SettingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTabbedPane dialogTabs;
    private JColourButton backgroundJColourButton;
    private JColourButton gridJColourButton;
    private JColourButton cursorJColourButton;
    private JColourButton pinJColourButton;
    private JColourButton textJColourButton;
    private JColourButton shapesJColourButton;

    public SettingDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Preferences");

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        backgroundJColourButton.setColour(SchematicColours.getBackgroundColour());
        gridJColourButton.setColour(SchematicColours.getGridColour());
        cursorJColourButton.setColour(SchematicColours.getCursorColour());
        textJColourButton.setColour(SchematicColours.getDefaultTextColour());
        shapesJColourButton.setColour(SchematicColours.getDefaultShapeColour());
        pinJColourButton.setColour(SchematicColours.getPinColour());
    }

    private void onOK() {
        SchematicColours.setBackgroundColour(backgroundJColourButton.getColour());
        SchematicColours.setGridColour(gridJColourButton.getColour());
        SchematicColours.setCursorColour(cursorJColourButton.getColour());
        SchematicColours.setDefaultShapeColour(shapesJColourButton.getColour());
        SchematicColours.setDefaultTextColour(textJColourButton.getColour());
        SchematicColours.setPinColour(pinJColourButton.getColour());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main() {
        SettingDialog dialog = new SettingDialog();
        dialog.setLocationByPlatform(true);
        dialog.pack();
        dialog.setVisible(true);
    }
}
