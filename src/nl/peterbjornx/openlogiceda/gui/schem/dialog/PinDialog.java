package nl.peterbjornx.openlogiceda.gui.schem.dialog;

import nl.peterbjornx.openlogiceda.gui.schem.BaseSchematicView;
import nl.peterbjornx.openlogiceda.gui.schem.ComponentView;
import nl.peterbjornx.openlogiceda.model.schem.PinPart;
import nl.peterbjornx.openlogiceda.model.schem.Rotation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PinDialog extends JDialog {
    private final BaseSchematicView view;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField pinNameField;
    private JRadioButton northRadioButton;
    private JRadioButton eastRadioButton;
    private JRadioButton southRadioButton;
    private JRadioButton westRadioButton;
    private JRadioButton normalRadioButton;
    private JRadioButton invertedRadioButton;
    private JRadioButton clockRadioButton;
    private JRadioButton invertedClockRadioButton;
    private ButtonGroup typeGroup;
    private ButtonGroup orientationGroup;
    private PinPart part;

    public PinDialog(BaseSchematicView view, PinPart edit) {
        this.view = view;
        setTitle("Edit pin");
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

        switch (edit.getOrientation()) {
            case NORTH:
                northRadioButton.setSelected(true);
                break;
            case EAST:
                eastRadioButton.setSelected(true);
                break;
            case SOUTH:
                southRadioButton.setSelected(true);
                break;
            case WEST:
                westRadioButton.setSelected(true);
                break;
        }

        switch (edit.getStyle()) {
            case NORMAL:
                normalRadioButton.setSelected(true);
                break;
            case INVERTED:
                invertedRadioButton.setSelected(true);
                break;
            case INVERTED_CLOCK:
                invertedClockRadioButton.setSelected(true);
                break;
            case CLOCK:
                clockRadioButton.setSelected(true);
                break;
        }
        pinNameField.setText(edit.getName());
        this.part = edit;
    }

    private void onOK() {
        part.setOrientation(Rotation.valueOf(orientationGroup.getSelection().getActionCommand()));
        part.setStyle(PinPart.PinStyle.valueOf(typeGroup.getSelection().getActionCommand()));
        part.setName(pinNameField.getText());
        dispose();
    }

    private void onCancel() {
        view.cancel();
        dispose();
    }

    public static void showDialog(BaseSchematicView view, PinPart edit) {
        PinDialog dialog = new PinDialog(view, edit);
        dialog.setLocationByPlatform(true);
        dialog.pack();
        dialog.setVisible(true);
    }

}
