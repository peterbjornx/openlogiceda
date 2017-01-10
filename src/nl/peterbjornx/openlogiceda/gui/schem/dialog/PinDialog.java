package nl.peterbjornx.openlogiceda.gui.schem.dialog;

import nl.peterbjornx.openlogiceda.gui.schem.ComponentView;
import nl.peterbjornx.openlogiceda.model.schem.PinPart;
import nl.peterbjornx.openlogiceda.model.schem.Rotation;

import javax.swing.*;
import java.awt.event.*;

public class PinDialog extends JDialog {
    private final ComponentView view;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField pinNameField;
    private JRadioButton northRadioButton;
    private JRadioButton eastRadioButton;
    private JRadioButton southRadioButton;
    private JRadioButton westRadioButton;
    private ButtonGroup orientationGroup;

    public PinDialog(ComponentView view, PinPart edit) {
        this.view = view;
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

        if (edit != null){
            switch( edit.getOrientation() ){
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
            pinNameField.setText(edit.getName());
        }
    }

    private void onOK() {
        view.onPinDialog(pinNameField.getText(),Rotation.valueOf(orientationGroup.getSelection().getActionCommand()));
        dispose();
    }

    private void onCancel() {
        view.onDialogCancel();
        dispose();
    }

    public static void showDialog(ComponentView view,PinPart edit) {
        PinDialog dialog = new PinDialog(view,edit);
        dialog.setLocationRelativeTo(view);
        dialog.pack();
        dialog.setVisible(true);
    }
}
