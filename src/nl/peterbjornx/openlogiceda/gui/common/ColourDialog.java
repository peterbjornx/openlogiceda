package nl.peterbjornx.openlogiceda.gui.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ColourDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JColorChooser colourChooser;
    private Color colour;

    public ColourDialog(Color c) {
        setTitle("Select colour");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
        colourChooser.setColor(c);
        colour = c;
    }

    private void onOK() {
        colour = colourChooser.getColor();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static Color choose(JComponent c, Color in) {
        ColourDialog dialog = new ColourDialog(in);
        dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        return dialog.colour;
    }

}
