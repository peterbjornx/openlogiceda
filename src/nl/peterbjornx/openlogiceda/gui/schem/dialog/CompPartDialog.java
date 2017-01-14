package nl.peterbjornx.openlogiceda.gui.schem.dialog;

import nl.peterbjornx.openlogiceda.gui.schem.BaseSchematicView;
import nl.peterbjornx.openlogiceda.model.schem.BaseSchematicPart;
import nl.peterbjornx.openlogiceda.model.schem.ComponentPart;

import javax.swing.*;
import java.awt.event.*;

public class CompPartDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameTextField;
    private JTextField refTextField;
    private JTextField simConfigTextField;
    private BaseSchematicView c;
    private ComponentPart part;

    public CompPartDialog(BaseSchematicView c, ComponentPart p) {
        this.c = c;
        part = p;
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
        refTextField.setText(part.getReference());
        nameTextField.setText(part.getName());
        simConfigTextField.setText(part.getSimConfig());
    }

    private void onOK() {
        part.setReference(refTextField.getText());
        part.setName(nameTextField.getText());
        part.setSimConfig(simConfigTextField.getText());
        dispose();
    }

    private void onCancel() {
        c.cancel();
        dispose();
    }

    public static void main(BaseSchematicView c, ComponentPart p) {
        CompPartDialog dialog = new CompPartDialog(c,p);
        dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }
}
